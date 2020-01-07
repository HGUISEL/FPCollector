package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class ControlNode extends GraphNode{
	ControlState state;
	
	ArrayList<GraphNode> nexts = new ArrayList<GraphNode>();
	
	public ControlNode(ASTNode node, ControlState state) {
		super(node);
		
		this.state = state;
	}
	
	public void setState(ControlState state) {
		this.state = state;
	}
	
}
