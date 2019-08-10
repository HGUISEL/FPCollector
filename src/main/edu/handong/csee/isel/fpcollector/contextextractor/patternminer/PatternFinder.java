package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.structures.VectorNode;

public class PatternFinder {
	public HashMap<ArrayList<String>, Integer> mineLinePatterns
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation){
		HashMap<ArrayList<String>, Integer> linePatterns = new HashMap<>();
		
		//Among contextVecotInformation, get line which include violated variable and its Pattern
		//1) get All Sequential Nodes until node's start line is same with SimpleName's start line
		//2) don't care about number of nodes just get All.
		
		//START HERE
		//START HERE
		//START HERE
		//START HERE
		//START HERE
		//START HERE
		
		return linePatterns;
	}
	
	
	//Key : Pattern, Value : Size of Pattern (e.g. if pattern is [ 1, 2 ] than size of pattern is 2)
	public HashMap<ArrayList<String>, Integer> mineAllSequentialPatterns
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation){
		HashMap<ArrayList<String>, Integer> patterns = new HashMap<>();
		int minSize = 99999;
		int maxPatternSize = -1;
		//get Maximum size of pattern which is the minimum of contextVector size
		for(SimpleEntry<ASTNode, ArrayList<VectorNode>> tempNodes : contextVectorInformation) {
			if(minSize > tempNodes.getValue().size()) {
				minSize = tempNodes.getValue().size();
			}
		}
		maxPatternSize = minSize;
		
		//get Patterns
		//get Pattern size from two to max Pattern size(i means minimum pattern size)
		for(int i = 2; i<= maxPatternSize; i ++) {
			//get context vector one by one
			for(SimpleEntry<ASTNode, ArrayList<VectorNode>> tempPattern : contextVectorInformation) {
				//print Nodes Vector
//				System.out.print("Nodes : [" );
//				for(VectorNode tempNode : tempPattern) {
//					System.out.print(tempNode.getNode().getNodeType() + ", ");
//				}
//				System.out.println("] ");
				//get pattern according to pattern size and its rule(in this case, sequential)
				for(int j = 0 ; j < tempPattern.getValue().size() - i + 1 ; j ++) {
					ArrayList<String> tempPatternContext = new ArrayList<>();
					int tempIdx = j;
					int tempNumOfNode = i;
					int flag = 0;
					while(tempNumOfNode > 0 && tempIdx<tempPattern.getValue().size()) {
						if(tempPattern.getValue().get(tempIdx).getVectorNodeInfo().equals("Useless")) {
							tempIdx++;
							continue;
						}
						if(tempPattern.getValue().get(tempIdx).getVectorNodeInfo().equals("SimpleName")) {
							flag++;
						}
						if(flag == 2) {
							break;
						}
						tempPatternContext.add(tempPattern.getValue().get(tempIdx).getVectorNodeInfo());
						tempIdx++;
						tempNumOfNode--;
					}
					
					if(tempPatternContext.get(0).equals("SimpleName") /*&&
							!tempPatternContext.contains("Useless")*/) {
						patterns.put(tempPatternContext, i);
					}
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
	
	public HashMap<ArrayList<String>, Integer> getSPFrequency
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation,
	HashMap<ArrayList<String>, Integer> allSequentialPatterns){
		HashMap<ArrayList<String>, Integer> frequency = new HashMap<>();
		
		ArrayList<ArrayList<String>> contextVectors= new ArrayList<>();
		
		for(SimpleEntry<ASTNode, ArrayList<VectorNode>> contextVector : contextVectorInformation) {
			ArrayList<String> tempVector = new ArrayList<>();
			for(VectorNode node : contextVector.getValue()) {
				tempVector.add(node.getVectorNodeInfo());
			}
			contextVectors.add(tempVector);
		}
		//allow duplication in one vector(e.g. [1,2,3,1,2,3] -> pattern [1,2,3] twice
		for(Map.Entry<ArrayList<String>, Integer> pattern : allSequentialPatterns.entrySet()) {
			int count = 0;
			for(ArrayList<String> vector : contextVectors) {
				for(int i = 0 ; i < vector.size(); i ++) {
					for(int j = 0 ; j < pattern.getKey().size(); j ++) {
						int tempVectorIdx = i;
						int tempPatternIdx = j;
						int correct = 0;
						while(vector.size() > pattern.getKey().size() &&
								tempVectorIdx < vector.size() &&
								tempPatternIdx < pattern.getKey().size() &&
								vector.get(tempVectorIdx).equals(pattern.getKey().get(tempPatternIdx))) {
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

	public HashMap<ArrayList<String>, Integer> 
	sortByCounting(HashMap<ArrayList<String>, Integer> patternFrequency) {
		Map<String, Integer> frequency = new HashMap<>();
		
		for(ArrayList<String> tempKey : patternFrequency.keySet()) {
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
			System.out.println(String.format("%s (%d)", key, frequency.get(key)));
			count ++;
		}
		
		return null;
	}
}
