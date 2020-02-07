package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	public ASTNode node;
	public ControlNode parent;
	public int level;
	public String path;
	public GraphNode(ASTNode node, int level, String path) {
		this.node = node;
		this.level = level;
		this.path = path;
	}
	public GraphNode(ASTNode node, int level) {
		this.node = node;
		this.level = level;		
	}
	public ASTNode getNode() {
		return node;
	}
}
