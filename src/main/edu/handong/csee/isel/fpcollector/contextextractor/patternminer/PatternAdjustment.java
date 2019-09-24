package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PatternAdjustment {
	
	public HashMap<String, Integer> adjustTP
	(HashMap<String, Integer> tpPatterns, 
			HashMap<String, Integer> bPatterns){
		HashMap<String, Integer> adjustedTPPattern = new HashMap<>();
		
		int size = tpPatterns.size();
		int progress = 0;
		System.out.println("size : " + size);
		System.out.println("Bsize : " + bPatterns.size());
		//195

		 
		System.out.println("adjusted size " + adjustedTPPattern.size());
//		//121
		for(Entry<String, Integer> tpPattern : tpPatterns.entrySet()) {
			progress++;
			if(!bPatterns.containsKey(tpPattern.getKey())) {
				adjustedTPPattern.put(tpPattern.getKey(), tpPattern.getValue());
			}
			if(progress == 1) {
				System.out.print("Start to adjusting Patterns...\n0%...");
			}
			if(progress == size/4) {
				System.out.print("25%...");
			}
			if(progress == size/2) {
				System.out.print("50%...");	
			}
			if(progress == size * 3 / 4) {
				System.out.print("75%...");	
			}
			if(progress == size - 1) {
				System.out.print("done...\n");	
			}
		}
		
		System.out.println("Adjusted Size : " + adjustedTPPattern.entrySet().size());
		return adjustedTPPattern;
	}
	
	public HashMap<String, Integer> adjustBP
	(HashMap<String, Integer> tpPatterns, 
			HashMap<String, Integer> bPatterns){
		
		HashMap<String, Integer> adjustedBPattern = new HashMap<>();
		
		int size = tpPatterns.size();
		int progress = 0;
		System.out.println("size : " + size);
		System.out.println("Bsize : " + bPatterns.size());

		 
		System.out.println("adjusted size " + adjustedBPattern.size());

		for(Entry<String, Integer> bPattern : bPatterns.entrySet()) {
			progress++;
			if(!tpPatterns.containsKey(bPattern.getKey())) {
				adjustedBPattern.put(bPattern.getKey(), bPattern.getValue());
			}
			if(progress == 1) {
				System.out.print("Start to adjusting Patterns...\n0%...");
			}
			if(progress == size/4) {
				System.out.print("25%...");
			}
			if(progress == size/2) {
				System.out.print("50%...");	
			}
			if(progress == size * 3 / 4) {
				System.out.print("75%...");	
			}
			if(progress == size - 1) {
				System.out.print("done...\n");	
			}
		}
		
		System.out.println("Adjusted Size : " + adjustedBPattern.entrySet().size());
		return adjustedBPattern;
	}
}
