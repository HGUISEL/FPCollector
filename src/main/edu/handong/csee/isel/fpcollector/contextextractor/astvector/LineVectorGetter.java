package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ViolationControlFlowGetter {
	public ArrayList<String> getControlFlow(ArrayList<String> dirLineErrVarDataDependLine){
		ArrayList<String> controlFlowStatements = new ArrayList<>();
		
try {
			
			for(String pathVariable : dirLineErrVarDataDependLine) {
				String path = pathVariable.split(",")[0];
				String var = pathVariable.split(",")[3].trim();
				String dataDependLine = pathVariable.split(",%%%%%")[1].trim();
				
				String dependLines[] = dataDependLine.split("\n");
				String lineNumber = "";
				ArrayList<Integer> dependentLines = new ArrayList<>();
				
				for(String lines : dependLines) {
					lineNumber = lines.substring(0, 5).split(")")[0];
					dependentLines.add(Integer.parseInt(lineNumber));
				}
				
				String line = "";
				
				File f = new File(path);
				FileReader fReader = new FileReader(f);
				BufferedReader bufReader = new BufferedReader(fReader);
				
				int lineNum = 0;
				
				while((line = bufReader.readLine()) != null) {
				
				}
				
				
				bufReader.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return controlFlowStatements;
	}
}
