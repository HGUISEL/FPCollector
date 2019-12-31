package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class DataNode extends GraphNode{
	State state;
	String name;
	
	public DataNode(ASTNode node) {
		super(node);
	}
}
