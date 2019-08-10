package edu.handong.csee.isel.fpcollector.structures;

import org.eclipse.jdt.core.dom.*;

public class VectorNode {
	private ASTNode node;
	private String info = "";
	private String spec = "";
	
	public VectorNode(ASTNode node){
		this.node = node;
		this.info = node.getClass().toString().split("jdt.core.dom.")[1];
		
		if(node instanceof VariableDeclarationFragment){
			if(((VariableDeclarationFragment) node).getInitializer() != null) {
				this.spec =	((VariableDeclarationFragment) node).getInitializer().getClass().toString().split("dom.")[1];
			} else {
				this.spec = "Not Initialized";
			}
		} 
		else if(node instanceof Assignment) {
			if(((Assignment) node).getRightHandSide().toString().matches
					(".+\\b" + ((Assignment) node).getLeftHandSide().toString() + "\\b.+")) {
			this.spec = "AssigmentWithSame";
			} else {
				this.spec = "AssigmentWithDiff";
			}
		}
		else if(node instanceof SingleVariableDeclaration) {
			this.spec = "" + ((SingleVariableDeclaration) node).getType();
		}
		else if(node instanceof QualifiedName) {
			this.spec =((QualifiedName) node).toString().split("\\.")[1] + ")";
		}
		else if(node instanceof InfixExpression || node instanceof MethodInvocation ||
				node instanceof ExpressionStatement || node instanceof ClassInstanceCreation) {
			this.spec = "Useless";
		} 
		else {
		this.spec = "";
		}
	}
	
	public ASTNode getNode() {
		return this.node;
	}
	
	public String getVectorNodeInfo() {
		return info;
	}
	
	public String getVectorNodeSpec() {
		return spec;
	}
}
