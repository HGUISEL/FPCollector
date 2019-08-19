package edu.handong.csee.isel.fpcollector.structures;

import java.util.AbstractMap.SimpleEntry;

public class ContextPattern {
	
	private SimpleEntry<String, Integer> patternList;
	private Double nodeScore = 0.0;
	
	public ContextPattern(String pattern, Integer frequency) {
		patternList = new SimpleEntry<String, Integer>(pattern, frequency);
	}
	
	public SimpleEntry<String, Integer> getPattern() {
		return patternList;
	}
	
	public Integer getFrequency() {
		return patternList.getValue();
	}
	
	public String getPatternString(){
		return patternList.getKey();
	}
	
	public Double getNodeScore() {
		return nodeScore;
	}
	
	public void setNodeScore(Double score) {
		nodeScore = score;
	}
	
}
