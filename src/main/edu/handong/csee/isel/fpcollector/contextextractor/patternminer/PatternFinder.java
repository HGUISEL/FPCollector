package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.handong.csee.isel.fpcollector.structures.VectorNode;

public class PatternFinder {
	//Key : Pattern, Value : Size of Pattern (e.g. if pattern is [ 1, 2 ] than size of pattern is 2)
	public HashMap<ArrayList<Integer>, Integer> mineAllSequentialPatterns
	(ArrayList<ArrayList<VectorNode>> contextVectorInformation){
		HashMap<ArrayList<Integer>, Integer> patterns = new HashMap<>();
		int minSize = 99999;
		int maxPatternSize = -1;
		//get Maximum size of pattern which is the minimum of contextVector size
		for(ArrayList<VectorNode> tempNodes : contextVectorInformation) {
			if(minSize > tempNodes.size()) {
				minSize = tempNodes.size();
			}
		}
		maxPatternSize = minSize;
		
		//get Patterns
		//get Pattern size from two to max Pattern size
		for(int i = 2; i<= maxPatternSize; i ++) {
			//get context vector one by one
			for(ArrayList<VectorNode> tempPattern : contextVectorInformation) {
				//print Nodes Vector
//				System.out.print("Nodes : [" );
//				for(VectorNode tempNode : tempPattern) {
//					System.out.print(tempNode.getNode().getNodeType() + ", ");
//				}
//				System.out.println("] ");
				//get pattern according to pattern size and its rule(in this case, sequential)
				for(int j = 0 ; j < tempPattern.size() - i + 1 ; j ++) {
					ArrayList<Integer> tempPatternContext = new ArrayList<>();
					int tempIdx = j;
					int tempNumOfNode = i;
					while(tempNumOfNode > 0) {
					tempPatternContext.add(tempPattern.get(tempIdx).getNode().getNodeType());
					tempIdx++;
					tempNumOfNode--;
					}
					patterns.put(tempPatternContext, i);
				}
			}
		}
		//print all pattern
//		for(Map.Entry<ArrayList<Integer>, Integer> elements : patterns.entrySet()) {
//			System.out.print("Pattern : [");
//			for(Integer patternElements : elements.getKey()) {
//				System.out.print(patternElements + ", ");
//			}
//			System.out.print("]\n");
//		}
		//print the number of patterns
		System.out.println("Number of Patterns : " + patterns.size());
		
		return patterns;
	}
	
	public HashMap<ArrayList<Integer>, Integer> getSPFrequency
	(ArrayList<ArrayList<VectorNode>> contextVectorInformation,
	HashMap<ArrayList<Integer>, Integer> allSequentialPatterns){
		HashMap<ArrayList<Integer>, Integer> frequency = new HashMap<>();
		
		ArrayList<ArrayList<Integer>> CVInNumber= new ArrayList<>();
		
		for(ArrayList<VectorNode> contextVector : contextVectorInformation) {
			ArrayList<Integer> tempVector = new ArrayList<>();
			for(VectorNode node : contextVector) {
				tempVector.add(node.getNode().getNodeType());
			}
			CVInNumber.add(tempVector);
		}
		//allow duplication in one vector(e.g. [1,2,3,1,2,3] -> pattern [1,2,3] twice
		for(Map.Entry<ArrayList<Integer>, Integer> pattern : allSequentialPatterns.entrySet()) {
			int count = 0;
			for(ArrayList<Integer> vector : CVInNumber) {
				for(int i = 0 ; i < vector.size(); i ++) {
					for(int j = 0 ; j < pattern.getKey().size(); j ++) {
						int tempVectorIdx = i;
						int tempPatternIdx = j;
						int correct = 0;
						while(vector.size() > pattern.getKey().size() &&
								tempVectorIdx < vector.size() &&
								tempPatternIdx < pattern.getKey().size() &&
								vector.get(tempVectorIdx) == pattern.getKey().get(tempPatternIdx)) {
							correct ++;
							tempPatternIdx ++;
							tempVectorIdx ++;
						}
						if(correct == pattern.getKey().size()) {
							count ++;
						}
					}
				}
			}
			frequency.put(pattern.getKey(), count);
		}
		
		return frequency;
	}

	public HashMap<ArrayList<Integer>, Integer> 
	sortByCounting(HashMap<ArrayList<Integer>, Integer> patternFrequency) {
		Map<String, Integer> frequency = new HashMap<>();
		
		for(ArrayList<Integer> tempKey : patternFrequency.keySet()) {
			Integer tempValue = patternFrequency.get(tempKey);
			String keys = tempKey.toString();
			frequency.put(keys, tempValue);
		}
		
		List<String> frequencyList = new ArrayList<>(frequency.keySet());
		
		Collections.sort(frequencyList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return frequency.get(o2).compareTo(frequency.get(o1));
			}
		});
		
		int ranking = 31;
		int count = 0;
		for(String key : frequencyList) {
			if(count == ranking) {
				break;
			}
			System.out.println(String.format("Key : %s, Value : %d", key, frequency.get(key)));
			count ++;
		}
		
		return null;
	}
}
