package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

public class GitCheckout {
	public String checkoutPath = "";
	
	public void checkout(String clonedPath, String time, String projectName) {
		
		checkoutPath = "git" + File.separator + projectName + "_P";
		cloneDir(clonedPath, checkoutPath);
		checkoutGivenTime(time, projectName);
	}
	
	private void cloneDir(String clonedPath, String checkoutPath){
		
		File clonedDir = new File(clonedPath);
		
		if(!clonedDir.exists()) {
			System.out.println("Err : The Project Doesn't Cloned Yet.");
			System.exit(-1);
		}
		
		File checkoutDir = new File(checkoutPath);
		
		try {
			FileUtils.copyDirectory(clonedDir, checkoutDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkoutGivenTime(String givenTime, String projectName) {
		
		String[] pastTime = goBack(givenTime);
		
		String command = 
				"git rev-list -1 --before=\"" 
				+ pastTime[0] + "-"
				+ pastTime[1] + "-" 
				+ pastTime[2] + "\" HEAD";
	
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
			Executor exec = new DefaultExecutor();
			exec.setStreamHandler(streamHandler);
			exec.setWorkingDirectory(new File(checkoutPath));

			CommandLine cl = CommandLine.parse(command);
			int[] exitValues = {0};
			exec.setExitValues(exitValues);
			int exitvalue = exec.execute(cl);
			String dueCommitID = outputStream.toString();

			String gitCO = "git checkout " + dueCommitID +" -f";
			System.out.println("### " + gitCO);
			cl = CommandLine.parse(gitCO);
			exitvalue = exec.execute(cl);
			System.out.println(outputStream.toString());

			System.out.println("### GIT CHECKOUT: (" + exitvalue + ")" + projectName + " " + gitCO);
			outputStream.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String[] goBack(String givenTime){
		String[] aYearAgo = new String[3];
		
		int year;
		String[] givenDate = givenTime.split("-");
		year = Integer.parseInt(givenDate[0]) - 1;
		
		aYearAgo[0] = "" + year;
		aYearAgo[1] = givenDate[1];
		aYearAgo[2] = givenDate[2];
		
		return aYearAgo;
	}
	
}
