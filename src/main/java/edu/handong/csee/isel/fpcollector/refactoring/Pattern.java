package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

public class Pattern {
	ArrayList<String> pattern = new ArrayList<>();
	
	public void addPattern(String node) {
		pattern.add(node);
	}
	
	public ArrayList<String> getPattern(){
		return pattern;
	}
	
}
