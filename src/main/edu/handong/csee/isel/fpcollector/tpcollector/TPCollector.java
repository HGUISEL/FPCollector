package edu.handong.csee.isel.fpcollector.tpcollector;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fpcollector.utils.Reader;
import edu.handong.csee.isel.fpcollector.utils.ReportAnalyzer;
import edu.handong.csee.isel.fpcollector.utils.Writer;
/**
 * 
 * Collect True Positive for reducing common pattern of result.
 * 
 * <p> Process <br>
 * <pre>
 * 1. Get Project Name by using Input number [1]
 * 2. Read the analysis report to get analyzed information.
 * 3. By using the report informations, get violated lines in each current and past project.
 * 4. Compare the current project's violated lines and the past project's violated lines.
 * 5. Get True Positive lines which are in the past project but not in past project.
 * 6. Write Result file on output path which contains violate path, line number, error message.
 * </pre>
 * <p> Input File<br>
 * 1) Path of a text file which include a Target Project's git address(for getting project name)<br>
 * 2) Path of a text file which include Output file path<br>
 * <br>
 * For example,<br>
 * <pre> 1) https://github.com/spring-projects/dubbo.git
 * 2) ./Result.csv
 * </pre>
 * 
 * <p>Output<br>
 * <pre>
 * 1) Result_TP.csv : This documentation include TP's path, line number, and error message.
 * </pre>
 * 
 * @author yoonhochoi
 * @version 1.0
 * @since 1.0
 * @see edu.handong.csee.isel.fpcollector.utils.Reader
 * @see edu.handong.csee.isel.fpcollector.utils.Writer
 * @see edu.handong.csee.isel.fpcollector.utils.ReportAnalyzer
 *
 */
public class TPCollector {
	public void run(String[] info) {
		ArrayList<String> currentReportInfo = new ArrayList<>();
		ArrayList<String> pastReportInfo = new ArrayList<>();

		HashMap<String, String> currentLineInfo = 	new HashMap<>();
		HashMap<String, String> pastLineInfo = new HashMap<>();
		HashMap<String, String> truePositives = new HashMap<>();
		
		String projectName = "";
		String outputResultPath = "";
		String currentReportPath = "";
		String pastReportPath = "";
		
		projectName = info[0].split("/")[info[0].split("/").length -1].split("\\.")[0];
		outputResultPath = info[2].split("\\.csv")[0] + "_TP.csv";
		currentReportPath = projectName + "_Current.csv";
		pastReportPath = projectName + "_Past.csv";
		
		//get violation informations(Directory, line number, error message)
		currentReportInfo = getInfo(currentReportPath);
		
		//get violation informations(Directory, line number, error message)
		pastReportInfo = getInfo(pastReportPath);
	
		//get line info by read file
		currentLineInfo = getViolationLine(currentReportInfo);
				
		pastLineInfo = getViolationLine(pastReportInfo);
		
		//get True Positive
		
		truePositives = getTruePositive(currentLineInfo, pastLineInfo);
		System.out.println("\nStart to write Result...\n");
		writeOutput(truePositives, outputResultPath);
		System.out.println("done");
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
	getTruePositive(HashMap<String, String> current,
			HashMap<String, String> past){
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		HashMap<String, String> truePositive = new HashMap<>();
		
		truePositive = analyzer.truePositiveFinder(current, past);
		
		return truePositive;
	}
	
	public void writeOutput(HashMap<String, String> truePositive, String path) {
		Writer writer = new Writer();
		writer.writeComparisonResult(truePositive, path);
	}
}
