package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

public class ReportComparator {
	//In FPC, its elements' form : report_information$$$violated_code
	public ArrayList<SimpleEntry<String, String>> FPC = new ArrayList<>();
	public ArrayList<SimpleEntry<String, String>> TPC = new ArrayList<>();
	
	public void run(ReportReader current, ReportReader past, String projectName) {
		getTFPC(current, past, projectName);
	}
	
	private void getTFPC(ReportReader current, ReportReader past, String projectName){
		System.out.println("currentReportSize: " + current.alarmedCodes.size());
		System.out.println("pastReportSize: " + past.alarmedCodes.size());
		
		for(int i = 0 ; i < current.alarmedCodes.size(); i++) {
//			String currentPath = current.alarmedCodes.get(i).getValue();
			String currentCode = current.alarmedCodes.get(i).getKey();
			for(int j = 0 ; j < past.alarmedCodes.size(); j++) {
//				String pastPath = past.alarmedCodes.get(j).getValue().split(":")[0].replaceFirst(projectName + "_P", projectName);
				String pastCode = past.alarmedCodes.get(j).getKey();
				if(currentCode.equals(pastCode) /*&& currentPath.split(":")[0].equals(pastPath)*/) {
					FPC.add(current.alarmedCodes.get(i));
					past.alarmedCodes.remove(j);
					break;
				}
			}
		}
		
		TPC.addAll(past.alarmedCodes);
		
		System.out.println("FPCSize: " + FPC.size());
		System.out.println("TPCSize: " + past.alarmedCodes.size());
	}
}
