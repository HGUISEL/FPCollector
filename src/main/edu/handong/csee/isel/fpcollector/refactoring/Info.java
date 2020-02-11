package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;

public class Info {
	public String source;
	public ArrayList<ASTNode> varNodes = new ArrayList<>();
	public ArrayList<ASTNode> fieldNodes = new ArrayList<>();
	public ArrayList<String> varNames = new ArrayList<>();
	public ArrayList<String> fieldNames = new ArrayList<>();
	public String start;
	public String end;
	public String path;
	
	
	public void nodesToStrings() {
		for(ASTNode node : varNodes) {
			if(node instanceof SimpleName)
				varNames.add(((SimpleName) node).getIdentifier());			
		}
		
		for(ASTNode node: fieldNodes) {
			if(node instanceof SimpleName)
				fieldNames.add(((SimpleName) node).getIdentifier());
			else if(node instanceof ThisExpression)
				fieldNames.add("this");			
		}
	}
	
	public void printInfo() {
		System.out.println("source: " + this.source);
		if (this.varNames.size() >= 1)
			System.out.println("varName0: " + this.varNames.get(0));
		System.out.println("start: " + this.start);
		System.out.println("end: " + this.end);
	}
}
