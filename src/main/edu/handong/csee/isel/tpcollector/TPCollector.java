package edu.handong.csee.isel.tpcollector;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fpcollector.utils.Reader;
import edu.handong.csee.isel.fpcollector.utils.ReportAnalyzer;
import edu.handong.csee.isel.fpcollector.utils.Writer;

public class TPCollector {
	public void run(String[] info) {
		ArrayList<String> currentReportInfo = new ArrayList<>();
		ArrayList<String> pastReportInfo = new ArrayList<>();

		HashMap<String, String> currentBlameInfo = 	new HashMap<>();
		HashMap<String, String> pastBlameInfo = new HashMap<>();
		HashMap<String, String> suspects = new HashMap<>();
		
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
	
		//get git blame/annotate and line info
		currentBlameInfo = getViolationLine(currentReportInfo);
				
		pastBlameInfo = getViolationLine(pastReportInfo);
		
		//get suspects
		
		suspects = getTruePositive(currentBlameInfo, pastBlameInfo);
		System.out.println("\nStart to write Result...\n");
		writeOutput(suspects, outputResultPath);
		System.out.println("-------------------------------------");
		System.out.println("@@@@@ Result File is created@@@@@");
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
