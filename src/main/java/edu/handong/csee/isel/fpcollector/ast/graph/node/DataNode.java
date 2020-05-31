package edu.handong.csee.isel.fpcollector.ast.graph.node;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fpcollector.ast.graph.node.state.VarCondition;
import edu.handong.csee.isel.fpcollector.ast.graph.node.state.VarState;
import edu.handong.csee.isel.fpcollector.ast.graph.node.state.VarType;

public class DataNode extends GraphNode{
	VarState state;
	VarCondition condition;
	VarType type;
	ASTNode from;
	String varName;
	
	public DataNode(ASTNode node, int level) {
		super(node, level);
	}
	
	public void setState(VarState state) {
		this.state = state;
	}
	
	public void setCondition(VarCondition condition) {
		this.condition = condition;
	}
	
	public void setType(VarType type) {
		this.type = type;
	}
	
	public void setFrom(ASTNode from) {
		this.from = from;
	}
	
}
