package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

public class ReportComparator {
	//In FPC, its elements' form : report_information$$$violated_code
	public ArrayList<String> FPC = new ArrayList<>();
	
	public void getFPC(ReportReader current, ReportReader past){
		for(int i = 0 ; i < current.alarmedCodes.size(); i++) {
			String currentCode = current.alarmedCodes.get(i);
			for(int j = 0 ; j < past.alarmedCodes.size(); j++) {
				String pastCode = past.alarmedCodes.get(j);
				if(currentCode.equals(pastCode)) {
					FPC.add(current.reportInformation.get(i) + "%%%%%" +currentCode);
					past.alarmedCodes.remove(j);
					break;
				}
			}
		}
	}
}
