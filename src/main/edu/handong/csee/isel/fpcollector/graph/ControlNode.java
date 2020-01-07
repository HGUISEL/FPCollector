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
	
	public void printInfo() {
		System.out.println(this.node);
		printChildren(this);
		System.out.println();
		System.out.println("========================================================================================");
	}
	
	private void printChildren(ControlNode n) {
		for (int i = 0; i < n.nexts.size(); i++) {
			GraphNode n_ =  n.nexts.get(i);
			System.out.println(n_.node.getClass().getSimpleName());
//			if (n_ instanceof DataNode)
//				System.out.println("(D) n: " + n_.node + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).from);
//			else System.out.println("(C) n: " + n_.node + ", state : " + ((ControlNode)n_).state);
			if (n_ instanceof ControlNode) {
				printChildren((ControlNode)n_);
			}
		}
	}
	
	
	
}
