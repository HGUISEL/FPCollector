package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class ReportReader {
	public ArrayList<String> reportInformation;
	public ArrayList<SimpleEntry<String, String>> alarmedCodes = new ArrayList<>();
	
	public void readReport(String reportFilePath) {
		
		reportInformation = readReportFile(reportFilePath);
		alarmedCodes = readCodes(reportInformation);
	}
	
	private ArrayList<String> readReportFile(String path){
		ArrayList<String> resultInfo = new ArrayList<>();
		File f = new File(path);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			
			while((str = fBufReader.readLine()) != null) {
				resultInfo.add(str);
			}
			fBufReader.close();
		} 
		catch (IOException e) {
				e.printStackTrace();
		}
		return resultInfo;
	}
	
	private ArrayList<SimpleEntry<String, String>> readCodes(ArrayList<String> reportInformation){
		ArrayList<SimpleEntry<String, String>> alarmedCodes = new ArrayList<>();
		int lineNum = 0;
		
		for(String reportInstance : reportInformation) {
			if(reportInstance.split(":").length > 2) {
				lineNum = Integer.parseInt(reportInstance.split(":")[1]);
				alarmedCodes.add(new SimpleEntry<String, String>(readLineOfCode(reportInstance, lineNum), reportInstance));
			}
		}
		return alarmedCodes;
	}
	
	private String readLineOfCode(String reportInstance, int lineNum) {
		String dir = "";
		String code = "";
		
		if(reportInstance.split(":").length > 2) {
			dir = reportInstance.split(":")[0];
			code = "";
			try {
				File f = new File(dir);
				FileReader fReader = new FileReader(f);
				BufferedReader fBufReader = new BufferedReader(fReader);
				int lineNumber = 1;
				while((code = fBufReader.readLine()) != null) {
					if(lineNumber == lineNum) break;
					lineNumber++;
				}
				fBufReader.close();	
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} else {
			code = "null";
		}
		
		return code.trim();
	}
	
}
