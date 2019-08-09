package edu.handong.csee.isel.fpcollector.structures;

import org.eclipse.jdt.core.dom.*;

public class VectorNode {
	private ASTNode node;
	private String info = "";
	
	public VectorNode(ASTNode node){
		this.node = node;
		if(node instanceof VariableDeclarationFragment){
			if(((VariableDeclarationFragment) node).getInitializer() != null) {
				this.info = "DecInitializer(" +
						((VariableDeclarationFragment) node).getInitializer().getClass().toString().split("dom.")[1] +
						")";
			} else {
				this.info = "DecInitializer(Not Initialized)";
			}
		} 
		else if(node instanceof Assignment) {
			//StartHere
			//StartHere
			//StartHere
			//StartHere
			//StartHere
			//StartHere
			if(((Assignment) node).getRightHandSide().toString().matches
					(((Assignment) node).getLeftHandSide().toString())) {
			this.info = "AssigmentWithSame";
			} else {
				System.out.println(((Assignment) node).getLeftHandSide().toString());
				System.out.println(((Assignment) node).getRightHandSide().toString());
				this.info = "AssigmentWithDiff";
			}
		}
		else if(node instanceof SingleVariableDeclaration) {
			this.info = "SingleDecType(" + ((SingleVariableDeclaration) node).getType() + ")";
		}
		else if(node instanceof QualifiedName) {
			this.info = "QNWith(" + ((QualifiedName) node).toString().split("\\.")[1] + ")";
		}
		else if(node instanceof InfixExpression || node instanceof MethodInvocation ||
				node instanceof ExpressionStatement || node instanceof ClassInstanceCreation) {
			this.info = "Useless";
		} 
		else {
		this.info = node.getClass().toString().split("jdt.core.dom.")[1];
		}
	}
	
	public ASTNode getNode() {
		return this.node;
	}
	
	public String getVectorNodeInfo() {
		return info;
	}
}
