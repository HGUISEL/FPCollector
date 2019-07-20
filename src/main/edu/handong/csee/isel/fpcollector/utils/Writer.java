package edu.handong.csee.isel.fpcollector.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class Writer {
	public void writeSuspects(HashMap<String, SimpleEntry<String, String>> suspects, String path) {
		File file = new File(path);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			for(Map.Entry<String, SimpleEntry<String, String>> entry: suspects.entrySet()) {
				writer.write(entry.getValue().getKey() + ", " +
						entry.getValue().getValue() + "\n");
			}
			
			writer.flush();
			writer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		finally{
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	//To do this, I should use my own Delimiter. So, tomorrow, I should check about it
	public void writeContexts
	(ArrayList<ArrayList<String>> context, ArrayList<String> result, String path) {
		String fileName = path.split("\\.")[0];
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName + "_w_Context.csv"));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("File Path", "Line number", "Code Context"));
			) {			
				
			csvPrinter.printRecords(result);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
}

