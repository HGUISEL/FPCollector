package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	ASTNode node;
	ArrayList<ASTNode> nexts = new ArrayList<ASTNode>();
}
