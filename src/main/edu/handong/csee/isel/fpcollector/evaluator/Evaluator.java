package edu.handong.csee.isel.fpcollector.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.handong.csee.isel.fpcollector.structures.ContextPattern;
/**
 * 
 * Eliminate Common Contexts which aren't indicate False Positives' Context.
 * 
 * <p> 	Process<br>
 * <pre>
 * 1. Normalize nodes which are in TP and FP.
 * 2. Normalize Frequency of Each TP and FP.
 * 3. Get Pattern Score.
 * 4. Print top 5.
 * </pre>
 * <p>Input	<br>
 * <pre>
 * 1) Top 20 Common Contexts of False Positive (not accurate).
 * 2) Top 20 Common Contexts of True Positive (not accurate).
 * </pre>
 * <p>Output<br>
 * <pre>
 * 1) Print FalsePositives' Common Context(Until accuracy 100% for 2 Projects, dubbo and spring-boot)
 * </pre>
 * 
 * @author yoonhochoi
 * @version 1.0
 * @since 1.0
 */
public class Evaluator {
	public void run
	(ArrayList<ContextPattern> tp, ArrayList<ContextPattern> fp, ArrayList<ContextPattern> buggy) {
		HashMap<String, Double> tpNodeNormalization = new HashMap<>();
		HashMap<String, Double> tpFrequencyNormalization = new HashMap<>();
		HashMap<String, Double> fpNodeNormalization = new HashMap<>();
		HashMap<String, Double> fpFrequencyNormalization = new HashMap<>();
		HashMap<String, Double> buggyNodeNormalization = new HashMap<>();
		HashMap<String, Double> buggyFrequencyNormalization= new HashMap<>();
		
		ArrayList<ContextPattern> scoredPattern = new ArrayList<>();
	
		tpNodeNormalization = nodeNormalization(tp);
		buggyNodeNormalization = nodeNormalization(buggy);
		fpNodeNormalization = nodeNormalization(fp);
		
		tpFrequencyNormalization = frequencyNormalization(tp);
		buggyFrequencyNormalization = frequencyNormalization(buggy);
		fpFrequencyNormalization = frequencyNormalization(fp);
		
		scoredPattern = 
			getPatternScore(fp, tpNodeNormalization, buggyNodeNormalization, fpNodeNormalization, 
					tpFrequencyNormalization, buggyFrequencyNormalization,fpFrequencyNormalization);
	}
	
	public HashMap<String,Double> nodeNormalization(ArrayList<ContextPattern> nodes) {
		HashMap<String, Double> nodeWeight = new HashMap<>();
		Double max = -999.0;
		Double min = 999.0;
		Double total = 0.0;
		Double avrg = 0.0;
		Double standardDeviation = 0.0;
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
		
		//for standardization
		ArrayList<Double> values = new ArrayList<>();
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			//System.out.println("Node : " + temp.getKey() + " Weight : " + temp.getValue());
			values.add(temp.getValue());
		}
		
		for(Double tempVal : values) {
			total += tempVal;
		}
		avrg = total / values.size();
		standardDeviation = getStandardDeviation(values, avrg, 0);
		nodeWeight = standardization(nodeWeight, avrg, standardDeviation);
		
		//for normalization
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
				
		//Print node Frequency
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
		frequencyWeight.put(temp.getPatternString(), Double.valueOf(temp.getFrequency().toString()));
	}
		
		//for standardization
		Double total = 0.0;
		Double avrg = 0.0;
		Double standardDeviation = 0.0;
		ArrayList<Double> values = new ArrayList<>();
			for(ContextPattern temp : frequency) {
				//System.out.println("Node : " + temp.getKey() + " Weight : " + temp.getValue());
				values.add(Double.valueOf(temp.getFrequency().toString()) );
			}
		
			for(Double tempVal : values) {
				total += tempVal;
			}
			avrg = total / values.size();
			standardDeviation = getStandardDeviation(values, avrg, 0);
			frequencyWeight = standardization(frequencyWeight, avrg, standardDeviation);
		
		//for normalization
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
				
		//print pattern frequency
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
	
	public Double getStandardDeviation(ArrayList<Double> values, Double avrg, int opt){
		Double sum = 0.0;
		Double sd = 0.0;
		Double diff;
		for(Double value : values) {
			diff = value - avrg;
			sum += diff * diff;
		}
		sd = Math.sqrt(sum / (values.size() - opt));
		return sd;
	}
	
	public HashMap<String, Double> standardization(HashMap<String, Double> nodeWeight, Double avrg, Double stdDevi) {
		for(Map.Entry<String, Double> temp : nodeWeight.entrySet()) {
			nodeWeight.put(temp.getKey(), (temp.getValue()-avrg)/stdDevi);
		}
		return nodeWeight;
	}
	
	public ArrayList<ContextPattern> 
	getPatternScore(ArrayList<ContextPattern> nodes, HashMap<String, Double> tpNodeNormalization,
			HashMap<String, Double> buggyNodeNormalization, HashMap<String, Double> fpNodeNormalization,
			HashMap<String, Double> tpFrequencyNormalization, HashMap<String, Double> buggyFrequencyNormalization,
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
					//System.out.println(temp.getTpPatternScore());
					temp.setTpPatternScore(temp.getTpPatternScore() + tpNodeNormalization.get(tempNode) * (-1.0));
					//System.out.println(temp.getTpPatternScore());
				} if(buggyNodeNormalization.containsKey(tempNode)){
					//System.out.println(temp.getFpPatternScore());
					temp.setBuggyPatternScore(temp.getBuggyPatternScore() + buggyNodeNormalization.get(tempNode) * (1.0));
					//System.out.println(temp.getFpPatternScore());
				} if(fpNodeNormalization.containsKey(tempNode)){
					//System.out.println(temp.getFpPatternScore());
					temp.setFpPatternScore(temp.getFpPatternScore() + fpNodeNormalization.get(tempNode) * (0.0));
					//System.out.println(temp.getFpPatternScore());
				}
			}
			//pattern frequency
			if(tpFrequencyNormalization.containsKey(temp.getPatternString()) /*&& 
					tpFrequencyNormalization.get(temp.getPatternString()) > 0.1*/) {
				temp.setTpPatternScore(temp.getTpPatternScore() 
						+ tpFrequencyNormalization.get(temp.getPatternString()) * (-1.0));
			} if(buggyFrequencyNormalization.containsKey(temp.getPatternString())){
				//System.out.println(temp.getFpPatternScore());
				//System.out.println(fpFrequencyNormalization.get(temp.getPatternString()));
				temp.setBuggyPatternScore(temp.getBuggyPatternScore() + (buggyFrequencyNormalization.get(temp.getPatternString())* 1.0));
				//System.out.println(temp.getFpPatternScore());
			} if(fpFrequencyNormalization.containsKey(temp.getPatternString())){
				//System.out.println(temp.getFpPatternScore());
				//System.out.println(fpFrequencyNormalization.get(temp.getPatternString()));
				temp.setFpPatternScore(temp.getFpPatternScore() + (fpFrequencyNormalization.get(temp.getPatternString())* 1.0));
				//System.out.println(temp.getFpPatternScore());
			}
			temp.setPatternScore(temp.getFpPatternScore() + temp.getTpPatternScore() + temp.getBuggyPatternScore());
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
		System.out.println("\n Top 5. FP Common Cotext");
		for(ContextPattern temp : scoredPattern) {
			System.out.println(temp.getPatternString() + "(" +temp.getFrequency() + ")" 
									+ " Score : " + temp.getPatternScore());
			count ++;
			if(count == 5) break;
		}
		
		return scoredPattern;
	}
}
