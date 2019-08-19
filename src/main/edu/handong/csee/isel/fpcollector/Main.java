package edu.handong.csee.isel.fpcollector;

import java.io.File;
import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.evaluator.Evaluator;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;
import edu.handong.csee.isel.fpcollector.structures.ContextPattern;
import edu.handong.csee.isel.fpcollector.tpcollector.TPCollector;

public class Main {
	/*
	 * information[0] : Target Project git address
	 * information[1] : Rule Context
	 * information[2] : Output Path
	 * information[3] : Tool command
	 * information[4] : Time
	 */
	final static int TRUE_POSITIVE = 0;
	final static int FALSE_POSITIVE = 1;
	
		public static void main(String[] args) {
			FPCollector getFPSuspects = new FPCollector();
			TPCollector getTP = new TPCollector();
			ContextExtractor getFalsePositiveContext = new ContextExtractor();
			ContextExtractor getTruePositiveContext = new ContextExtractor();
			Evaluator getScore = new Evaluator();
			ArrayList<ContextPattern> truePositivePattern = new ArrayList<>();
			ArrayList<ContextPattern> falsePositivePattern = new ArrayList<>();
			
			
			String[] information  = getFPSuspects.initiate(args);
			
			if(!new File(information[2]).exists()) {
			getFPSuspects.run(information);
			} else {
				System.out.println("!!!!! ResultFile is Already exists");
			}
			
			getTP.run(information);
			
			truePositivePattern = getTruePositiveContext.run(information, TRUE_POSITIVE);
			falsePositivePattern = getFalsePositiveContext.run(information, FALSE_POSITIVE);
			
			getScore.run(truePositivePattern, falsePositivePattern);
		}
}
