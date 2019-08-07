package edu.handong.csee.isel.fpcollector.structures;

import org.eclipse.jdt.core.dom.ASTNode;

public class VectorNode {
	private ASTNode node;
	private int supportCount = 0;
	
	public VectorNode(ASTNode node){
		this.node = node;
		this.supportCount = 0;
	}
	
	public ASTNode getNode() {
		return this.node;
	}
	
	public int getSupportCount() {
		return supportCount;
	}
}
