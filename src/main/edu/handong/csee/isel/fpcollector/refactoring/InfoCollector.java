package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.graph.GraphBuilder;

public class InfoCollector {
	static final int VAR = 0;
	static final int FIELD = 1;
	public ArrayList<Info> infos = new ArrayList<>();
	
	public void run(String result_path) throws IOException {
		File outputFile = new File(result_path);
		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		
		br.readLine();
		String line = "";
		int c = 0;
        while ((line = br.readLine()) != null) {
        	if (line.startsWith("/")) {
        		c++;
            	if (c % 10 == 0) System.out.println(c);
            	
        		String[] tokenList = line.split(",", -1);            	        		
        		
        		Info info = new Info();
            	info.path = tokenList[0];
            	info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(tokenList[0]))));
            	info.start = Integer.parseInt(getScope(tokenList[1], 0, info.sourceByLine));
            	info.end = Integer.parseInt(getScope(tokenList[1], 1, info.sourceByLine));
            	info.varNames.add(getVarName(tokenList[3]));
            	if(info.varNames.get(0) == null) {
            		info.varNames.remove(0);
            		info.varNodes.addAll(getVarList(info));
                	info.fieldNodes.addAll(getFieldList(info));
                	info.nodesToStrings();
            	}
            	
            	infos.add(info);                       
        	}        
        }
        br.close();
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
	
	private String[] getSourceByLine(String source) {
		return source.split("\n");
	}
	
	private String getScope(String scope, int op, ArrayList<String> sourceByLine) {
		String[] scopeList = scope.split("-");
		if(scopeList.length == 1) {
			return getOneLineScope(Integer.parseInt(scopeList[0]), op, sourceByLine);
		}
		return scopeList[op];
	}
	
	private String getOneLineScope(int scope, int op, ArrayList<String> sourceByLine) {
		if (op == 0) {
			int i = scope - 2;
			while(!sourceByLine.get(i).contains(";")
					&& !sourceByLine.get(i).contains("//")
					&& !sourceByLine.get(i).contains("{")
					&& !sourceByLine.get(i).contains("}")
					) {
				i--;
			}
			return (i + 2) + "";
		} else {
			int i = scope - 1;
			while(!sourceByLine.get(i).contains(";")
					&& !sourceByLine.get(i).contains("//")
					&& !sourceByLine.get(i).contains("{")
					&& !sourceByLine.get(i).contains("}")) {
				i++;
			}
			return (i + 1) + "";
		}
	}
	
	private String getVarName(String token) {
		String[] tokenList = token.split("'");
		if(tokenList.length == 1) {
			return null;
		}
		return tokenList[1];
	}
	
	private ArrayList<ASTNode> getVarList(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), VAR);
	}
	
	private ArrayList<ASTNode> getFieldList(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), FIELD);
	}
}