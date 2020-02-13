package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.File;
import java.util.ArrayList;

public class ReportComparator {
	//In FPC, its elements' form : report_information$$$violated_code
	public ArrayList<String> FPC = new ArrayList<>();
	public ArrayList<String> TPC = new ArrayList<>();
	
	public double fixingRate =0.0;
	
	public void getTFPC(ReportReader current, ReportReader past, String projectName){
		for(int i = 0 ; i < current.alarmedCodes.size(); i++) {
			String currentCode = current.alarmedCodes.get(i);
			for(int j = 0 ; j < past.alarmedCodes.size(); j++) {
				String pastCode = past.alarmedCodes.get(j);
				if(currentCode.equals(pastCode)) {
					FPC.add(current.reportInformation.get(i) + "%%%%%" +currentCode);
					past.alarmedCodes.remove(j);
					past.reportInformation.remove(j);
					break;
				}
			}
		}
		
		for (int i = 0; i < past.alarmedCodes.size(); i++) {
			String[] reportInfo = past.reportInformation.get(i).split(":");
			reportInfo[0].replaceFirst(projectName + "_P", projectName);
			File f = new File(reportInfo[0]);
			
			if (f.exists())
				TPC.add(past.reportInformation.get(i) + "%%%%%" + past.alarmedCodes.get(i));
		}
	}
	
	public void getFixingRate(ReportReader current, ReportReader past) {
		int totalPastAlarmSize = past.alarmedCodes.size();
		int remainCount = 0;
		for(int i = 0 ; i < past.alarmedCodes.size(); i ++) {
			String pastCode = past.alarmedCodes.get(i);
			for(int j = 0; j < current.alarmedCodes.size(); j ++) {
				String currentCode = current.alarmedCodes.get(j);
				if(pastCode.equals(currentCode)) {
					current.alarmedCodes.remove(j);
					remainCount++;
					break;
				}				
			}
		}
		fixingRate = 1 - ((double)(totalPastAlarmSize - remainCount) / (double)totalPastAlarmSize); 
	}
}
