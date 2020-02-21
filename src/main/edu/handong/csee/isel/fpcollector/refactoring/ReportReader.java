package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class ReportReader {
	public ArrayList<SimpleEntry<String, String>> alarmedCodes = new ArrayList<>();
	
	public void readReport(String reportFilePath) {
		readReportFile(reportFilePath);
	}
	
	private void readReportFile(String path){
		File f = new File(path);
		
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			
			int lineNum = 0;
			
			while((str = fBufReader.readLine()) != null) {
				if(str.split(":").length > 2) {
					lineNum = Integer.parseInt(str.split(":")[1]);
					alarmedCodes.add(new SimpleEntry<String, String>(readLineOfCode(str, lineNum), str));
				}
			}
			fBufReader.close();
		} 
		catch (IOException e) {
				e.printStackTrace();
		}
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
