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
			String atLine = separate[1];
			info.add(new SimpleEntry<String, String>(separate[0], atLine));
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("%%%%% " + exitvalue + " Collected All Information About Violation Lines Successfully");
		
		return blameInfo;
	}
}
