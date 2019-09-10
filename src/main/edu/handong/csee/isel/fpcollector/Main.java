package edu.handong.csee.isel.fpcollector;

/*
 * To-do
 * 2. Use True Positive pattern(before being changed)
 * 3. Use not only standardization, but also Normalization
 */

import java.io.File;
import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.contextextractor.patternminer.PatternAdjustment;
import edu.handong.csee.isel.fpcollector.evaluator.Evaluator;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;
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
	final static int TRUE_POSITIVE = 0;
	final static int FALSE_POSITIVE = 1;
	final static int BUGGY = 2;
		public static void main(String[] args) {
			FPCollector getFPSuspects = new FPCollector();
			TPCollector getTP = new TPCollector();
			
			ContextExtractor getFalsePositiveContext = new ContextExtractor();
			ContextExtractor getTruePositiveContext = new ContextExtractor();
			ContextExtractor getBuggyContext = new ContextExtractor();
			Evaluator getScore = new Evaluator();
			PatternAdjustment adjust = new PatternAdjustment();
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
			
			truePositivePattern = getTruePositiveContext.run(information, TRUE_POSITIVE);
			falsePositivePattern = getFalsePositiveContext.run(information, FALSE_POSITIVE);
			buggyPattern = getBuggyContext.run(information, BUGGY);
			truePositivePattern = adjust.adjustTP(truePositivePattern, falsePositivePattern, buggyPattern);
			falsePositivePattern = adjust.adjustTP(truePositivePattern, falsePositivePattern, buggyPattern);
			buggyPattern = adjust.adjustTP(truePositivePattern, falsePositivePattern, buggyPattern);
			
			getScore.run(truePositivePattern, falsePositivePattern, buggyPattern);
		}
}
