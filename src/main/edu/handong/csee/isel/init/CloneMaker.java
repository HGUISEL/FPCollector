package edu.handong.csee.isel.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CloneMaker {
	public String gitClone(String address) {
		String projectName = address;
		String[] splited = projectName.split("/");
		String fileName = splited[splited.length -1];
		String name = fileName.replaceFirst(".git", "");
		
		System.out.println("===== " + name + " is launched =====");
		
		File gitDir = new File("git");
		if(!gitDir.exists()) {
			gitDir.mkdir();
		}
		try {
			File clonePath = new File(gitDir + File.separator + name);
			
			if(!clonePath.exists()) {
				Process pro = Runtime.getRuntime().exec("git clone " + address + " " + "git" + File.separator + name);
				
				String line = null;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				while((line = in.readLine()) != null) {
					System.out.println(line);
				}
				
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while((line = in.readLine()) != null) {
					System.out.println(line);
				}
				
				pro.waitFor();
				
				if(pro.exitValue() >= 1) {
					System.err.println("!!!!!" + pro.exitValue() + " GIT CLONE FAILED " + name);
				}
				else {
					System.out.println("@@@@@ Cloned Successfully");
				}
				
				pro.destroy();
			}
			else {
				System.out.println("!!!!! Git already cloned");
			}
		}
		catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return "git" + File.separator + name;
	}
}
