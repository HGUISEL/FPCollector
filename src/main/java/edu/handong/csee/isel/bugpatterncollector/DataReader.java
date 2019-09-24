package edu.handong.csee.isel.bugpatterncollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import edu.handong.csee.isel.datastructure.DiffInfo;

public class DataReader {

	public ArrayList<DiffInfo> run(String path) throws IOException, GitAPIException {
		ArrayList<DiffInfo> diffList = readResult(path) ;
		getDiff(diffList) ;
		
		return diffList ;
	}

	private ArrayList<DiffInfo> readResult(String resultPath) throws IOException {
		File resultFile = new File(resultPath) ;
		BufferedReader br = new BufferedReader(new FileReader(resultFile)) ;
		ArrayList<DiffInfo> diffList = new ArrayList<DiffInfo>() ;

		String line = "" ;
		line = br.readLine() ;
		while((line = br.readLine()) != null) {
			DiffInfo diff = new DiffInfo() ;
			String[] token = line.split(",", -1) ;

			if (token.length < 6) continue ;

			diff.BI.id = token[0] ;
			diff.fix.id = token[3] ;
			diff.BI.filePath = token[1] ;
			diff.fix.filePath = token[2] ;
			diff.filePath = token[2] ;
			diff.prefixLine = Integer.parseInt(token[5]) ;

			diffList.add(diff) ;
		}
		br.close();

		return diffList ;
	}
	
	static public DiffAlgorithm diffAlgorithm = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS);
	static public RawTextComparator diffComparator = RawTextComparator.WS_IGNORE_ALL;

	private void getDiff(ArrayList<DiffInfo> diffList) throws IOException, GitAPIException {
		ObjectId commitID ;
		Repository repo = setRepo() ;
		BlameCommand blamer = new BlameCommand(repo) ;

		for (DiffInfo d: diffList) {
			commitID = repo.resolve(d.fix.id + "~1");
			blamer.setDiffAlgorithm(diffAlgorithm);
			blamer.setTextComparator(diffComparator);
			blamer.setFollowFileRenames(true);
			blamer.setStartCommit(commitID);
			blamer.setFilePath(d.fix.filePath);
			BlameResult blame = blamer.call();

			// biPath might be different from fixPath
			boolean renamed = false;
			if (blame == null){
				renamed = true;
				commitID = repo.resolve(d.fix.id);
				blamer.setDiffAlgorithm(diffAlgorithm);
				blamer.setTextComparator(diffComparator);
				blamer.setFollowFileRenames(false);
				blamer.setStartCommit(commitID);
				blamer.setFilePath(d.BI.filePath);
				blame = blamer.call();
			}

			d.fixCode = fetchBlob(repo, d.fix.id, d.fix.filePath) ;
			d.prefixCode = (!renamed)? fetchBlob(repo, d.fix.id + "~1", d.fix.filePath)
					: fetchBlob(repo, d.BI.id, d.BI.filePath) ;
			
//			String[] fixLines = d.fixCode.split("\n") ;
//			String[] prefixLines = d.prefixCode.split("\n") ;
//			createFile(d) ;
			
			d.editList = getEditListFromDiff(d.fixCode,d.prefixCode);
			System.out.println("EditList Length: " + d.editList.size()) ;
			
			for (Edit edit : d.editList) {
				if (edit.getBeginA() == edit.getEndA() && edit.getBeginB() < edit.getEndB())
					System.out.println("Insert Edit : [" + edit.getBeginB() + ", " + edit.getEndB() + ")" ) ;
				else if (edit.getBeginA() < edit.getEndA() && edit.getBeginB() == edit.getEndB())
					System.out.println("Delete Edit : [" + edit.getBeginA() + ", " + edit.getEndA() + ")" ) ;
				else if (edit.getBeginA() < edit.getEndA() && edit.getBeginB() < edit.getEndB())
					System.out.println("Replace Edit : [" + edit.getBeginA() + ", " + edit.getEndA() + ") | [" + edit.getBeginB() + ", " + edit.getEndB() + ")" ) ;
			}
			
			//			System.out.println("Fix: " + d.fixCode) ;
			//			System.out.println("Prefix: " + d.prefixCode) ;
//			break ;
		}
	}
	
	static public EditList getEditListFromDiff(String file1, String file2) {
		RawText rt1 = new RawText(file1.getBytes());
		RawText rt2 = new RawText(file2.getBytes());
		EditList diffList = new EditList();

		//diffList.addAll(new HistogramDiff().diff(RawTextComparator.WS_IGNORE_ALL, rt1, rt2));
		diffList.addAll(diffAlgorithm
				.diff(diffComparator, rt1, rt2));
		return diffList;
	}
	
	private void createFile(DiffInfo d) throws IOException {
		File fixFile = new File("/Users/yujin/ISEL/NewProject/Testcode/fix.java") ;
		File prefixFile = new File("/Users/yujin/ISEL/NewProject/Testcode/prefix.java") ;
		
		FileWriter fixFw = new FileWriter(fixFile) ;
		FileWriter prefixFw = new FileWriter(prefixFile) ;
		
		fixFw.write(d.fixCode);
		fixFw.flush();
		prefixFw.write(d.prefixCode);
		prefixFw.flush();
		
		fixFw.close();
		prefixFw.close();
	}

	static public String fetchBlob(Repository repo, String revSpec, String path) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException {
		// Resolve the revision specification
		final ObjectId id = repo.resolve(revSpec);

		// Makes it simpler to release the allocated resources in one go
		ObjectReader reader = repo.newObjectReader();

		// Get the commit object for that revision
		RevWalk walk = new RevWalk(reader);
		RevCommit commit = walk.parseCommit(id);
		walk.close();

		// Get the revision's file tree
		RevTree tree = commit.getTree();
		// .. and narrow it down to the single file's path
		TreeWalk treewalk = TreeWalk.forPath(reader, path, tree);

		if (treewalk != null) {
			// use the blob id to read the file's data
			byte[] data = reader.open(treewalk.getObjectId(0)).getBytes();
			reader.close();
			return new String(data, "utf-8");
		} else {
			return "";
		}
	}

	private Repository setRepo() throws IOException {
		try (Repository repo = new FileRepositoryBuilder()
				.setGitDir(new File("/Users/yujin/ISEL/NewProject/BugPatchCollector/build/distributions/BugPatchCollector/bin/repositories/zookeeper/.git"))
				.readEnvironment()
				.findGitDir()
				.build()) {
			System.out.println("Having repository: " + repo.getDirectory()) ;

			// the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
			//            Ref head = repo.exactRef("refs/heads/master");
			//            System.out.println("Ref of refs/heads/master: " + head);
			return repo ;
		}
		// clean up here to not keep using more and more disk-space for these samples
		//        FileUtils.deleteDirectory(repoDir.getParentFile());
	}
	
//	private static AbstractTreeIterator prepareTreeParser(Repository repository, ObjectId objectId) throws IOException {
//		// from the commit we can build the tree which allows us to construct the TreeParser
//		//noinspection Duplicates
//		try (RevWalk walk = new RevWalk(repository)) {
//			RevCommit commit = walk.parseCommit(objectId);
//			RevTree tree = walk.parseTree(commit.getTree().getId());
//
//			CanonicalTreeParser treeParser = new CanonicalTreeParser();
//			try (ObjectReader reader = repository.newObjectReader()) {
//				treeParser.reset(reader, tree.getId());
//			}
//
//			walk.dispose();
//
//			return treeParser;
//		}
//	}
//
	//	private ArrayList<DiffInfo> checkoutedFileMaker(ArrayList<CommitInfo> input) throws ExecuteException, IOException {
	//		String workingPath = "/Users/yujin/ISEL/NewProject/BugPatchCollector/build/distributions/BugPatchCollector/bin/repositories/zookeeper/" ;
	//		ArrayList<DiffInfo> coFileList = new ArrayList<DiffInfo>() ;
	//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	//		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	//		Executor exec = new DefaultExecutor();
	//		
	//		exec.setStreamHandler(streamHandler);
	//		exec.setWorkingDirectory(new File(workingPath));
	//		
	//		String cmd ;
	//		int exitValue ;
	//		CommandLine cmdLine ;
	//		int[] exitValues = {0};
	//		exec.setExitValues(exitValues);
	//		for (CommitInfo commit : input) {
	//			DiffInfo checkoutedFile = new DiffInfo() ;
	//			
	//			checkoutedFile.filePath = workingPath + commit.fixPath ;
	//			checkoutedFile.prefixLine = commit.prefixLine ;
	//			
	//			cmd = "git checkout " + commit.fixID + " " + checkoutedFile.filePath;
	//			cmdLine = CommandLine.parse(cmd) ;
	//			exitValue = exec.execute(cmdLine) ;
	//			if (outputStream.toString().isEmpty()) System.out.println("git checkout: ") ;
	//			
	//			checkoutedFile.fixFile = new File(checkoutedFile.filePath) ;
	//			checkoutedFile.fixSource = getSource(checkoutedFile.fixFile) ;
	//			
	//			outputStream.reset() ;
	//
	//			cmd = "git log -2 --pretty=format:\"%h\" " + checkoutedFile.filePath ;
	//			cmdLine = CommandLine.parse(cmd) ;
	//			exitValue = exec.execute(cmdLine) ;
	//			String out = outputStream.toString() ;
	////			System.out.println("out: " + out) ;
	//			String[] output = out.split("\n") ;
	//			System.out.println("out1: " + output[0] + ", out2: " + output[1]) ;
	//			
	//			outputStream.reset() ;
	//			
	////			if (output.length != 2) continue ;
	//			
	//			
	//			
	////			String prefixCommitID = findPrefixCommitID(checkoutedFile.filePath, exec) ;
	//			
	////			CommandLine cmdLine = CommandLine.parse(cmd) ;
	////			int exit = exec.execute(cmdLine) ;
	////			
	////			checkoutedFile.BIFile = new File(workingPath + commit.BIFilePath) ;
	////			checkoutedFile.BISource = getSource(checkoutedFile.BIFile) ;
	////			checkoutedFile.BILine = commit.BILine ;
	////			
	////			cmd = "git checkout " + commit.FixCommitID + " ./" + commit.FixFilePath ;
	////			cmdLine = CommandLine.parse(cmd) ;
	////			exit = exec.execute(cmdLine) ;
	////			
	//
	////			checkoutedFile.FixLine = commit.FixLine ;
	////			
	//			coFileList.add(checkoutedFile) ;
	//		}
	//		outputStream.close() ;
	//		
	//		return coFileList ;
	//	}
	//	
	//	private String getSource (File file) throws IOException {
	//		StringBuilder builder = new StringBuilder(1000);
	//		BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
	//
	//		String source;
	//		char[] buffer = new char[1024];
	//		int num = 0;
	//
	//		while((num = reader.read(buffer)) != -1) {
	//			String readData = String.valueOf(buffer, 0, num);
	//			builder.append(readData);
	//		}
	//		reader.close();
	//
	//		source = builder.toString();
	//
	//		return source;
	//	}
}
