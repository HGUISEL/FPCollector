package edu.handong.csee.isel.fpcollector.structures;

import java.util.AbstractMap.SimpleEntry;

public class ContextPattern {
	
	private SimpleEntry<String, Integer> patternList;
	private Double patternScore = 0.0;
	private Double tpPatternScore = 0.0;
	private Double fpPatternScore = 0.0;
	private Double buggyPatternScore= 0.0;
	
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
	
	public Double getPatternScore() {
		return patternScore;
	}
	
	public void setPatternScore(Double score) {
		patternScore = score;
	}
	
	public Double getTpPatternScore() {
		return tpPatternScore;
	}
	
	public void setTpPatternScore(Double score) {
		tpPatternScore = score;
	}
	
	public Double getBuggyPatternScore() {
		return buggyPatternScore;
	}
	
	public void setBuggyPatternScore(Double score) {
		buggyPatternScore = score;
	}
	
	public Double getFpPatternScore() {
		return fpPatternScore;
	}
	
	public void setFpPatternScore(Double score) {
		fpPatternScore = score;
	}
	
}
