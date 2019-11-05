package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BNFChecker {
	Info info = new Info();
	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	ArrayList<SimpleName> violatedNodeInRange = new ArrayList<>();
	
	public BNFChecker(Info info) {
		this.info = info;
		buildAST();
	}
	
	private void buildAST() {
		JavaASTParser javaParser = new JavaASTParser(info.source);
		violatedNode = javaParser.getViolatedNames(info.varName);
		
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
	
	public void getBNF(SimpleName node){
		for(ASTNode tempNode = node; tempNode instanceof TypeDeclaration; tempNode = node.getParent()) {
			
		}
	}
	
	
	
}
