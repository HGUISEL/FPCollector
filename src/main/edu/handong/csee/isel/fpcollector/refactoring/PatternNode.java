package edu.handong.csee.isel.fpcollector.refactoring;

import org.eclipse.jdt.core.dom.ASTNode;

public class PatternNode {
	ASTNode node;
	int count;
	
	public PatternNode (ASTNode n) {
		this.node = n;
		this.count = 0;
	}
}
