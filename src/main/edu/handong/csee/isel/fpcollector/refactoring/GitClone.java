package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitClone {
	public String clonedPath ="";
	
	public void clone(String gitAddress, String projectName) {
		File newDir = new File("git");
		if(!newDir.exists()) {
			newDir.mkdir();
		}
		
		runClone(gitAddress, projectName, newDir);
	}
	
	private void runClone(String gitAddress, String projectName, File newDir) {
		try {
			File clonePath = new File(newDir.toString() + File.separator + projectName);
			
			clonedPath = clonePath.toString();
			
			if(!clonePath.exists()) {
				Process pro = 
						Runtime.getRuntime().exec("git clone " + gitAddress + " " + "git" + File.separator + projectName);
				
				String printedLine = null;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				while((printedLine = in.readLine()) != null) {
					System.out.println(printedLine);
				}
				
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while((printedLine = in.readLine()) != null) {
					System.out.println(printedLine);
				}
				
				pro.waitFor();
				
				if(pro.exitValue() >= 1) {
					System.err.println("Err(" + pro.exitValue() + ")"+ " : " + projectName + " CLONE FAILED ");
					System.exit(-1);
				}
				else {
					System.out.println("Cloned SUCCESSFULLY");
				}
				pro.destroy();
			}
			else {
				System.out.println("Skip : Git already cloned");
			}
		}
		catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
