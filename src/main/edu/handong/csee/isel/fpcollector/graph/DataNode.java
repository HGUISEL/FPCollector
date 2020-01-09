package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class DataNode extends GraphNode{
	VarState state;
	VarState inCondition;
	VarState type;
	ASTNode from;
	
	public DataNode(ASTNode node, int level) {
		super(node, level);
	}
	
	public void setState(VarState state) {
		this.state = state;
	}
	
	public void setInCondition(VarState inCondition) {
		this.inCondition = inCondition;
	}
	
	public void setType(VarState type) {
		this.type = type;
	}
	
	public void setFrom(ASTNode from) {
		this.from = from;
	}
}
