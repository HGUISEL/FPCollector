package edu.handong.csee.isel.fpcollector.contextextractor;

import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.reportanalysis.ReportAnalyzer;
import edu.handong.csee.isel.fpcollector.structures.DirLineErrmsgContext;
import edu.handong.csee.isel.fpcollector.utils.Reader;
import edu.handong.csee.isel.fpcollector.utils.Writer;

public class ContextExtractor {
	/*
	 * Input : A csv file, result of fpsuspectsgetter
	 * Output : A csv file, directory and line number and context information
	 */
	public void run(String[] infos) {
		//read result file
		ArrayList<String> resultInfo = new ArrayList<>();
		ArrayList<ArrayList<String>> lineContext = new ArrayList<>();
		String OutputPath = infos[2];
		
		DirLineErrmsgContext context;
		String project = infos[0];
		String[] getName = project.split("/");
		String gitName = getName[getName.length - 1];
		String projectName = gitName.split("\\.")[0];
		resultInfo = readResultFile(OutputPath);
		
		//get lines of code Using File is far much faster
			//lineContext = getContextUsingBlame(resultInfo, projectName);
			lineContext = getContextUsingFile(resultInfo, projectName);
			
		//part 1 : get AST Vector
		//find violated variable and its path
		ArrayList<String> varPath = new ArrayList<>();
		varPath = getViolationVarPath(resultInfo);
		//get all line where violated variable is located
		ArrayList<String> dataDependentLines = new ArrayList<>();
		dataDependentLines = getDataDependentLines(varPath);
		//get control flow of lines
		ArrayList<String> relatedControlFlow = new ArrayList<>();
			/*
			 * It would works like below
			 * 1) find { or } in source code
			 * 		* if it is {
			 * 			a. check its line number is less than dataDependentLine number
			 * 				a-1. if it is less, store temporarily its line number 
			 * 					 and its line contents
			 * 				a-2. then find }
			 * 				a-3. if }'s line number is bigger than dataDependentLine number,
			 * 					 store its line number and its line contents
			 * 				a-4. if more { is found, store it and wait for }
			 * 				a-5. maybe, it would work by using stack
			 * 2) if the pair of {} is found, store them with dependent lines
			 * 3) sorting as its line number
			 * 4) then, all process is done with control flow and data dependency
			 */
		realtedControlFlow = getRealtedControlFlow(dataDependentLines);
		//Make AST
			//using library
		//get all nodes in vector
			//just change into vector
		
		//part 2 : get Frequent Node
		
		//part 3 : how to choose Nodes as it is common and important(range, kind or etc.)

		//write a file
			System.out.println("\n----- Start to Rearrange Data -----\n");
			context = new DirLineErrmsgContext(lineContext, resultInfo);
			writeContext(context, OutputPath);
			System.out.println("@@@@@ Context Extracting Process is Completed");
	}
	
	public ArrayList<String> readResultFile(String path){
		Reader reader = new Reader();
		ArrayList<String> resultInfo = new ArrayList<>();
		
		resultInfo = reader.readResult(path);
		return resultInfo;	
	}
	
	public ArrayList<ArrayList<String>> getContextUsingBlame(ArrayList<String> result, String name){
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		lineInfo = analyzer.getCtxUsingBlame(result, name);
		
		return lineInfo;
	}
	
	public ArrayList<ArrayList<String>> getContextUsingFile(ArrayList<String> result, String name){
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		lineInfo = analyzer.getCtxUsingFile(result, name);
		
		return lineInfo;
	}
	
	public void writeContext(DirLineErrmsgContext context, String path) {
		Writer writer = new Writer();
		writer.writeContextsForDFA(context, path);
	}
}
