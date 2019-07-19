package edu.handong.csee.isel.fpcollector.contextextractor;

import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.reportanalysis.ReportAnalyzer;
import edu.handong.csee.isel.fpcollector.utils.Reader;

public class ContextExtractor {
	/*
	 * Input : A csv file, result of fpsuspectsgetter
	 * Output : A csv file, directory and line number and context information
	 */
	public void run(String OutputPath) {
		//read result file
		ArrayList<String> resultInfo = new ArrayList<>();
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		FPCollector collector = new FPCollector();
		String clonedPath = collector.getClonedPath();
		resultInfo = readResultFile(OutputPath);
		
		//get lines of code
			//using git blame
			lineInfo = getContextUsingBlame(resultInfo, clonedPath);
				//process build
				//execute git blame
				//split and store *11 times
			//using reader
				//file open
				//read lines
				//store 11 lines
		//write a file
			
	}
	
	public ArrayList<String> readResultFile(String path){
		Reader reader = new Reader();
		ArrayList<String> resultInfo = new ArrayList<>();
		
		resultInfo = reader.readResult(path);
		return resultInfo;	
	}
	
	public ArrayList<ArrayList<String>> getContextUsingBlame(ArrayList<String> result, String path){
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		lineInfo = analyzer.getCtxUsingBlame(result, path);
		
		return lineInfo;
	}
}
