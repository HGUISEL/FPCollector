package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunTool {
	final static int FAILED = 0;
	final static int SUCCESS = 1;
	final static int CLONE = 2;
	final static int CHECKOUT = 3;
	
	public String reportPath = "";
	
	public void getReport(int flag, String toolCommand,String clonedPath, String rule, String projectName) {
		
		if(flag == CLONE) {
			reportPath = projectName + "_C.csv";
		}
		else if(flag == CHECKOUT) {
			reportPath = projectName + "_P.csv";
		}
		
		if(checkReportExist(reportPath)) {
			System.out.println("The Report is aleady exists.");
		} else {
			runPMD(toolCommand, clonedPath, rule);
		}
		
	}
	
	private boolean checkReportExist(String reportPath) {
		
		File checker = new File(reportPath);
		return checker.exists();
		
	}
	
	private void runPMD(String toolCommand, String clonedPath, String rule) {
		
		String command = toolCommand
						+ " -d " + clonedPath
						+ " -R " + rule
						+ " -reportfile " + reportPath;
		
		try {
			
			Process pro = Runtime.getRuntime().exec(command);
			String printedLine = "";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			while((printedLine = in.readLine()) != null) {
				System.out.println(printedLine);
			}
		
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while((printedLine= in.readLine()) != null) {
				System.out.println(printedLine);
			}
		
			pro.waitFor();
			
			//ExitVaule : 4 Network failure.
			//https://drupal.stackexchange.com/questions/82737/what-does-cron-error-exit-status-4-mean-in-syslog-ubuntu
			if(pro.exitValue() >= 1) {
				System.err.println("Err(" + pro.exitValue() +")"+ ": RUN FAILED");
			}
			else {
				System.out.println("Get Report SUCCESSFULLY");
			}
		
			pro.destroy();
		
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
