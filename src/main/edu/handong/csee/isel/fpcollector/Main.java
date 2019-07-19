package edu.handong.csee.isel.fpcollector;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;

public class Main {
	/*
	 * information[0] : Target Project git address
	 * information[1] : Rule Context
	 * information[2] : Output Path
	 * information[3] : Tool command
	 */
		public static void main(String[] args) {
			FPCollector getFPSuspects = new FPCollector();
			ContextExtractor getContext = new ContextExtractor();
			
			String[] information  = getFPSuspects.initiate(args);
			
			getFPSuspects.run(information);
			getContext.run(information[3]);
		}
}
