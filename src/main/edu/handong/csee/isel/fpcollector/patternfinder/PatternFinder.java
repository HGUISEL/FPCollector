package edu.handong.csee.isel.fpcollector.patternfinder;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class PatternFinder {
	public ArrayList<SimpleEntry<String, Integer>> getDirLine(ArrayList<String[]> report) {
		ArrayList<SimpleEntry<String, Integer>> info = new ArrayList<>();
		for(String[] temp : report) {			
			String[] sperate = temp[0].split(":");
			Integer atLine = Integer.parseInt(sperate[1]);
			info.add(new SimpleEntry<String, Integer>(sperate[0], atLine));
		}
		return info;
	}

}
