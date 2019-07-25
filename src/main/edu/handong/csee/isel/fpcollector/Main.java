/* To-do
 * 1) get related source code
 * 2) make a AST tree for the source code
 * 3) make class which could store base-line and other information
 * 4) read AST and store information in the 3) class
 * 5) 4)'s information means that get all parents nodes and children nodes of base-line
 * 6)----------------------------------------------------------------------------------
 * 7) data mining using frequency
 * 8) get common context as a number
 * 9) translate the number to class name
 * 
 * What I miss
 * 1) generalize method
 * 2) avoid using nested data structure
 * 3) find external library which could simplify my code
 */
package edu.handong.csee.isel.fpcollector;

import java.io.File;

import edu.handong.csee.isel.fpcollector.contextextractor.ContextExtractor;
import edu.handong.csee.isel.fpcollector.fpsuspectsgetter.FPCollector;

public class Main {
	/*
	 * information[0] : Target Project git address
	 * information[1] : Rule Context
	 * information[2] : Output Path
	 * information[3] : Tool command
	 * information[4] : Time
	 */
		public static void main(String[] args) {
			FPCollector getFPSuspects = new FPCollector();
			ContextExtractor getContext = new ContextExtractor();
			String[] information  = getFPSuspects.initiate(args);
			
			if(!new File(information[2]).exists()) {
			getFPSuspects.run(information);
			}
			else {
				System.out.println("!!!!! ResultFile is Already exists");
			}
			getContext.run(information);
		}
}
