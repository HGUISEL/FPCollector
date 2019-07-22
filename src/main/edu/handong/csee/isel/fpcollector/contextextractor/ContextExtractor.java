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
		
		//get lines of code
			lineContext = getContextUsingBlame(resultInfo, projectName);
			//using reader
				//file open
				//read lines
				//store 11 lines
		//write a file
			System.out.println("\n----- Start to Rearrange Data -----\n");
			context = new DirLineErrmsgContext(lineContext, resultInfo);
			System.out.println("\n----- Start to Write a File -----\n");
			writeContext(context, OutputPath);
			System.out.println("\n----- All Process is Completed");
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
	
	public void writeContext(DirLineErrmsgContext context, String path) {
		Writer writer = new Writer();
		writer.writeContexts(context, path);
	}
}
