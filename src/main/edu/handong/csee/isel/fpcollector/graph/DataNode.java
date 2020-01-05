package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class DataNode extends GraphNode{
	VarState state;
	
	public DataNode(ASTNode node, VarState state) {
		super(node);
		
		this.state = state;
	}
}
