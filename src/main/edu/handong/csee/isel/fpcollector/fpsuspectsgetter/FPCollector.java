package edu.handong.csee.isel.fpcollector.fpsuspectsgetter;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.checkout.*;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.clone.*;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.reportanalysis.*;
import edu.handong.csee.isel.fpcollector.utils.*;

public class FPCollector{
	/*
	 * 0 : project address
	 * 1 : rule context
	 * 2 : output path
	 * 3 : tool command
	 * project : project address
	 * clonedPath : after git clone, the path where it is cloned
	 * rule : Rule context which user want to test
	 * currentReportPath : csv file path where it has current result of tool
	 * pastReportPath : csv file path where it has past result of tool
	 * outputResultPath : csv file path where it has total result of this tool
	 */
	final static int CURRENT = 0;
	final static int PAST = 1;
	
	public String[] initiate(String[] args){
		String[] info = new String[4];
		info[0] = init(args[0]);
		info[1] = init(args[1]);
		info[2] = init(args[2]);
		info[3] = init(args[3]);
		
		return info;
	}
	
	public void run(String[] info) {
		//Should I use SimpleEntry?? It could be changed to just String
		ArrayList<String> currentReportInfo = new ArrayList<>();
		ArrayList<String> pastReportInfo = new ArrayList<>();

		HashMap<String, String> currentBlameInfo = 	new HashMap<>();
		HashMap<String, String> pastBlameInfo = new HashMap<>();
		HashMap<String, String> suspects = new HashMap<>();
		
		String project = "";
		String clonedPath = "";
		String rule = "";
		String outputResultPath = "";
		String toolCommand = "";
		String checkoutPath = "";
		String currentReportPath = "";
		String pastReportPath = "";
		
		project = info[0];
		rule = info[1];
		outputResultPath = info[2];
		toolCommand = info[3];
		
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
		
		suspects = getSuspects(currentBlameInfo, pastBlameInfo);
		System.out.println("\nStart to write Result...\n");
		writeOutput(suspects, outputResultPath);
		System.out.println("-------------------------------------");
		System.out.println("@@@@@ Result File is created@@@@@");
	}
	
	public String init(String file) {
		Reader read = new Reader();
		String contents;
		
		contents = read.readInputFiles(file);
		
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
	
	public ArrayList<String> getInfo(String path){
		Reader read = new Reader();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		ArrayList<String[]> readReport = new ArrayList<>();
		ArrayList<String> reportInfo = new ArrayList<>();
		
		readReport = read.readPmdReport(path);
		reportInfo = analyzer.getDirLineErrmsg(readReport); 
		
		return reportInfo;
		
	}
	
	public HashMap<String, String>
	blame(ArrayList<String> reportInfo, String path){
		HashMap<String, String> blameInfo =
				new HashMap<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		blameInfo = analyzer.gitBlame(reportInfo, path);
		
		return blameInfo;
	}
	
	public HashMap<String, String> 
	getSuspects(HashMap<String, String> current,
			HashMap<String, String> past){
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		HashMap<String, String> suspects = new HashMap<>();
		
		suspects = analyzer.suspectsFinder(current, past);
		
		return suspects;
	}
	
	public void writeOutput(HashMap<String, String> suspects, String path) {
		Writer writer = new Writer();
		writer.writeSuspects(suspects, path);
	}
}
