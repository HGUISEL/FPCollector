package edu.handong.csee.isel.fpcollector.fpsuspectsgetter.checkout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
//applying current time
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

public class CheckOutMaker {
	public String gitCheckOut(String clonedPath, String time) {
		String separator = "/";
		
			String name = clonedPath.split(separator)[1];
			String checkOutPath = "git" + File.separator + name + "_Past";
			
			File clonedDir = new File(clonedPath);
			
			if(!clonedDir.exists()) {
				System.out.println("!!!!! File Doesn't Cloned Yet");
				System.exit(-1);
			}
			
			File checkoutDir = new File(checkOutPath);
			
			try {
				FileUtils.copyDirectory(clonedDir, checkoutDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		//git check out
			/*
			 *  1) get in to a cloned directory which has a same name of original project
			 *  2) git checkout to 1 year ago
			 *  3) get directory path which indicate check out directory's location
			 */
			
			int yearAgo;
			//applying current time
//			String[] currentDate;
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//			LocalDate localDate = LocalDate.now();
//			currentDate = dtf.format(localDate).toString().split("-");
//			yearAgo = Integer.parseInt(currentDate[0]) - 1;
			
			//applying input time
			String[] currentDate = time.split("-");
			yearAgo = Integer.parseInt(currentDate[0]) - 1;
			
			String command = "git rev-list -1 --before=\"" + yearAgo + "-"
					+ currentDate[1] + "-" + currentDate[2] + "\" HEAD";
		
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
				Executor exec = new DefaultExecutor();
				exec.setStreamHandler(streamHandler);
				exec.setWorkingDirectory(new File(checkOutPath));

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

				System.out.println("### GIT CHECKOUT: (" + exitvalue + ")" + name + " " + gitCO);
				outputStream.close();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		
		//return checkout project path
		return checkOutPath;
	}
}
