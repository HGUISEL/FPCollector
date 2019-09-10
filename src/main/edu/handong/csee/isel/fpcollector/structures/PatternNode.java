package edu.handong.csee.isel.fpcollector.structures;

import java.util.AbstractMap.SimpleEntry;

public class PatternNode {
	String pattern = "";
	Integer score = 0;
	
	public PatternNode(String node, Integer weight) {
		pattern = node;
		score = weight;
	}
	
//	public SimpleEntry<String, Integer> getNode() {
//		return 
//	}
//	
//	public Integer getWeight() {
//		return patternList.getValue();
//	}
//	
//	public String getNodeString(){
//		return patternList.getKey();
//	}
//	
//	public SimpleEntry<String, Integer> addWeight(Integer weight){
//		patternList.setValue(patternList.getValue() + weight);
//		return patternList;
//	}
}
