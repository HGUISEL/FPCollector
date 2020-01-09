package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	public ASTNode node;
	public ControlNode parent;
	int level;
	public GraphNode(ASTNode node, int level) {
		this.node = node;
		this.level = level;
	}
	public ASTNode getNode() {
		return node;
	}
}
