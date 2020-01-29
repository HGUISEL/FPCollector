package edu.handong.csee.isel.fpcollector.ast.graph.node;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.ast.graph.node.state.CntrlState;

public class RootNode extends ControlNode{

	public RootNode(ASTNode node, int level) {
		super(node, CntrlState.S, level);
	}
}
