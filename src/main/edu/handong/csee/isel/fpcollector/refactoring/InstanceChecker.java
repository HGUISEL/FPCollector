package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class InstanceChecker {
	final static int FPC = 4;
	final static int TPC = 5;
	
	public Integer fpcInstanceNumber = 0;
	public Integer tpcInstanceNumber = 0;
		
	public void checkNum(String fileName, int type) {
		
		Reader outputFile;
		int count = 0;
		try {
			outputFile = new FileReader(fileName);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);
			
			if(type == FPC) {
				for(CSVRecord record : records) {
					if(record.get(0).equals("File Path")) continue;
				count++;
				}
				fpcInstanceNumber = count;
			} 
			
			else if(type == TPC) {
				for(CSVRecord record : records) {
					if(record.get(0).equals("File Path")) continue;
				count++;
				}
				tpcInstanceNumber = count;
			} 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
		}
	}
	
}
