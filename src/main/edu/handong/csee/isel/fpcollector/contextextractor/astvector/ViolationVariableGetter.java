package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.util.ArrayList;

public class ViolationVariableGetter {
	public ArrayList<String> getViolationVariable(ArrayList<String> result){
		ArrayList<String> vV = new ArrayList<>();
		
		for(String info : result) {
			String varName = "";
			String errMsg = info.split(",")[2];
			String splitToGetName = errMsg.split("variable '")[1];
			char[] toGetName = splitToGetName.toCharArray();
			for(Character letter : toGetName) {
				if(Character.isAlphabetic(letter) || Character.isDigit(letter)) {
				varName = varName.concat("" + letter);
				} else break;
			}
			vV.add(info + ", " + varName);
		}
		
		return vV;
	}
}
