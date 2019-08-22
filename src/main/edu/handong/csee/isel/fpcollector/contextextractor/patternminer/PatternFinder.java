package edu.handong.csee.isel.fpcollector.contextextractor.patternminer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.google.common.collect.Sets;

import edu.handong.csee.isel.fpcollector.structures.ContextPattern;
import edu.handong.csee.isel.fpcollector.structures.VectorNode;

public class PatternFinder {
	public HashMap<ArrayList<String>, Integer> minePatterns
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation){
		HashMap<ArrayList<String>, Integer> patterns = new HashMap<>();
		ArrayList<ArrayList<String>> linePatterns = new ArrayList<>();
		ArrayList<ArrayList<String>> blockPatterns = new ArrayList<>();

		
		//Among contextVecotInformation, get line which include violated variable and its Pattern
		//1) get All Sequential Nodes until node's start line is same with SimpleName's start line
		//2) don't care about number of nodes just get All.
		
		//lineContext
		int total = contextVectorInformation.size();
		int progress = 0;
		System.out.println("Collecting Line Patterns...");
		for(SimpleEntry<ASTNode, ArrayList<VectorNode>> tempPattern: contextVectorInformation) {
			progress ++;
			if(progress == 1) {
				System.out.print("0%...");
			}
			if(progress == total / 4 ) {
				System.out.print("25%...");
			}
			if(progress == total / 2) {
				System.out.print("50%...");
			}
			if(progress == (total *3) / 4) {
				System.out.print("75%...");
			}
			if(progress == total) {
				System.out.print("done...");
			}
			ASTNode violationOccurrence = tempPattern.getKey();
			ArrayList<String> lineContext = new ArrayList<>();
			ArrayList<String> blockContext = new ArrayList<>();
			ASTNode root = violationOccurrence.getRoot();
			CompilationUnit cUnit = (CompilationUnit) root;
			
			for(VectorNode temp : tempPattern.getValue()) {
				if(temp.getNode().getClass() == violationOccurrence.getClass() &&
						temp.getNode().toString().equals(violationOccurrence.toString())) {
					if(lineContext.size() != 0) {
						linePatterns.add(lineContext);
						blockPatterns.add(blockContext);
						lineContext = new ArrayList<>();
						blockContext = new ArrayList<>();
					}
					violationOccurrence = temp.getNode();
				}
				
				if(cUnit.getLineNumber(violationOccurrence.getStartPosition()) == 
						cUnit.getLineNumber(temp.getNode().getStartPosition())) {
					lineContext.add(temp.getVectorNodeInfo());
				}
				blockContext.add(temp.getVectorNodeInfo());
//				ArrayList<String> test = new ArrayList<>();
//				test.add("SimpleName");
//				test.add("ClassInstanceCreation");
//				test.add("ReturnStatement");
//				if(blockContext.containsAll(test)) {
//					System.out.println("Pause");
//					System.out.println(root);
//				}
			}
			
			if(lineContext.size() > 2 ) {
				linePatterns.add(lineContext);
			}
		}
		
		for(ArrayList<String> pattern : linePatterns) {
			if(pattern.size() > 2) {
				if(patterns.containsKey(pattern)) {
					patterns.put(pattern, Integer.sum(patterns.get(pattern), 1)); 
				}
				else {
					patterns.put(pattern, 1);
				}
			}
		}
		
		//block Pattern
		System.out.println("\nCollecting Block Patterns...");
		progress = 0;
		total = blockPatterns.size();
		for(ArrayList<String> blockPattern : blockPatterns) {
			progress ++;
			if(progress == 1) {
				System.out.print("0%...");
			}
			if(progress == total / 4 ) {
				System.out.print("25%...");
			}
			if(progress == total / 2) {
				System.out.print("50%...");
			}
			if(progress == (total *3) / 4) {
				System.out.print("75%...");
			}
			if(progress == total) {
				System.out.print("done...");
			}
			for(ArrayList<String> linePattern : linePatterns) {
				if(blockPattern.containsAll(linePattern)) {
					ArrayList<ArrayList<String>> combinationContext = new ArrayList<>();
					ArrayList<String> tempPattern = new ArrayList<>();
					tempPattern.clear(); 
					tempPattern.addAll(linePattern);
					blockPattern.removeAll(linePattern);
					
					combinationContext = combination(blockPattern);
					
					for(ArrayList<String>  tempCombination: combinationContext) {
						tempPattern.addAll(tempCombination);
						if(tempPattern.size() > 2) {
							if(!patterns.containsKey(tempPattern)) {
								patterns.put(tempPattern, 1);
							}
							else {
								patterns.put(tempPattern, Integer.sum(patterns.get(tempPattern), 1));
							}
						}
						tempPattern.clear();
						tempPattern.addAll(linePattern);
					}			
					break;
				}
			}
		}
		/*
		HashMap<ArrayList<String>, Integer> tempPatterns = new HashMap<>();
		tempPatterns.putAll(patterns);
		Iterator<ArrayList<String>> patternChecker = tempPatterns.keySet().iterator();
		
		//Minimum Support Count is size of Patterns' 1%
		while(patternChecker.hasNext()) {
			ArrayList<String> key = patternChecker.next();
			if(tempPatterns.get(key) < patterns.size() / 100) {
				System.out.println("Pattern : " + key + " Frequency : " + patterns.get(key));
			}
		}*/
		
		System.out.println("\n" + patterns.size());
		
		return patterns;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ArrayList<String>> combination (ArrayList<String> blockPattern){
		ArrayList<ArrayList<String>> combinationResult = new ArrayList<>();
		ArrayList<SimpleEntry<Integer, String>> order = new ArrayList<>();
		int idx = 0;
		
		for(String temp : blockPattern) {
			SimpleEntry<Integer, String> ordering = new SimpleEntry<>(idx, temp);
			order.add(ordering);
			idx++;
		}
		for(Set<SimpleEntry<Integer, String>> sets : Sets.powerSet(Sets.newHashSet(order))) {
			ArrayList<SimpleEntry<Integer, String>> combinations = new ArrayList<>();
			ArrayList<String> combinationPatterns = new ArrayList<>();
			for(Object temp : sets.toArray()) {
				combinations.add((SimpleEntry<Integer, String>) temp);
			}
			
			Collections.sort(combinations, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((SimpleEntry<Integer, String>) o1).getKey() - ((SimpleEntry<Integer, String>) o2).getKey();
				}
			});
			
			for(SimpleEntry<Integer, String> tempNode : combinations) {
				combinationPatterns.add(tempNode.getValue());
			}
			
			if(combinations.size() > 0) {
				combinationResult.add(combinationPatterns);
			}
		}
		
		return combinationResult;
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

	public ArrayList<ContextPattern> 
	sortByCounting(HashMap<ArrayList<String>, Integer> patternFrequency) {
		Map<String, Integer> frequency = new HashMap<>();
		ArrayList<ContextPattern> frequentPattern = new ArrayList<>();
		System.out.println("Sorting Patterns...");
		for(ArrayList<String> tempKey : patternFrequency.keySet()) {
			Integer tempValue = patternFrequency.get(tempKey);
			String keys = tempKey.toString();
			frequency.put(keys, tempValue);
		}
		
		List<String> frequencyList = new ArrayList<>(frequency.keySet());
		Collections.sort(frequencyList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				if(frequency.get(o1) == null && frequency.get(o2) != null) return 1;
				else if(frequency.get(o2) == null && frequency.get(o1) != null) return -1;
				else if(frequency.get(o1) == null && frequency.get(o2) == null) return 0;
				return frequency.get(o2).compareTo(frequency.get(o1));
				
			}
		});
		
		int ranking = 20;
		int count = 0;
		for(String key : frequencyList) {
			if(count == ranking) {
				break;
			}
			ContextPattern tempPattern = new ContextPattern(key, frequency.get(key));
			frequentPattern.add(tempPattern);
			count ++;
		}
		System.out.println("@@@@@ Sorting Complete");
		return frequentPattern;
	}
}
