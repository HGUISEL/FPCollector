package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import edu.handong.csee.isel.fpcollector.structures.VectorNode;

public class SupportCountGetter {
	public ArrayList<ArrayList<VectorNode>> 
	getSupportCount(ArrayList<ArrayList<VectorNode>> ctxVectorInfo){
		ArrayList<SimpleEntry<Integer, Integer>> supportCount = new ArrayList<>();
		ArrayList<SimpleEntry<Integer, Integer>> validSupportCount = new ArrayList<>();
		int[] table = new int[100];
		Arrays.fill(table, -1);
		Integer idx = 0 ;
		
		for(ArrayList<VectorNode> tempList : ctxVectorInfo) {
			for(VectorNode temp : tempList) {
				table[temp.getNode().getNodeType()]++;
			}
		}
		
		for(int tempCount : table) {
			supportCount.add(new SimpleEntry<Integer, Integer>(idx, tempCount));
			idx++;
		}
		
		for(SimpleEntry<Integer, Integer> tempCountInfo : supportCount) {
			if(tempCountInfo.getValue() != -1) {
				validSupportCount.add(tempCountInfo);
			}
		}
		
		Comparator<SimpleEntry<Integer, Integer>> supCountComp = new Comparator<SimpleEntry<Integer, Integer>>(){
			@Override
			public int compare(SimpleEntry<Integer, Integer> Ent1, SimpleEntry<Integer, Integer> Ent2) {
				return Ent2.getValue() - Ent1.getValue();
			}
		};	
		Collections.sort(validSupportCount, supCountComp);
		
		System.out.println("\n=========SORTING==========");
		for(SimpleEntry<Integer, Integer> tempCountInfo : validSupportCount) {
			System.out.println("TypeNum : " + tempCountInfo.getKey() + ", SupportCount : " + tempCountInfo.getValue());
		}
		
		return new ArrayList<ArrayList<VectorNode>>();
	}
}
