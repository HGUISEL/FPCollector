package edu.handong.csee.isel.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.checkout.*;
import edu.handong.csee.isel.fpcollector.collector.*;
import edu.handong.csee.isel.fpcollector.init.*;
import edu.handong.csee.isel.fpcollector.patternfinder.*;
import edu.handong.csee.isel.fpcollector.utils.*;

public class FPCollector{
	/*
	 * 0 : project address
	 * 1 : rule context
	 * 2 : output path
	 * 
	 * project : project address
	 * clonedPath : after git clone, the path where it is cloned
	 * rule : Rule context which user want to test
	 * outputCurrentPath : csv file path where it has current result of tool
	 * outputpastPath : csv file path where it has past result of tool
	 * outputResultPath : csv file path where it has total result of this tool
	 */
	final static int CURRENT = 0;
	final static int PAST = 1;
	
	String project;
	String clonedPath;
	String rule;
	String outputCurrentPath;
	String outputPastPath;
	String outputResultPath;
	
	public static void main(String[] args) {
		FPCollector start = new FPCollector();
		start.run(args);
		
	}
	
	public void run(String[] args) {
		outputCurrentPath = initiate(args);
		outputPastPath = checkOut(rule);
		//collect(outputCurrentPath, outputPastPath, outputResultPath);
	}
	
	public String initiate(String[] list) {
		ArrayList<String[]> toolReport = new ArrayList<>();
		Reader read = new Reader();
		CloneMaker clone = new CloneMaker();
		ToolExecutor tool = new ToolExecutor();
		PatternFinder finder = new PatternFinder();
		String resultPath;
		
		project = read.readTextFile(list[0]);
		rule = read.readTextFile(list[1]);
		outputResultPath = read.readTextFile(list[2]);
		
		clonedPath = clone.gitClone(project);
		resultPath = tool.runPMD(clonedPath, rule, CURRENT);
		
		toolReport = read.readPmdReport(resultPath);
		finder.findPattern(toolReport);
		
		return resultPath;
	}
	
	public String checkOut(String rule) {
		CheckOutMaker checkOut = new CheckOutMaker();
		ToolExecutor tool = new ToolExecutor();
		String filePath;
		String resultPath;
		
		filePath = checkOut.gitCheckOut(clonedPath);
		resultPath = tool.runPMD(filePath, rule, PAST);
				
		return resultPath;
	}
	
	//method name -> verb
	//class name -> actor
//	public void collect(String currentPath, String pastPath, String resultPath) {
//		ResultCompartor compare = new ResultCompartor();
//		SuspectsExtractor suspects = new SuspectsExtractor();
//		ArrayList<String> suspectsList = new ArrayList<>();
//		
//		suspectsList = compare.compareResults(currentPath, pastPath);
//		suspects.writeFile(suspectsList);
//	}

}
