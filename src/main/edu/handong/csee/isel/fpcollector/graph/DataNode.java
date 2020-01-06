package edu.handong.csee.isel.fpcollector.graph;

import org.eclipse.jdt.core.dom.ASTNode;

public class DataNode extends GraphNode{
	VarState state;
	VarState inCondition;
	ASTNode from;
	
	public DataNode(ASTNode node) {
		super(node);
	}
	
	public void setState(VarState state) {
		this.state = state;
	}
	
	public void setInCondition(VarState inCondition) {
		this.inCondition = inCondition;
	}
	
	public void setFrom(ASTNode from) {
		this.from = from;
	}
}
