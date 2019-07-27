package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ViolationControlFlowGetter {
	public ArrayList<String> getControlFlow(ArrayList<String> dirLineErrVarDataDependLine){
		ArrayList<String> abstractedContext = new ArrayList<>();
		
		try {
			
			for(String pathLine : dirLineErrVarDataDependLine) {
				String tempPath = pathLine.split(",")[0];
				String tempLine = pathLine.split(",")[1].trim();
			
				File f = new File(tempPath);
				FileReader fReader = new FileReader(f);
				BufferedReader bufReader = new BufferedReader(fReader);
				
				String line = "";
				String lineInfo = ",%%%%%";
				int lineNum = 0;
				
				while((line = bufReader.readLine()) != null) {
					lineNum ++;
					if(line.contains(var)) {
						lineInfo = lineInfo.concat("" + lineNum + ") " +line + "\n");
					}
				}
				bufReader.close();
				dpLines.add(pathVariable + lineInfo);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return abstractedContext;
	}
}
