package edu.handong.csee.isel.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ToolExecutor {
	public String runPMD(String path, String rule) {
		System.out.println("----- Running the Tool -----");
		String[] command = {"/Users/yoonhochoi/Documents/ISEL/pmd/pmd-bin-6.15.0/bin/run.sh", "pmd", "-d", path, "-R", rule, "-f", "csv"};
		//String command = "/Users/yoonhochoi/Documents/ISEL/pmd/pmd-bin-6.15.0/bin/run.sh pmd -d git/MaterialDesignLibrary -R category/java/errorprone.xml/AssignmentToNonFinalStatic -f csv >Current.csv";
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
			
			if(pro.exitValue() >= 1) {
				System.err.println("!!!!! " +pro.exitValue() + "Run Failed");
			}
			else {
				System.out.println("@@@@@ Run Successfully");
			}
			
			pro.destroy();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}
}
