package edu.handong.csee.isel.fpcollector.structures;

import java.util.AbstractMap.SimpleEntry;

public class ContextPattern {
	private SimpleEntry<String, Integer> patternList;
	
	public ContextPattern(String pattern, Integer frequency) {
		patternList = new SimpleEntry<String, Integer>(pattern, frequency);
	}
	
	public SimpleEntry<String, Integer>getPattern() {
		return patternList;
	}
	
}
