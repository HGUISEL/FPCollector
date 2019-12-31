package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class ControlNode extends GraphNode{
	State state;
	
	public ControlNode(ASTNode node) {
		super(node);
	}
	
}
