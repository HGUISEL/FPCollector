package edu.handong.csee.isel.fpcollector.ast.graph;

import edu.handong.csee.isel.fpcollector.ast.parser.JavaASTParser;
import edu.handong.csee.isel.fpcollector.refactoring.Info;

public class GraphBuilder {
	
	public void run(Info info) {
		JavaASTParser parser = new JavaASTParser();
		
		parser.run(info);
		
	}
}
