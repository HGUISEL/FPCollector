package edu.handong.csee.isel.fpcollector.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fpcollector.structures.DirLineErrmsgContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Writer {
	public void writeComparisonResult(HashMap<String, String> suspects, String path) {
		File file = new File(path);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			for(Map.Entry<String, String> entry: suspects.entrySet()) {
				String dir = entry.getValue().split(",")[0];
				String lineNum = entry.getValue().split(",")[1];
				String errorMessage = entry.getValue().split(",")[2];
				writer.write(dir + ", " + lineNum + "," + errorMessage + "\n");
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
	public void writeContextsForDFA
	(DirLineErrmsgContext context, String path) {
		String fileName = "./Result";/* ./Result.csv */
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName + "_w_Context.csv"));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("File Path", "Line number", "Anomaly", "Variable", "Code Context"));
			) {			
			for(String line : context.getDirLineContext()) {
				String dir = line.split(",")[0];
				String lineNum = line.split(",")[1].trim();
				String errmsg = line.split(",")[2];
				String anomaly = errmsg.split("-anomaly for")[0];
				String var = errmsg.split("-anomaly for")[1];
				//String contexts = line.split(",%%%%%")[1];
				String contexts = line.split("!@#")[1];
				csvPrinter.printRecord(dir, lineNum, anomaly, var, contexts);
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
}

