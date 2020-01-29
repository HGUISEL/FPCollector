package edu.handong.csee.isel.fpcollector.ast.graph.node;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.handong.csee.isel.fpcollector.ast.graph.node.state.CntrlState;
import edu.handong.csee.isel.fpcollector.ast.graph.node.state.CntrlType;

public class ControlNode extends GraphNode{
	CntrlState state;
	CntrlType type;
	
	ArrayList<GraphNode> nexts = new ArrayList<GraphNode>();

	public ControlNode(ASTNode node, CntrlState state, int level) {
		super(node, level);
		
		this.state = state;
	}
	
	public void setState(CntrlState state) {
		this.state = state;
	}
	
	public void setType(CntrlType type) {
		this.type = type;
	}
	
	public void printInfo() {
		System.out.println(this.node);
		printChildren(this);
		System.out.println("========================================================================================");
	}
	
	private void printChildren(ControlNode n) {
		for (int i = 0; i < n.nexts.size(); i++) {
			GraphNode n_ =  n.nexts.get(i);
			if (n_ instanceof DataNode) {
				for(int k = 0 ; k < n.level; k ++) {
					System.out.printf("\t");
				}
				if(n_.node instanceof SimpleName)
					System.out.println("(D) n: " + n_.node.getClass().getSimpleName() + "( "+ n_.node +" )" +  ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).condition + " " + ((DataNode)n_).type + " " + ((DataNode)n_).from);
				else
					System.out.println("(D) n: " + n_.node.getClass().getSimpleName()  + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).condition + " " + ((DataNode)n_).from);
			}
			else {
				for(int k = 0 ; k < n.level; k ++) {
					System.out.printf("\t");
				}
				System.out.println("(C) n: " + n_.node.getClass().getSimpleName() + /*"( "+ ("" + n_.node).split("\n")[0] +" )" +*/ ", state : " + ((ControlNode)n_).state + " " + ((ControlNode)n_).type);
			}
			if (n_ instanceof ControlNode) {
				printChildren((ControlNode)n_);
			}
		}
	}
	

}
