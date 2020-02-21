package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.graph.JavaASTParser;

public class InfoCollector {
	static final int VAR = 0;
	static final int FIELD = 1;
	static final int VARNODE = 2;
	static final int FIELDNODE = 3;
	static final int STRINGNODE = 4;
	
	ArrayList<Info> outputInfo = new ArrayList<>();
	
	public ArrayList<Info> run(String result_path) throws IOException {
		File outputFile = new File(result_path);
		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		
		br.readLine();
		String line = "";
		int count = 0;
		int DFA = 0;
		int DL =0;
		
        while ((line = br.readLine()) != null) {
        	       
        	if (line.startsWith("/")) {
        		String[] tokenList = line.split(",", -1);            	        		
        		count ++;
        		Info info = new Info();
            	info.path = tokenList[0];
            	info.source = getSource(tokenList[0]);
            	info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(info.source)));
            	info.start = Integer.parseInt(getScope(tokenList[1], 0, info.sourceByLine));
            	info.end = Integer.parseInt(getScope(tokenList[1], 1, info.sourceByLine));
            	if(count >= 420) 
             		System.out.println(count);
            	
            	info.varNames.add(getVarNameDFA(tokenList[3]));
            	if(info.varNames.get(0) == null) {
            		info.varNames.remove(0);
            		info.varNames.add(getVarName(tokenList[2]));
            	}
            	else DFA = 1;
            	
            	if(info.varNames.get(0) == null || info.varNames.size() == 0) {            		
            		info.varNodes.addAll(getVarList(info));
                	info.fieldNodes.addAll(getFieldList(info));
                	info.nodesToStrings();
            	} else {
            		if(DFA == 1) {
	            		info.varNodes.addAll(getVarNode(info));
	            		info.fieldNodes.addAll(getFieldNode(info));
            		} else {
            			info.varNodes.addAll(getStringNode(info));
            		}
            	}
            	
            	if(info.varNodes.size() == 0) {
            		System.exit(0);
            	}
            	
            	outputInfo.add(info);                       
        	}        
        }
        br.close();
		return outputInfo;
	}
	
	private ArrayList<ASTNode> getStringNode(Info info){
		JavaASTParser tempParser = new JavaASTParser(info);
		return tempParser.getViolatedVariableList(info.source, STRINGNODE);
	}
	
	private ArrayList<ASTNode> getVarNode(Info info){
		JavaASTParser tempParser = new JavaASTParser(info);
		return tempParser.getViolatedVariableList(info.source, VARNODE);
	}
	
	private ArrayList<ASTNode> getFieldNode(Info info){		
		JavaASTParser tempParser = new JavaASTParser(info);
		return tempParser.getViolatedVariableList(info.source, FIELDNODE);		
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
		String newToken = token.replace("\"\"", "$");
		token = token.replace("\"\"", "\"");
		int firstIdx = newToken.indexOf("$");
		int lastIdx = newToken.lastIndexOf("$");
		newToken = token.substring(firstIdx+1, lastIdx);
		
//		if(tokenList.length == 1) {
//			return null;
//		}
		return newToken;
	}
	
	private String getVarNameDFA(String token) {		
		String[] tokenList = token.split("'");
		if(tokenList.length == 1) {
			return null;
		}
		return tokenList[1];
	}
	
	private ArrayList<ASTNode> getVarList(Info info){
		JavaASTParser tempParser = new JavaASTParser(info);
		return tempParser.getViolatedVariableList(info.source, VAR);
	}
	
	private ArrayList<ASTNode> getFieldList(Info info){
		JavaASTParser tempParser = new JavaASTParser(info);
		return tempParser.getViolatedVariableList(info.source, FIELD);
	}
}

//package edu.handong.csee.isel.fpcollector.refactoring;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class InfoCollector {
//	static final int VAR = 0;
//	static final int FIELD = 1;
//	ArrayList<Info> outputInfo = new ArrayList<>();
//	
//	public ArrayList<Info> run(String result_path) throws IOException {
//		File outputFile = new File(result_path);
//		BufferedReader br = new BufferedReader(new FileReader(outputFile));
//		
//		br.readLine();
//		String line = "";
////		int count = 0;
//        while ((line = br.readLine()) != null) {
////        	count ++;
////        	if(count == 239)        
//        	if (line.startsWith("/")) {
//        		String[] tokenList = line.split(",", -1);
//            	Info info = new Info();
//            	info.path = tokenList[0];            	
//            	info.source = getSource(tokenList[0]);
//            	info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(info.source)));
//            	info.start = Integer.parseInt(getScope(tokenList[1], 0, info.sourceByLine));
//            	info.end = Integer.parseInt(getScope(tokenList[1], 1, info.sourceByLine));
//            	info.varName = getVarName(tokenList[3]);
//            	outputInfo.add(info);                       
//        	}        
//        }
//        br.close();
//		return outputInfo;
//	}
//	
//	private String getSource(String file_path) throws IOException {
//		File f = new File(file_path);
//		BufferedReader br = new BufferedReader(new FileReader(f));
//		StringBuilder builder = new StringBuilder(1000);
//	
//		char[] buf = new char[1024];
//		String source;
//		int num = 0;
//		while((num = br.read(buf)) != -1) {
//			String readData = String.valueOf(buf, 0, num);
//			builder.append(readData);
//		}
//		source = builder.toString();
//		br.close();
//		
//		return source;
//	}
//	
//	private String[] getSourceByLine(String source) {
//		return source.split("\n");
//	}
//	
//	private String getScope(String scope, int op, ArrayList<String> sourceByLine) {
//		String[] scopeList = scope.split("-");
//		if(scopeList.length == 1) {
//			return getOneLineScope(Integer.parseInt(scopeList[0]), op, sourceByLine);
////			return scopeList[0];
//		}
//		return scopeList[op];
//	}
//	
//	private String getOneLineScope(int scope, int op, ArrayList<String> sourceByLine) {
//		if (op == 0) {
//			int i = scope - 2;
//			while(!sourceByLine.get(i).contains(";")
//					&& !sourceByLine.get(i).contains("//")
//					&& !sourceByLine.get(i).contains("{")
//					&& !sourceByLine.get(i).contains("}")) {
//				i--;
//			}
//			return (i + 2) + "";
//		} else {
//			int i = scope - 1;
//			while(!sourceByLine.get(i).contains(";")
//					&& !sourceByLine.get(i).contains("//")
//					&& !sourceByLine.get(i).contains("{")
//					&& !sourceByLine.get(i).contains("}")) {
//				i++;
//			}
//			return (i + 1) + "";
//		}
//	}
//	
//	private String getVarName(String token) {
//		String[] tokenList = token.split("'");
//		if(tokenList.length == 1) {
//			return null;
//		}
//		return tokenList[1];
//	}
//}