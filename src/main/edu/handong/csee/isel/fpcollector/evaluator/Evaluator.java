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
		HashMap<String, Double> tpFrequencyNormalization = new HashMap<>();
		HashMap<String, Double> fpNodeNormalization = new HashMap<>();
		HashMap<String, Double> fpFrequencyNormalization = new HashMap<>();
		ArrayList<ContextPattern> scoredPattern = new ArrayList<>();
	
		tpNodeNormalization = nodeNormalization(tp);
		fpNodeNormalization = nodeNormalization(fp);
		
		tpFrequencyNormalization = frequencyNormalization(tp);
		fpFrequencyNormalization = frequencyNormalization(fp);
		
		scoredPattern = 
				getNodeScore(fp, tpNodeNormalization, fpNodeNormalization, tpFrequencyNormalization, fpFrequencyNormalization);
	}
	
	public HashMap<String,Double> nodeNormalization(ArrayList<ContextPattern> nodes) {
		HashMap<String, Double> nodeWeight = new HashMap<>();
		Double max = -999.0;
		Double min = 999.0;
		for(ContextPattern tempPattern : nodes) {
			//System.out.println(tempPattern.getPatternString() + "||" + tempPattern.getFrequency());
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
			//System.out.println("Node : " + temp.getKey() + " Weight : " + temp.getValue());
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
		System.out.println("\n\n");
		return nodeWeight;
	}
	
	public HashMap<String, Double> frequencyNormalization(ArrayList<ContextPattern> frequency){
		HashMap<String, Double> frequencyWeight = new HashMap<>();
		
		Double min = 999.0;
		Double max = -999.0;
		
		for(ContextPattern temp : frequency) {
			if(min > temp.getFrequency()) {
				min = Double.valueOf(temp.getFrequency().toString());
			}
			if(max < temp.getFrequency()) {
				max = Double.valueOf(temp.getFrequency().toString());
			}
			frequencyWeight.put(temp.getPatternString(), Double.valueOf(temp.getFrequency().toString()));
		}
		
		frequencyWeight = normalization(frequencyWeight, max, min);
		
		for(Map.Entry<String, Double> temp : frequencyWeight.entrySet()) {
			System.out.println("Pattern : " + temp.getKey() + "\tNormalized Frequency : " + temp.getValue());
		}
		System.out.println("\n\n");
		
		return frequencyWeight;
	}
	
	public HashMap<String, Double> normalization(HashMap<String, Double> nodeWeight, Double max, Double min) {
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			nodeWeight.put(temp.getKey(), (temp.getValue()-min)/(max-min));
		}
		return nodeWeight;
	}
	
	public ArrayList<ContextPattern> 
	getNodeScore(ArrayList<ContextPattern> nodes, HashMap<String, Double> tpNodeNormalization,
			HashMap<String, Double> fpNodeNormalization, HashMap<String, Double> tpFrequencyNormalization,
			HashMap<String, Double> fpFrequencyNormalization){
		ArrayList<ContextPattern> scoredPattern = new ArrayList<>();
		
		for(ContextPattern temp : nodes) {
			//node
			for(String tempNode :temp.getPatternString().split(",")) {
				if(tempNode.contains("[")){
					tempNode = tempNode.split("\\[")[1].trim();
				} else if(tempNode.contains("]")) {
					tempNode = tempNode.split("\\]")[0].trim();
				}
				if(tpNodeNormalization.containsKey(tempNode) /*&& 
						tpNodeNormalization.get(tempNode) > 0.1*/) {
					temp.setTpPatternScore(temp.getTpPatternScore() + tpNodeNormalization.get(tempNode) * (-1.0));
				}else if(fpNodeNormalization.containsKey(tempNode)){
				temp.setFpPatternScore(temp.getFpPatternScore() + fpNodeNormalization.get(tempNode) * (0.0));
				}
			}
			//pattern frequency
			if(tpFrequencyNormalization.containsKey(temp.getPatternString()) /*&& 
					tpFrequencyNormalization.get(temp.getPatternString()) > 0.1*/) {
				temp.setTpPatternScore(temp.getTpPatternScore() 
						+ tpFrequencyNormalization.get(temp.getPatternString()) * (-1.0));
			}else if(fpFrequencyNormalization.containsKey(temp.getPatternString())){
				temp.setFpPatternScore(temp.getFpPatternScore() + (fpFrequencyNormalization.get(temp.getPatternString())* 0.2));
			}
			temp.setPatternScore(temp.getFpPatternScore() + temp.getTpPatternScore());
			scoredPattern.add(temp);
		}
		
		Collections.sort(scoredPattern, new Comparator<ContextPattern>() {
			public int compare(ContextPattern o1, ContextPattern o2) {
				if (o1.getPatternScore() < o2.getPatternScore()) return 1;
				else if (o1.getPatternScore() > o2.getPatternScore()) return -1;
				return 0;
			}
		});
		
		int count = 0 ; 
		for(ContextPattern temp : scoredPattern) {
			System.out.println(temp.getPatternString() + "(" +temp.getFrequency() + ")" 
									+ "=>" + temp.getPatternScore());
			count ++;
			if(count == 5) break;
		}
		
		return scoredPattern;
	}
}
