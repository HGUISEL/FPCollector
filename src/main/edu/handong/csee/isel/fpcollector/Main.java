package edu.handong.csee.isel.fpcollector;

/*
 * To-do
 * 2. Use True Positive pattern(before being changed)
 * 3. Use not only standardization, but also Normalization
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.contextextractor.patternminer.PatternAdjustment;
import edu.handong.csee.isel.fpcollector.evaluator.Evaluator;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;
import edu.handong.csee.isel.fpcollector.refactoring.GitCheckout;
import edu.handong.csee.isel.fpcollector.refactoring.GitClone;
import edu.handong.csee.isel.fpcollector.refactoring.Input;
import edu.handong.csee.isel.fpcollector.refactoring.RunTool;
import edu.handong.csee.isel.fpcollector.structures.ContextPattern;
import edu.handong.csee.isel.fpcollector.tpcollector.TPCollector;

/**
 * 
 * False Positive Collector's main function.
 * 
 * <p> First, get information about target project's git address,<br>
 * rule context, result file's output path, tool command, and time.<br>
 * Then, Collect False Positive Suspects, and get True Positives.<br>
 * After that, collect True Positive's context and FalsePositive's context.<br>
 * Lastly, assign Score to each patterns and print top 5 of them out.<br>
 * 
 * <p> Input File<br>
 * 1) Path of a text file which include a Target Project's git address<br>
 * 2) Path of a text file which include a Tool's Rule context<br>
 * 3) Path of a text file which include Output file path<br>
 * 4) Path of a text file which include Tool Command<br>
 * 5) Path of a text file which include Time<br>
 * <br>
 * For example,<br>
 * <pre> 1) https://github.com/spring-projects/dubbo.git
 * 2) category/java/errorprone.xml/DataflowAnomalyAnalysis
 * 3) ./Result.csv
 * 4) /Users/yoonhochoi/Documents/ISEL/pmd/pmd-bin-6.15.0/bin/run.sh pmd 
 * 5) 2019-07-21
 * </pre>
 * 
 * @author yoonhochoi
 * @version 1.0
 * @since 1.0
 * @see edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector
 * @see edu.handong.csee.isel.fpcollector.tpcollector.TPCollector
 * @see edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor
 * @see edu.handong.csee.isel.fpcollector.evaluator.Evaluator
 *
 */

public class Main {
	
	final static int FAILED = 0;
	final static int SUCCESS = 1;
	final static int CLONE = 2;
	final static int CHECKOUT = 3;
	
	public static void main(String[] args) {
			
//1. Preparing for Collecting False Positive Candidates
			
		//1. read input file
		Input input = new Input();
		if(input.getInput(args) == FAILED) {
			System.out.println("Wrong Input : Please, Input Properly.");
			System.exit(-1);
		}
			
		//2. git clone the project
		GitClone gitClone = new GitClone();
		gitClone.clone(input.gitAddress, input.projectName);
			
		//3. run pmd
		RunTool runPMD = new RunTool();
		runPMD.getReport(CLONE, input.toolCommand, gitClone.clonedPath, input.rule, input.projectName);
			
		//4. clone the cloned project and checkout to a year a go
		GitCheckout gitCheckout = new GitCheckout();
		gitCheckout.checkout(gitClone.clonedPath, input.time, input.projectName);
			
		//5. run pmd to checkouted project
		runPMD.getReport(CHECKOUT, input.toolCommand, gitCheckout.checkoutPath, input.rule, input.projectName);
			
		System.out.println("Step 1 CLEAR");
			
//2. Collecting False Positive Candidates
			
			//1. read report of present project
			//2. read report of past project
			//3. compare is there anything same
			//4. if same, get them as FPC
			
//3. Get Pattern of the FPC
			
			//1. 
			
			
			
			FPCollector getFPSuspects = new FPCollector();
			TPCollector getTP = new TPCollector();
			
			ContextExtractor getFalsePositiveContext = new ContextExtractor();
			ContextExtractor getTruePositiveContext = new ContextExtractor();
			ContextExtractor getBuggyContext = new ContextExtractor();
			Evaluator getScore = new Evaluator();
			PatternAdjustment adjust = new PatternAdjustment();
			HashMap<String, Integer> tpCodeContextNodes =new HashMap<>();
			HashMap<String, Integer> fpCodeContextNodes =new HashMap<>();
			HashMap<String, Integer> buggyCodeContextNodes =new HashMap<>();
		
			ArrayList<ContextPattern> truePositivePattern = new ArrayList<>();
			ArrayList<ContextPattern> falsePositivePattern = new ArrayList<>();
			ArrayList<ContextPattern> buggyPattern = new ArrayList<>();
			
			String[] information  = getFPSuspects.initiate(args);
			
			if(!new File(information[2]).exists()) {
				getFPSuspects.run(information);
			} else {
				System.out.println("!!!!! FP Result File is Already Exists");
			}
			
			if(!new File(information[2].split("\\.csv")[0] + "_TP.csv").exists()) {
				getTP.run(information);
			} else {
				System.out.println("!!!!! TP Result File is Already Exists");
			}
			
//			tpCodeContextNodes = getTruePositiveContext.getPattern(information, TRUE_POSITIVE);
//			buggyCodeContextNodes = getBuggyContext.getPattern(information, BUGGY);
//			fpCodeContextNodes = getFalsePositiveContext.getPattern(information, FALSE_POSITIVE);
//			
//			for(Map.Entry<String, Integer> temp : fpCodeContextNodes.entrySet()) {
//			//	System.out.println(temp.getKey());
//			}
//			
////			tpCodeContextNodes = adjust.adjustTP(tpCodeContextNodes, buggyCodeContextNodes);
////			buggyCodeContextNodes = adjust.adjustBP(tpCodeContextNodes, buggyCodeContextNodes);
//			
//			truePositivePattern = getTruePositiveContext.sortPattern(tpCodeContextNodes, TRUE_POSITIVE);
//			buggyPattern = getBuggyContext.sortPattern(buggyCodeContextNodes, BUGGY);
//			falsePositivePattern = getFalsePositiveContext.sortPattern(fpCodeContextNodes, FALSE_POSITIVE);
//			
//			getScore.run(truePositivePattern, falsePositivePattern, buggyPattern);
		}
}
