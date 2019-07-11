package edu.handong.csee.isel.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
	
	public String readTextFile(String path) {
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
		return null;
	}
	
	public ArrayList<String[]> readPmdReport(String path) {
		ArrayList<String[]> report = new ArrayList<>();
		String[] lineOfReport;
		
		File f = new File(path);
		try {
			FileReader fReader = new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			
			while((str = fBufReader.readLine()) != null) {
				lineOfReport = str.split("\t");
				report.add(lineOfReport);
			}
			fBufReader.close();
			return report;
		}
		catch(IOException e) {
				e.printStackTrace();
		}
		
		
		return null;
	}
	
}
