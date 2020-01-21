package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InfoCollector {
	ArrayList<Info> outputInfo = new ArrayList<>();
	
	public ArrayList<Info> run(String result_path) throws IOException {
		File outputFile = new File(result_path);
		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		
		br.readLine();
		String line = "";
        while ((line = br.readLine()) != null) {
        	if (line.startsWith("/")) {
        		String[] tokenList = line.split(",", -1);
            	Info info = new Info();
            	
            	info.source = getSource(tokenList[0]);
            	info.start = getScope(tokenList[1], 0);
            	info.end = getScope(tokenList[1], 1);
            	info.varName.add(getVarName(tokenList[3]));
            	
            	outputInfo.add(info);
        	}
        }
        br.close();
		return outputInfo;
	}
	
	private String getSource(String file_path) throws IOException {
		File f = new File(file_path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder(1000);
	
		char[] buf = new char[1024];
		String source;
		int num = 0;
		while((num = br.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, num);
			builder.append(readData);
		}
		source = builder.toString();
		br.close();

		return source;
	}
	
	private String getScope(String scope, int op) {
		String[] scopeList = scope.split("-");
		if(scopeList.length == 1) {
			return scopeList[0];
		}
		return scopeList[op];
	}
	
	private String getVarName(String token) {
		String[] tokenList = token.split("'");
		if(tokenList.length == 1) {
			return null;
		}
		return tokenList[1];
	}

}
