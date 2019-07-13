package edu.handong.csee.isel.fpcollector.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ToolExecutor {
	final static int CURRENT = 0;
	
	public String runPMD(String toolCommand,String path, String rule, int flag) {
		String name = path.split(File.separator)[1];
		String reportPath = "";
		
		if(flag == CURRENT) {
			reportPath = name +"_Current.csv";
		}
		else {
			reportPath = name + "_Past.csv";
		}
		
		System.out.println("----- Running the Tool -----");
		String command = toolCommand
				+ " -d " + path
				+ " -R " + rule
				+ " -reportfile " + reportPath;
		try {
			Process pro = Runtime.getRuntime().exec(command);
			String line = "";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			while((line = in.readLine()) != null) {
				System.out.println(line);
			}
			
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while((line= in.readLine()) != null) {
				System.out.println(line);
			}
			
			pro.waitFor();
			//ExitVaule : 4 Network failure.
			//https://drupal.stackexchange.com/questions/82737/what-does-cron-error-exit-status-4-mean-in-syslog-ubuntu
			if(pro.exitValue() >= 1) {
				System.err.println("!!!!! " + pro.exitValue()+ " Run Failed");
			}
			else {
				System.out.println("@@@@@ Run Successfully");
			}
			
			pro.destroy();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return reportPath;
	}
}
