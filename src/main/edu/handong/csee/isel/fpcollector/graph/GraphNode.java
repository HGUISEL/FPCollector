package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	public ASTNode node;
	ArrayList<ASTNode> nexts = new ArrayList<ASTNode>();
	
	public GraphNode(ASTNode node) {
		this.node = node;
	}
	public ASTNode getNode() {
		return node;
	}
	public void addNode(ASTNode node) {
		nexts.add(node);
	}
}
