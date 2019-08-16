package edu.handong.csee.isel.fpcollector;

import java.io.File;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;
import edu.handong.csee.isel.tpcollector.TPCollector;

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
			
			String[] information  = getFPSuspects.initiate(args);
			
			if(!new File(information[2]).exists()) {
			getFPSuspects.run(information);
			}
			else {
				System.out.println("!!!!! ResultFile is Already exists");
			}
			getTP.run(information);
			getTruePositiveContext.run(information, TRUE_POSITIVE);
			getFalsePositiveContext.run(information, FALSE_POSITIVE);
			
		}
}
