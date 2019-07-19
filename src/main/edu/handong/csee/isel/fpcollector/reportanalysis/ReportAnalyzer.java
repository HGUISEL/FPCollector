package edu.handong.csee.isel.fpcollector.reportanalysis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

public class ReportAnalyzer {
	
	public ArrayList<SimpleEntry<String, String>> getDirLine(ArrayList<String[]> report) {
		ArrayList<SimpleEntry<String, String>> info = new ArrayList<>();
		for(String[] temp : report) {			
			String[] separate = temp[0].split(":");
			String directoryPath = separate[0];
			String atLine = separate[1];
			info.add(new SimpleEntry<String, String>(directoryPath, atLine));
		}
		return info;
	}
	
	public HashMap<String, SimpleEntry<String, String>> gitBlame
	(ArrayList<SimpleEntry<String, String>> reportInfo, String path){
		HashMap<String, SimpleEntry<String, String>> blameInfo =
				new HashMap<>();
		int exitvalue = -1;
		//result : line contents of code, directory, line
				//compare just line info
				//when run git blame, it should be in root folder of cloned/checkout project
				//and form is "git blame dir -L linenum, linenum, -l"
				//then we can get line info as third one
		
		try {
			int size = reportInfo.size();
			int progress = 0;
			int quater = size/4;
			int half = size /2 ;
			int halfNQuater = size * 3 / 4;
			System.out.println("\n----- Start to Collect Line Information -----\n");
			for(SimpleEntry<String, String> temp : reportInfo) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
				Executor exec = new DefaultExecutor();
				exec.setStreamHandler(streamHandler);
				exec.setWorkingDirectory(new File(path));

				String tempDir = temp.getKey();
				String tempLine = temp.getValue();
				String command = "git blame " + tempDir + " -L " + tempLine+ "," +tempLine +" -l";
				CommandLine cl = CommandLine.parse(command);
				int[] exitValues = {0};
			
				exec.setExitValues(exitValues);
				exitvalue = exec.execute(cl);
				String blameResult = outputStream.toString();
				
				String lineInfo = blameResult.split("     *")[1].trim();

				blameInfo.put(lineInfo, temp);
				outputStream.flush();
				outputStream.close();
				progress ++;
				if(progress == quater) {
					System.out.println("@@@@@ 25% Complete");
				}
				else if(progress == half) {
					System.out.println("@@@@@ 50% Complete");
				}
				else if (progress == halfNQuater) {
					System.out.println("@@@@@ 75% Complete");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("@@@@@ " + exitvalue + " Collected All Information About Violation Lines Successfully");
		
		return blameInfo;
	}
	
	public HashMap<String, SimpleEntry<String, String>> 
	suspectsFinder(HashMap<String, SimpleEntry<String, String>> current,
			HashMap<String, SimpleEntry<String, String>> past){
		HashMap<String, SimpleEntry<String, String>> suspects = new HashMap<>();
		
		for(String currentLine : current.keySet()) {
			for(String pastLine : past.keySet()) {
				if(currentLine.equals(pastLine)) {
					suspects.put(currentLine, current.get(currentLine));
					past.remove(pastLine);
					//The number of equal value is at most 1. Therefore, break
					break;
				}
			}
		}
		
		return suspects;
	}
}
