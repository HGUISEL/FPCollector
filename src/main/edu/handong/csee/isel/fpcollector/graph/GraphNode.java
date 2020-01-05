package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	public ASTNode node;
	public ControlNode parent;
	
	public GraphNode(ASTNode node) {
		this.node = node;
	}
	public ASTNode getNode() {
		return node;
	}
}
