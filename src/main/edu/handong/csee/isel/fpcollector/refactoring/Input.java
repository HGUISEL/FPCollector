package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Input {
	
	final static int SUCCESS = 1;
	final static int FAILED = 0;
	
	public String gitAddress = "" ;
	public String rule = "" ;
	public String outputPath = "" ;
	public String toolCommand = "" ;
	public String time = "" ;
	public String projectName = "" ;
	
	public int getInput(String[] args) {
		
		if(args.length < 5) {
			return FAILED;
		}
		
		gitAddress = readFiles(args[0]);
		rule = readFiles(args[1]);
		outputPath = readFiles(args[2]);
		toolCommand = readFiles(args[3]);
		time = readFiles(args[4]);
		projectName = getName(readFiles(args[0]));
		
		if(gitAddress.equals("WRONG") || rule.equals("WRONG") ||
				outputPath.equals("WRONG") || toolCommand.equals("WRONG") ||
				time.equals("WRONG"))
			return FAILED;
		
		return SUCCESS;
		
	}
	
	public String readFiles(String path) {
		File f = new File(path);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			
			while((str = fBufReader.readLine()) != null) {
				fBufReader.close();
				return str;
			}
			fBufReader.close();
		} 
		catch (IOException e) {
				e.printStackTrace();
		}
		return "WRONG";
	}
	
	private String getName(String address) {
		String projectName = address;
		String[] splited = projectName.split("/");
		String fileName = splited[splited.length -1];
		String name = fileName.replaceFirst(".git", "");
		
		return name;
	}
	
}
