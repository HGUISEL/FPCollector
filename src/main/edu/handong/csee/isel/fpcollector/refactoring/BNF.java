package edu.handong.csee.isel.fpcollector.refactoring;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BNF {
	public String bnf = "";
	
	public void getBNF(SimpleName node){
		for(ASTNode tempNode = node; tempNode instanceof TypeDeclaration; tempNode = node.getParent()) {
			
		}
	}
}
