package edu.handong.csee.isel.fpcollector.refactoring;

import java.io.File;
import java.util.ArrayList;

public class ReportComparator {
	//In FPC, its elements' form : report_information$$$violated_code
	public ArrayList<String> FPC = new ArrayList<>();
	public ArrayList<String> TPC = new ArrayList<>();
	
	public double fixingRate =0.0;
	
	public void run(ReportReader current, ReportReader past, String projectName) {
		getTFPC(current, past, projectName);
	}
	
	private void getTFPC(ReportReader current, ReportReader past, String projectName){
		System.out.println("cSize: " + current.alarmedCodes.size());
		System.out.println("pSize: " + past.alarmedCodes.size());
		
		for(int i = 0 ; i < current.alarmedCodes.size(); i++) {
			String currentPath = current.alarmedCodes.get(i).getValue();
			String currentCode = current.alarmedCodes.get(i).getKey();
			for(int j = 0 ; j < past.alarmedCodes.size(); j++) {
				String pastPath = past.alarmedCodes.get(j).getValue().split(":")[0].replaceFirst(projectName + "_P", projectName);
				String pastCode = past.alarmedCodes.get(j).getKey();
				if(currentCode.equals(pastCode) && currentPath.split(":")[0].equals(pastPath)) {
					FPC.add(currentPath + "%%%%%" +currentCode);
					past.alarmedCodes.remove(j);
					break;
				}
			}
		}
		
		System.out.println("FPCsize: " + FPC.size());
		System.out.println("pSize2: " + past.alarmedCodes.size());
		
		getTPC(past, projectName);
	}
	
	private void getTPC(ReportReader past, String projectName) {
		for (int i = 0; i < past.alarmedCodes.size(); i++) {
			String pastReportInfo = past.alarmedCodes.get(i).getValue();
			String reportPath = pastReportInfo.split(":")[0].replaceFirst(projectName + "_P", projectName);
//			System.out.println(reportInfo[0]);
			File f = new File(reportPath);
			
			if (f.exists())
//				System.out.println(past.reportInformation.get(i) + "%%%%%" + past.alarmedCodes.get(i));
				TPC.add(pastReportInfo + "%%%%%" + past.alarmedCodes.get(i).getKey());
		}
	}
}
