package edu.handong.csee.isel.fpcollector.fpsuspectsgetter;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.checkout.*;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.clone.*;
import edu.handong.csee.isel.fpcollector.utils.*;
/**
 * 
 * Collect False Positive Suspects.
 * 
 * <p> 	Process<br>
 * <pre>
 * 1. Clone the Target Project, and run static analysis tool.
 * 2. Read the analysis report to get analyzed information.
 * 3. Checkout the Target Project to a year ago of the input time.
 * 4. Run same tool to checkout project and get report informations.
 * 5. By using the report informations, get violated lines in each current and past project.
 * 6. Compare the current project's violated lines and the past project's violated lines
 * 7. Get suspect lines which are same line in the current project and past project
 * 8. Write Result file on output path which contains violate path, line number, error message
 * </pre>
 * <p>Input	<br>
 * <pre>
 * 1)Info[0] : project address
 * 2)Info[1] : rule context
 * 3)Info[2] : output path
 * 4)Info[3] : tool command
 * 5)Info[4] : Time
 * </pre>
 * <p>Output<br>
 * <pre>
 * 1) Result.csv : This documentation include False Positive suspects' path, line number, and error message.
 * </pre>
 * 
 * @author yoonhochoi
 * @version 1.0
 * @since 1.0
 * @see edu.handong.csee.isel.fpcollector.utils.Reader
 * @see edu.handong.csee.isel.fpcollector.utils.ToolExecutor
 * @see edu.handong.csee.isel.fpcollector.utils.Writer
 * @see edu.handong.csee.isel.fpcollector.utils.ReportAnalyzer
 * @see edu.handong.csee.isel.fpcollector.fpsuspectsgetter.clone.CloneMaker
 * @see edu.handong.csee.isel.fpcollector.fpsuspectsgetter.checkout.CheckOutMaker
 *
 */

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
		String[] info = new String[5];
		info[0] = init(args[0]);
		info[1] = init(args[1]);
		info[2] = init(args[2]);
		info[3] = init(args[3]);
		info[4] = init(args[4]);
		
		return info;
	}
	
	public void run(String[] info) {
		ArrayList<String> currentReportInfo = new ArrayList<>();
		ArrayList<String> pastReportInfo = new ArrayList<>();

		HashMap<String, String> currentViolatedLine = 	new HashMap<>();
		HashMap<String, String> pastViolatedLine = new HashMap<>();
		HashMap<String, String> suspects = new HashMap<>();
		
		String project = "";
		String clonedPath = "";
		String rule = "";
		String outputResultPath = "";
		String toolCommand = "";
		String checkoutPath = "";
		String currentReportPath = "";
		String pastReportPath = "";
		String inputTime = "";
		
		project = info[0];
		rule = info[1];
		outputResultPath = info[2];
		toolCommand = info[3];
		inputTime = info[4];
		
		//clone the target project
		clonedPath = clone(project);
		//applying pmd to the target project
		currentReportPath = executeTool(toolCommand, clonedPath, rule, CURRENT);
		//get violation informations(Directory, line number)
		currentReportInfo = getInfo(currentReportPath);
		
		//check out the target project to a year ago
		checkoutPath = checkOut(clonedPath, inputTime);
		//applying pmd to the target project
		pastReportPath = executeTool(toolCommand, checkoutPath, rule, PAST);
		//get violation informations(Directory, line number)
		pastReportInfo = getInfo(pastReportPath);
	
		//get git blame/annotate and line info
		currentViolatedLine = getViolationLine(currentReportInfo);
				
		pastViolatedLine = getViolationLine(pastReportInfo);
		
		//get suspects
		
		suspects = getSuspects(currentViolatedLine, pastViolatedLine);
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
	
	public String checkOut(String path, String time) {
		CheckOutMaker checkOut = new CheckOutMaker();
		String checkoutPath;
		
		checkoutPath = checkOut.gitCheckOut(path, time);
		
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
	getViolationLine(ArrayList<String> reportInfo){
		HashMap<String, String> blameInfo =
				new HashMap<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		blameInfo = analyzer.getViolation(reportInfo);
		
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
		writer.writeComparisonResult(suspects, path);
	}
}
