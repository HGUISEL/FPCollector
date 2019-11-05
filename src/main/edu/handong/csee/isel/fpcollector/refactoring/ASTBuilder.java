package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.SimpleName;

public class ASTBuilder {
	Info info = new Info();
	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	ArrayList<SimpleName> violatedNodeInRange = new ArrayList<>();
	
	public ASTBuilder(Info info) {
		this.info = info;
		buildAST();
	}
	
	private void buildAST() {
		JavaASTParser javaParser = new JavaASTParser(info.source);
		violatedNode = javaParser.getViolatedNames(info.name);
		
		checkInRange();
	}
	
	private void checkInRange() {
		for(SimpleName violatedNode : violatedNode) {
			if(violatedNode.getStartPosition() >= Integer.parseInt(info.start) 
					&&  violatedNode.getStartPosition() <= Integer.parseInt(info.end)) {
				violatedNodeInRange.add(violatedNode);
			} else {
				continue;
			}
		}
	}
	
	public String belongsToChecker() {
		BNF bnf= new BNF();
		
		for(SimpleName node : violatedNodeInRange) {
			bnf.getBNF(node);
		}
	}
	
}
