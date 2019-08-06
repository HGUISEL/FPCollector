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
			int FileNum = 0;
			for(String pathVariable : pathLineErrVar) {
				String path = pathVariable.split(",")[0];
				String var = pathVariable.split(",")[3].trim();
			
				File f = new File(path);
				FileReader fReader = new FileReader(f);
				BufferedReader bufReader = new BufferedReader(fReader);
				
				String line = "";
				String lineInfo = ",%%%%%";
				ArrayList<Integer> controlFlow = new ArrayList<>();
				ArrayList<Integer> dataDependency = new ArrayList<>();
				ArrayList<String> blocks = new ArrayList<>();
				ArrayList<String> blockPairs = new ArrayList<>();
				//ArrayList<Integer> endBlock = new ArrayList<>();
				
				int lineNum = 0;
				//for ArrayList.get(i), initiate as -1
				int blockNum = -1;
				int blockPairNum = 0;
				int controlNum =0 ;
				
				while((line = bufReader.readLine()) != null) {
					lineNum ++;
					if(!line.trim().startsWith("*") && 
							!line.trim().startsWith("/*") &&
							!line.trim().startsWith("//") &&
							(line.matches(".+\\bif\\b.+") || line.contains(".+\\belse\\b.+") 
							|| line.contains(".+\\bwhile\\b.+") || line.contains(".+\\bfor\\b.+")
							|| line.contains(".+\\bswitch\\b.+") || line.contains(".+\\btry\\b.+")
							|| line.contains(".+\\bcatch\\b.+"))) {
						controlFlow.add(lineNum);
						controlNum ++;
					}
					
					if(lineNum == 36) {
						System.out.println("a");
					}
					
					if(controlNum > 0 && line.matches(".+\\{.+\\}.+")&&
							!line.trim().substring(0, 1).contains("*") && 
							!line.trim().substring(0, 1).contains("/*") &&
							!line.trim().substring(0, 1).contains("//")) {
						String blockPair = "" + lineNum + "-" + lineNum;
						blockPairs.add(blockPairNum, blockPair);
						blockPairNum ++;
					} else if(true) 
						{if (controlNum > 0 &&
							line.contains("}") &&
							!line.trim().startsWith("*") && 
							!line.trim().startsWith("/*") &&
							!line.trim().startsWith("//")) {
								String blockPair = blocks.get(blockNum) + "-" + lineNum;
								blocks.remove(blockNum);
								blockPairs.add(blockPairNum, blockPair);
								blockNum --;
								controlNum --;
								blockPairNum ++;
					} if(controlNum > 0 &&
							line.contains("{") &&
							!line.trim().startsWith("*") && 
							!line.trim().startsWith("/*") &&
							!line.trim().startsWith("//")) {
								blockNum ++;
								blocks.add(""+lineNum);
						}
					}
					
					String lineMatches= ".+\\b"+var+"\\b.+";
					if(line.matches(lineMatches)) {
						lineInfo = lineInfo.concat("" + lineNum + ") " + line + "\n");
						dataDependency.add(lineNum);
					}
					
					System.out.println("line" + lineNum);
				}
				FileNum ++;
				System.out.println(FileNum);
				if(FileNum == 16)
					System.out.println(FileNum);
				
				bufReader.close();
				
//				for(int i = 0 ; i < lineNum; i ++) {
//					if(startBlock.get(i))
//				}
				//dpLines.add(pathVariable + lineInfo);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return dpLines;
	}
}
