package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataDependentLineGetter {
	public ArrayList<String> getDataDependentLines(ArrayList<String> pathLineErrVar){
		ArrayList<String> dpLines = new ArrayList<>();
		
		try {
			
			for(String pathVariable : pathLineErrVar) {
				String path = pathVariable.split(",")[0];
				String var = pathVariable.split(",")[3].trim();
			
				File f = new File(path);
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
		
		return dpLines;
	}
}
