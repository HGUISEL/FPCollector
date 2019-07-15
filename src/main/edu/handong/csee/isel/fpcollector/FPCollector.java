/*
 * Todo
 * 
 * 2-2) make compare blame result 7.16
 * 2-3) make get result
 * 3-1) make pattern extractor
 * 3-2) make pattern comparator
 * 3-3) make get result
 */
package edu.handong.csee.isel.fpcollector;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fpcollector.checkout.*;
import edu.handong.csee.isel.fpcollector.clone.*;
import edu.handong.csee.isel.fpcollector.collector.*;
import edu.handong.csee.isel.fpcollector.reportanalysis.*;
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

	public void run(String[] args) {
		ArrayList<SimpleEntry<String, String>> currentReportInfo = new ArrayList<>();
		ArrayList<SimpleEntry<String, String>> pastReportInfo = new ArrayList<>();

		HashMap<String, SimpleEntry<String, String>> currentBlameInfo =
				new HashMap<>();
		HashMap<String, SimpleEntry<String, String>> pastBlameInfo =
				new HashMap<>();
		
		String project = "";
		String rule = "";
		String outputResultPath = "";
		String toolCommand = "";
		String clonedPath = "";
		String checkoutPath = "";
		String currentReportPath = "";
		String pastReportPath = "";
		
		project = init(args[0]);
		rule = init(args[1]);
		outputResultPath = init(args[2]);
		toolCommand = init(args[3]);
		
		//clone the target project
		clonedPath = clone(project);
		//applying pmd to the target project
		currentReportPath = executeTool(toolCommand, clonedPath, rule, CURRENT);
		//get violation informations(Directory, line number)
		currentReportInfo = getInfo(currentReportPath);
		
		//check out the target project to a year ago
		checkoutPath = checkOut(clonedPath);
		//applying pmd to the target project
		pastReportPath = executeTool(toolCommand, checkoutPath, rule, PAST);
		//get violation informations(Directory, line number)
		pastReportInfo = getInfo(pastReportPath);
	
		//get git blame/annotate and line info
		currentBlameInfo = blame(currentReportInfo, clonedPath);
				
		pastBlameInfo = blame(pastReportInfo, checkoutPath);
		
		//get suspects
	
		
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		//Start Here, compare currentBlame Info and pastBlameInfo
		
		
		//2) get pattern of line info
		
		//compare pattern
		
		//get suspects
		
	}
	
	public String init(String file) {
		Reader read = new Reader();
		String contents;
		
		contents = read.readTextFile(file);
		
		return contents;
	}
	
	public String clone(String project) {
		CloneMaker clone = new CloneMaker();
		String clonedPath;
		
		clonedPath = clone.gitClone(project);
		
		return clonedPath;
	}
	
	public String checkOut(String path) {
		CheckOutMaker checkOut = new CheckOutMaker();
		String checkoutPath;
		
		checkoutPath = checkOut.gitCheckOut(path);
		
		return checkoutPath;
	}
	
	public String executeTool(String command,String path, String rule, int flag) {
		ToolExecutor tool = new ToolExecutor();
		String resultPath;
		
		if(flag == CURRENT) {
			resultPath = tool.runPMD(command, path , rule, CURRENT);
		} else {
			resultPath = tool.runPMD(command ,path ,  rule, PAST);
		}
		
		return resultPath;
	}
	
	public ArrayList<SimpleEntry<String, String>> getInfo(String path){
		Reader read = new Reader();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		ArrayList<String[]> readReport = new ArrayList<>();
		ArrayList<SimpleEntry<String, String>> reportInfo = new ArrayList<>();
		
		readReport = read.readPmdReport(path);
		reportInfo = analyzer.getDirLine(readReport); 
		
		return reportInfo;
		
	}
	
	public HashMap<String, SimpleEntry<String, String>>
	blame(ArrayList<SimpleEntry<String, String>> reportInfo, String path){
		HashMap<String, SimpleEntry<String, String>> blameInfo =
				new HashMap<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		blameInfo = analyzer.gitBlame(reportInfo, path);
		
		return blameInfo;
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
