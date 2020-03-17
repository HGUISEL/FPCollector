package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.graph.ControlNode;
import edu.handong.csee.isel.fpcollector.graph.GraphBuilder;

public class InfoCollector {
	static final int VAR = 0;
	static final int FIELD = 1;
	static final int VARNODE = 2;
	static final int FIELDNODE = 3;
	static final int STRINGNODE = 4;

	public ArrayList<ControlNode> graphs = new ArrayList<>();
	
	public void run(String result_path, int phase) throws IOException {
		int firstInstance = 0;
		if(phase == 1) {
			firstInstance = 1;
		} else if(phase == 2) {
			firstInstance = 2000;
		} else if(phase == 3) {
			firstInstance = 4000;
		}
		
		int limit = phase * 2000;
		
		Reader outputFile = new FileReader(result_path);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);
		int count = 0 ;
		for (CSVRecord record : records) {			
			if(record.get(0).equals("File Path")) continue;
			count ++;
			
			if(count < firstInstance || count > limit) {
				continue;
			}
						
			System.out.println(count);
			if(count == 1163) {
				System.out.println("A");
			}
			Info info = new Info();			
		    info.path = record.get(0);
		    info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
		    info.start = Integer.parseInt(getScope(record.get(1), 0, info.sourceByLine));
		    info.end = Integer.parseInt(getScope(record.get(1), 1, info.sourceByLine));
		    //In case of String Literal
		    info.varNames.add(getVarNameByDoubleQuotation(record.get(2)));
		    //For String Literal
        	if(info.varNames.get(0) != null && info.varNodes.size() == 0) {
        		info.varNodes.addAll(getStringNode(info));
        	} 
		    //In case of DFA
        	else if(info.varNames.get(0) == null) {
		    	info.varNames.remove(0);
		    	info.varNames.add(getVarNameBySingleQuotation(record.get(2)));
		    	if(info.varNames.get(0) != null) {
		    		info.varNodes.addAll(getVarList(info));
		    		if(info.varNodes.size() == 0) {
		    			info.fieldNames.addAll(info.varNames);
		    			info.fieldNodes.addAll(getFieldList(info));
		    		}
		    	}
		    	else {
		    		//for other rules
		    		info.varNames.remove(0);
	        		info.varNodes.addAll(getVarList(info));
	            	info.fieldNodes.addAll(getFieldList(info));
	            	info.nodesToStrings();
		    	}
		    }
		    
        	if(info.varNodes.size() == 0 && info.fieldNodes.size() == 0) {
        		System.out.println("Something Goes Wrong");
        	}
        	
        	if(info.varNodes.size() >1) {
        		System.out.println("Something Goes Wrong");
        	}
		    
		    GraphBuilder graphBuilder = new GraphBuilder(info);
			graphBuilder.run();
			graphs.add(graphBuilder.root);
		}

//		File outputFile = new File(result_path);
//		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		
//		br.readLine();
//		String line = "";
//		int c = 0;
//        while ((line = br.readLine()) != null) {
//        	if(!line.startsWith("/")) continue;
//        	if(c == 419)
//        		System.out.println("a");
//        	Info info = getInfo(line);
//        	if (info != null) {
//        		c++;
////        		if (c % 10 == 0) {
//        			System.out.println(c);
////                    System.out.println("Heap Size(M) : " + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB");
////        		}
//        		
//    			GraphBuilder graphBuilder = new GraphBuilder(info);
//    			graphBuilder.run();
//    			graphs.add(graphBuilder.root);
//        	}
//        }
//        br.close();
		System.out.println("end");
	}	
	
//	private Info getInfo(String line) throws IOException {		
//    		String[] tokenList = line.split(",\"", 0);            	        		
//    		
//    		Info info = new Info();
//        	info.path = tokenList[0].split(",")[0];
//        	info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
//        	info.start = Integer.parseInt(getScope(tokenList[0].split(",")[1], 0, info.sourceByLine));
//        	info.end = Integer.parseInt(getScope(tokenList[0].split(",")[1], 1, info.sourceByLine));
//        	info.varNames.add(getVarNameByDoubleQuotation(tokenList[1]));
//        	if(info.varNames.get(0) == null) {
//        		info.varNames.remove(0);
//        		info.varNodes.addAll(getVarList(info));
//            	info.fieldNodes.addAll(getFieldList(info));
//            	info.nodesToStrings();
//        	}
//        	if(info.varNodes.size() == 0) {
//        		info.varNodes.addAll(getStringNode(info));
//        	}
//        	if(info.varNodes.size() == 0) {
//        		System.out.println("Something Goes Wrong");
//        	}
//        	return info;                         
//	}
	
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
	
	private String getVarNameByDoubleQuotation(String token) {		
		int start = 0;
		int end = 0;
		int flag = 0;
		for(int i = 0 ; i < token.length(); i ++) {
			if(flag == 0 && i< token.length()-1 && token.charAt(i)=='"') {
				if(token.charAt(i-1) != '\\') {					
					start = i+1;
					flag = 1;
				}
			} else if(flag == 1 && i< token.length()-1 && token.charAt(i)=='"') {
				if(token.charAt(i-1) != '\\') {
					end = i;
					flag = 2;
				}
			}
		}
		
		if(start == 0 && end == 0) {
			return null;
		}
		
		String newToken =token.substring(start, end);	
		return newToken;
	}
	
	private String getVarNameBySingleQuotation(String token) {		
		String[] tokenList = token.split("'");
		if(tokenList.length == 1) {
			return null;
		}
		return tokenList[3];
	}
	
	private ArrayList<ASTNode> getStringNode(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), STRINGNODE);
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