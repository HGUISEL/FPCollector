package edu.handong.csee.isel.fpcollector.checkout;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

public class CheckOutMaker {
	public String gitCheckOut(String clonedPath) {
		String separator = "/";
		
			String name = clonedPath.split(separator)[1];
			String checkOutPath = "git" + File.separator + name + "_Past";
			
			File clonedDir = new File(clonedPath);
			
			if(!clonedDir.exists()) {
				System.out.println("!!!!! File Doesn't Cloned Yet");
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
			
			String yearAgo = "";
			//get current time
			//make it a year ago
			//get commit id of year ago
			
			//git checkout
			try {
				Process pro = Runtime.getRuntime().exec("git rev-list -1 --before=\"" + yearAgo);
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
		
		//return checkout project path
		return checkOutPath;
	}
}
