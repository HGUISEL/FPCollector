package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PatternAdjustment {
	
	public HashMap<ArrayList<String>, Integer> adjustTP
	(HashMap<ArrayList<String>, Integer> tpPatterns, 
			HashMap<ArrayList<String>, Integer> bPatterns){
		HashMap<ArrayList<String>, Integer> adjustedTPPattern = new HashMap<>();
		
		int size = tpPatterns.size();
		int progress = 0;
		System.out.println("size : " + size);
		System.out.println("Bsize : " + bPatterns.size());
		//195
		int count = 0 ;
		Integer pCount = 0;
		System.out.println("adjusted size " + adjustedTPPattern.size());
		for(Entry<ArrayList<String>, Integer> bPattern : bPatterns.entrySet()) {
			for(Entry<ArrayList<String>, Integer> anotherBPattern : bPatterns.entrySet()) {
				if(bPattern.getKey().equals(anotherBPattern.getKey())) {
					pCount =  + anotherBPattern.getValue();
					adjustedTPPattern.put(bPattern.getKey(), pCount);
					break;
				}
			}
		}
//		 
		System.out.println("adjusted size " + adjustedTPPattern.size());
//		//121
//		for(Entry<ArrayList<String>, Integer> tpPattern : tpPatterns.entrySet()) {
//			if(bPatterns.containsKey(tpPattern.getKey())) {
//				adjustedTPPattern.put(tpPattern.getKey(), tpPattern.getValue());
//			}
//		}
		
//		for(Entry<ArrayList<String>, Integer> tpPattern : tpPatterns.entrySet())
//		 {
//			progress++;
//			for(Entry<ArrayList<String>, Integer> bPattern : bPatterns.entrySet()) {
//				if(tpPattern.getKey().equals(bPattern.getKey())) {
//					adjustedTPPattern.put(tpPattern.getKey(), tpPattern.getValue());
//					bPatterns.remove(bPattern.getKey());
//					break;
//				}
//			}
//			if(progress == 1) {
//				System.out.print("Start to adjusting Patterns...\n0%...");
//			}
//			if(progress == size/4) {
//				System.out.print("25%...");
//			}
//			if(progress == size/2) {
//				System.out.print("50%...");	
//			}
//			if(progress == size * 3 / 4) {
//				System.out.print("75%...");	
//			}
//			if(progress == size - 1) {
//				System.out.print("done...\n");	
//			}
//		}
		System.out.println("Adjusted Size : " + adjustedTPPattern.entrySet().size());
		return adjustedTPPattern;
	}
	
	public HashMap<ArrayList<String>, Integer> adjustBP
	(HashMap<ArrayList<String>, Integer> tpPattern, 
			HashMap<ArrayList<String>, Integer> bPattern){
		
		return bPattern;
	}
}
