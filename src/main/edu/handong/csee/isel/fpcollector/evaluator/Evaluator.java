package edu.handong.csee.isel.fpcollector.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.handong.csee.isel.fpcollector.structures.ContextPattern;

public class Evaluator {
	public void run(ArrayList<ContextPattern> tp, ArrayList<ContextPattern> fp) {
		HashMap<String, Double> tpNodeNormalization = new HashMap<>();
//		HashMap<String, Double> tpFrequencyNormalization = new HashMap<>();
		HashMap<String, Double> fpNodeNormalization = new HashMap<>();
//		HashMap<String, Double> fpFrequencyNormalization = new HashMap<>();
		ArrayList<ContextPattern> scoredTruePositive = new ArrayList<>();
		ArrayList<ContextPattern> scoredFalsePositive = new ArrayList<>();
		
		tpNodeNormalization = nodeNormalization(tp);
		fpNodeNormalization = nodeNormalization(fp);
		
		scoredTruePositive = getNodeScore(tp, tpNodeNormalization);
		scoredFalsePositive = getNodeScore(fp, fpNodeNormalization);
		
		//tpFrequencyNormalization = frequencyNormalization();
	}
	
	public ArrayList<ContextPattern> 
	getNodeScore(ArrayList<ContextPattern> nodes, HashMap<String, Double> normalization){
		ArrayList<ContextPattern> scoredPattern = new ArrayList<>();
		
		for(ContextPattern temp : nodes) {
			for(String tempNode :temp.getPatternString().split(",")) {
				if(tempNode.contains("[")){
					tempNode = tempNode.split("\\[")[1];
				} else if(tempNode.contains("]")) {
					tempNode = tempNode.split("\\]")[0];
				}
				temp.setNodeScore(temp.getNodeScore() + normalization.get(tempNode));
			}
			scoredPattern.add(temp);
		}
		
		Collections.sort(scoredPattern, new Comparator<ContextPattern>() {
			public int compare(ContextPattern o1, ContextPattern o2) {
				if (o1.getNodeScore() < o2.getNodeScore()) return 1;
				else if (o1.getNodeScore() > o2.getNodeScore()) return -1;
				return 0;
			}
		});
		
		for(ContextPattern temp : scoredPattern) {
			System.out.println(temp.getPatternString() + "(" +temp.getFrequency() + ")" 
									+ "=>" + temp.getNodeScore());
		}
		
		return scoredPattern;
	}
	
	public HashMap<String,Double> nodeNormalization(ArrayList<ContextPattern> nodes) {
		HashMap<String, Double> nodeWeight = new HashMap<>();
		Double max = -999.0;
		Double min = 999.0;
		for(ContextPattern tempPattern : nodes) {
			System.out.println(tempPattern.getPatternString() + "||" + tempPattern.getFrequency());
			for(String tempNode : tempPattern.getPatternString().split(",")) {
				
				if(tempNode.contains("[")){
					tempNode = tempNode.split("\\[")[1];
				} else if(tempNode.contains("]")) {
					tempNode = tempNode.split("\\]")[0];
				}
				
				if(!nodeWeight.containsKey(tempNode)) {
					nodeWeight.put(tempNode, Double.valueOf(tempPattern.getFrequency().toString()));
				} else {
					nodeWeight.put(tempNode, nodeWeight.get(tempNode) + Double.valueOf(tempPattern.getFrequency().toString()));
				}
			}
		}
		
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			System.out.println("Node : " + temp.getKey() + " Weight : " + temp.getValue());
			if(max < temp.getValue()) {
				max = Double.valueOf(temp.getValue().toString());
			}
			if(min > temp.getValue()) {
				min = Double.valueOf(temp.getValue().toString());
			}
		}
		
		nodeWeight = normalization(nodeWeight, max, min);
		
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			System.out.println("Node : " + temp.getKey() + " Weight : " + temp.getValue());
		}
		
		return nodeWeight;
	}
	
	
	
	public HashMap<String, Double> normalization(HashMap<String, Double> nodeWeight, Double max, Double min) {
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			nodeWeight.put(temp.getKey(), (temp.getValue()-min)/(max-min));
		}
		return nodeWeight;
	}
}
