package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class TFPCWriter {
	public String fileName;
	
	public void writeContexts (ArrayList<SimpleEntry<String, String>> FTPC, String type) {
		fileName = "./" + type + "Candidate.csv";/* ./Result.csv */
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("File Path", "Line number", "Error Message","Code Context"));
			) {			int count = 0;
			for(SimpleEntry<String, String> ftpcPair : FTPC) {
				String path = ftpcPair.getValue();
//				if(count == 215) {
//					System.out.println("h");
//				}
				if(path.split(":").length > 2 ) {
					String filePath = path.split(":",3)[0];
					String lineNumber = path.split(":",3)[1];
					String errmsg = path.split(":",3)[2];
					String contexts = getLineContext(filePath, lineNumber);
					csvPrinter.printRecord(filePath, lineNumber, errmsg, contexts);
				}
				count++;
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
//	public void writeContextsForDFA (ArrayList<SimpleEntry<String, String>> FTPC, String type) {
//		fileName = "./" + type + "Candidate.csv";/* ./Result.csv */
//		try(
//			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
//			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
//									.withHeader("File Path", "Line number", "Anomaly", "Variable", "Code Context"));
//			) {
//			for(SimpleEntry<String, String> ftpcPair : FTPC) {
//				String path = ftpcPair.getValue();
//				
//				String filePath = path.split(":")[0];
////				String lineNumber = reportInfo.split(":")[1];
//				String errmsg = path.split(":")[2];
//				String anomaly = errmsg.substring(8, 10);
//				String var = errmsg.split("-anomaly for")[1];
//				String lineRange = var.split("lines")[1];
//				if(!lineRange.matches(".+'.+'-'.+'.+")) continue;
//				String startLine = getRangeLineNum(lineRange)[0];
//				String endLine = getRangeLineNum(lineRange)[1];
//				lineRange = startLine + "-" + endLine;
//				String contexts = getLineContextForDFA(filePath, startLine, endLine);
//				csvPrinter.printRecord(filePath, lineRange, anomaly, var, contexts);
//			}
//			writer.flush();
//			writer.close();
//		}catch(IOException e){
//			e.printStackTrace();
//		} 
//	}
	
	private String[] getRangeLineNum(String lineRange) {
		char temp;
		String[] lineNum = new String[2];
		int startToReadStart = 0;
		int startToReadEnd = 0;
		
		for(int i = 0 ; i < lineRange.length(); i ++) {
			temp = lineRange.charAt(i);
			if(temp == '\'' && startToReadStart != 1) {
				startToReadStart = 1;
				continue;
			}
			if(temp == '\'' && startToReadStart == 1 && startToReadEnd == 1) {
				break;
			}
			if(temp == '\'' && startToReadStart == 1) {
				startToReadStart = 0;
				startToReadEnd = 1;
				continue;
			}
			if(startToReadStart == 1 && startToReadEnd != 1 && temp != ',') {
				if(lineNum[0] == null) lineNum[0] = "" + temp;
				else lineNum[0] = lineNum[0] + temp;
				continue;
			}
			if(startToReadStart == 1 && startToReadEnd == 1 && temp != ',') {
				if(lineNum[1] == null) lineNum[1] = "" + temp;
				else lineNum[1] = lineNum[1] + temp;
				continue;
			}
		}
		if(lineNum[0].contains("(") ||lineNum[1].contains("(")) {
			System.out.println("hi");
		}
		return lineNum;
	}
	
	private String getLineContextForDFA(String filePath, String startLine, String endLine) {
		String code = "";
		String context = "";
		int startLineNum = Integer.parseInt(startLine);
		int endLineNum = Integer.parseInt(endLine);
		try {
			File f = new File(filePath);
			FileReader fReader = new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			int lineNumber = 1;
			while((code = fBufReader.readLine()) != null) {
				if(lineNumber >= startLineNum && lineNumber <= endLineNum) { 
					context = context + "\n" + lineNumber + ":\t" +code;
				}
					lineNumber++;
				}
				fBufReader.close();	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return context;
	}
	
	private String getLineContext(String filePath, String lineNum) {
		String code = "";
		String context = "";
		int lineNumber = Integer.parseInt(lineNum);
		
		try {
			File f = new File(filePath);
			FileReader fReader = new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			int currentLineNumber = 1;
			while((code = fBufReader.readLine()) != null) {
				if(lineNumber == currentLineNumber) { 
					context = context + "\n" + lineNumber + ":\t" +code;
				}
					currentLineNumber++;
				}
				fBufReader.close();	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return context;
	}
	
}
