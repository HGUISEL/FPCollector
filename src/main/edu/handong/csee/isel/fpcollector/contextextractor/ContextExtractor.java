package edu.handong.csee.isel.fpcollector.contextextractor;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.contextextractor.astvector.ContextVectorGetter;
import edu.handong.csee.isel.fpcollector.contextextractor.astvector.ViolationVariableGetter;
import edu.handong.csee.isel.fpcollector.contextextractor.patternminer.PatternFinder;
import edu.handong.csee.isel.fpcollector.contextextractor.patternminer.SupportCountGetter;
import edu.handong.csee.isel.fpcollector.structures.ContextPattern;
import edu.handong.csee.isel.fpcollector.structures.DirLineErrmsgContext;
import edu.handong.csee.isel.fpcollector.structures.VectorNode;
import edu.handong.csee.isel.fpcollector.utils.Reader;
import edu.handong.csee.isel.fpcollector.utils.ReportAnalyzer;
import edu.handong.csee.isel.fpcollector.utils.Writer;

public class ContextExtractor {
	/*
	 * Input : A csv file, result of fpsuspectsgetter
	 * Output : A csv file, directory and line number and context information
	 */
	
	final static int TRUE_POSITIVE = 0;
	final static int FALSE_POSITIVE = 1;
	
	public ArrayList<ContextPattern> run(String[] info, int flag) {
		//read result file
		ArrayList<String> resultInfo = new ArrayList<>();
		//ArrayList<ArrayList<String>> lineContext = new ArrayList<>();
		String OutputPath = "";
		if(flag == FALSE_POSITIVE) {
			OutputPath = info[2];
		}
		else {
			OutputPath = info[2].split("\\.csv")[0] + "_TP.csv";
		}
		
		//DirLineErrmsgContext context;
		resultInfo = readResultFile(OutputPath);
			
		//part 1 : get AST Vector
		//find violated variable and its path
		ArrayList<String> varPath = new ArrayList<>();
		ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> contextNodeInformation = new ArrayList<>();
		ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation = new ArrayList<>();
//		HashMap<ArrayList<String>, Integer> allSequentialPatterns = new HashMap<>();
//		HashMap<ArrayList<String>, Integer> patternFrequency = new HashMap<>();
		HashMap<ArrayList<String>, Integer> contextPatterns = new HashMap<>();
		ArrayList<ContextPattern> sortedPatterns = new ArrayList<>();
		
		System.out.println("Collecting violation occurred Variable and its Path...");
		varPath = getViolationVarPath(resultInfo);
		System.out.println("\n@@@@@ Collected Successfully\n");
		System.out.println("Collecting violation occurred Method Context...");
		contextNodeInformation = getContextNode(varPath, flag);
		System.out.println("\n@@@@@ Collected Successfully\n");
		System.out.println("Vectorizing Collected Context...");
		contextVectorInformation = getContextVectorInformation(contextNodeInformation);
		System.out.println("\n@@@@@ Collected Successfully\n");
		
		//part 2 : get Frequent pattern
		//get all pattern(SP, OP, AP)
		//1) get SP
//		allSequentialPatterns = getAllPatterns(contextVectorInformation);
//		patternFrequency = getSequentialPatternFrequency(contextVectorInformation, allSequentialPatterns);
//		patternFrequency = sortByFrequency(patternFrequency);
		
		//2) get line Patterns
		contextPatterns = getPatterns(contextVectorInformation);
		
		//patternFrequency = getLinePatternFrequency(contextVectorInformation, linePatterns);
		//patternFrequency = sortByFrequency(patternFrequency);
		sortedPatterns = sortByFrequency(contextPatterns);
		
		System.out.println("\n\n");
		for(ContextPattern temp : sortedPatterns) {	
			System.out.println(temp.getPattern().getKey().toString() + " (" + temp.getPattern().getValue() + ")");
		}
		
		return sortedPatterns;
		//write a file
//			System.out.println("\n----- Start to Rearrange Data -----\n");
//			context = new DirLineErrmsgContext(lineContext, resultInfo);
//			writeContext(context, OutputPath);
//			System.out.println("@@@@@ Context Extracting Process is Completed");
	}
	
	public ArrayList<String> readResultFile(String path){
		Reader reader = new Reader();
		ArrayList<String> resultInfo = new ArrayList<>();
		
		resultInfo = reader.readResult(path);
		return resultInfo;	
	}
	
	public ArrayList<ArrayList<String>> getContextUsingBlame(ArrayList<String> result, String name){
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		lineInfo = analyzer.getCtxUsingBlame(result, name);
		
		return lineInfo;
	}
	
	public ArrayList<ArrayList<String>> getContextUsingFile(ArrayList<String> result, String name){
		ArrayList<ArrayList<String>> lineInfo = new ArrayList<>();
		ReportAnalyzer analyzer = new ReportAnalyzer();
		
		lineInfo = analyzer.getCtxUsingFile(result, name);
		
		return lineInfo;
	}
	
	public void writeContext(DirLineErrmsgContext context, String path) {
		Writer writer = new Writer();
		writer.writeContextsForDFA(context, path);
	}
	
	public ArrayList<String> getViolationVarPath(ArrayList<String> result){
		ArrayList<String> varPath = new ArrayList<>();
		ViolationVariableGetter vVGetter = new ViolationVariableGetter();
		
		varPath = vVGetter.getViolationVariable(result);
		
		return varPath;
	}
	
	public ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> getContextNode(ArrayList<String> varPath, int flag){
		ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> contextVector = 
				new ArrayList<>();
		ContextVectorGetter ctxGetter = new ContextVectorGetter();
		contextVector = ctxGetter.getContextNode(varPath, flag);
		
		return contextVector;
	}
	
	public ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> 
	getContextVectorInformation(ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> nodeInfo){
		ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVector = new ArrayList<>();
		ContextVectorGetter vectorGetter = new ContextVectorGetter();
		
		contextVector = vectorGetter.getContextVector(nodeInfo);
		
		return contextVector;
	}
	
	public ArrayList<ArrayList<VectorNode>> getContextFrequency
	(ArrayList<ArrayList<VectorNode>> contextVectorInformation){
		ArrayList<ArrayList<VectorNode>> frequencyPattern = new ArrayList<>();
		SupportCountGetter supportCountGetter = new SupportCountGetter();

		frequencyPattern = supportCountGetter.getSupportCount(contextVectorInformation);
		
		return frequencyPattern;
	}
	
	private HashMap<ArrayList<String>, Integer> getPatterns(
			ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation) {
		HashMap<ArrayList<String>, Integer> linePatterns = new HashMap<>();
		PatternFinder finder = new PatternFinder();
		
		linePatterns = finder.minePatterns(contextVectorInformation);
	
		return linePatterns;
	}

	public HashMap<ArrayList<String>, Integer> getAllPatterns
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation) {
		HashMap<ArrayList<String>, Integer> sequentialPatterns = new HashMap<>();
		
		PatternFinder finder = new PatternFinder();	
		
		sequentialPatterns = finder.mineAllSequentialPatterns(contextVectorInformation);
		
		return sequentialPatterns;
	}
	
	public HashMap<ArrayList<String>, Integer> getSequentialPatternFrequency
	(ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVectorInformation, 
	HashMap<ArrayList<String>, Integer> allSequentialPatterns) {
		PatternFinder finder = new PatternFinder();
		HashMap<ArrayList<String>, Integer> frequency = new HashMap<>();
		
		frequency = finder.getSPFrequency(contextVectorInformation, allSequentialPatterns);	
		
		return frequency;
	}
	
	public ArrayList<ContextPattern> sortByFrequency(
			HashMap<ArrayList<String>, Integer> patternFrequency) {
		ArrayList<ContextPattern> patterns = new ArrayList<>();
		PatternFinder finder = new PatternFinder();
		
		patterns = finder.sortByCounting(patternFrequency);
		
		return patterns;
	}
}
