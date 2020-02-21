package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.refactoring.Info;

public class GraphNode {
	public ASTNode node;
	public ControlNode parent;
	public int level;
	public String path;
	public ArrayList<ASTNode> varNodes = new ArrayList<>();
	public ArrayList<ASTNode> fieldNodes = new ArrayList<>();
	
//	public Info info;
	public GraphNode(ASTNode node, int level, Info info) {
		this.node = node;
		this.level = level;
		this.path = info.path;
		this.varNodes = info.varNodes;
		this.fieldNodes = info.fieldNodes;
		
//		this.info = info;
	}
	public GraphNode(ASTNode node, int level) {
		this.node = node;
		this.level = level;		
	}
	public ASTNode getNode() {
		return node;
	}
}
