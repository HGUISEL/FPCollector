package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;

public class PatternVector {
	HashMap<String, Integer> patternVector = new HashMap<>();
	
	public HashMap<String, Integer> getPatternVector(){
		return patternVector;
	}
	
	public boolean checkEmpty() {
		if(patternVector.size() == 0) {
			return true;
		} else return false;
	}
	
	public void addNodes(ASTNode tempNode) {
		HashMap<String, Integer> tempPatternVector = new HashMap<>();
		tempPatternVector.putAll(patternVector);
		
		for(String p : tempPatternVector.keySet()) {
			String temp = p+" " +tempNode.getClass().getName();
			
			if(patternVector.get(temp) != null) {
				int tempCount = patternVector.get(temp);
				patternVector.put(temp, ++tempCount);
				
			} else {
				patternVector.put(p+" "+tempNode.getClass().getName(), 1);
				
			}
		}
	}
	
	public void addNodes(ASTNode tempNode, Pattern pattern) {
		for(String p : pattern.getPattern()) {
			String temp = p+" " +tempNode.getClass().getName();
			
			if(patternVector.get(temp) != null) {
				int tempCount = patternVector.get(temp);
				patternVector.put(temp, ++tempCount); 
			} else {
				patternVector.put(p+" "+tempNode.getClass().getName(), 1);
			}
		}
	}
}
