package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.ArrayList;
import java.util.HashMap;
import edu.handong.csee.isel.fpcollector.structures.VectorNode;

public class PatternFinder {
	public HashMap<ArrayList<Integer>, Integer> mineAllPatterns
	(ArrayList<ArrayList<VectorNode>> contextVectorInformation){
		HashMap<ArrayList<Integer>, Integer> patterns = new HashMap<>();
		int minSize = 99999;
		int maxPatternSize = -1;
		for(ArrayList<VectorNode> tempNodes : contextVectorInformation) {
			if(minSize > tempNodes.size()) {
				minSize = tempNodes.size();
			}
		}
		maxPatternSize = minSize;
		
		for(int i = 2; i<= maxPatternSize; i ++) {
			for(ArrayList<VectorNode> tempPattern : contextVectorInformation) {
				System.out.print("Nodes : [" );
				for(VectorNode tempNode : tempPattern) {
					System.out.print(tempNode.getNode().getNodeType() + ", ");
				}
				System.out.println("] ");
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
		
		System.out.println("Number of Patterns : " + patterns.size());
		
		return patterns;
	}
}
