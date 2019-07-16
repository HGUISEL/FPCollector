package edu.handong.csee.isel.fpcollector.utils;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

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
}
