package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import edu.handong.csee.isel.fpcollector.refactoring.MethodAST;

public class GraphBuilder {
	public ArrayList<GraphNode> getGraph(ArrayList<MethodAST> methodASTs){
		ArrayList<GraphNode> graphs = new ArrayList<>();
		
		for (MethodAST mAST : methodASTs) {
			GraphNode tempRoot = new ControlNode(mAST.asts.get(0));
			for(ASTNode node : mAST.asts) {
				if(node instanceof MethodDeclaration) {
					continue;
				}
				//controlNode
				else if((tempRoot != null) && (node instanceof IfStatement ||
						node instanceof DoStatement ||
						node instanceof SwitchStatement ||						
						node instanceof ContinueStatement ||
						node instanceof BreakStatement ||
						node instanceof TryStatement ||
						node instanceof ForStatement ||						
						node instanceof EnhancedForStatement ||
						node instanceof ConditionalExpression ||
						node instanceof SwitchCase)
//						temp.getParent() instanceof MethodDeclaration
						&& !(node instanceof Block)
						) {
					if(node.getParent().getParent() == tempRoot.getNode()) {
						tempRoot.addNode(node);
					}
				}
				//terminateNode
				else if((tempRoot != null) && (
						node instanceof ReturnStatement ||												
						node instanceof ThrowStatement)
//						temp.getParent() instanceof MethodDeclaration
						&& !(node instanceof Block)
						) {
					if(node.getParent().getParent() == tempRoot.getNode()) {
						tempRoot.addNode(node);
					}
				}
				else if (node instanceof SimpleName){
					
				}
			}
			graphs.add(tempRoot);
		}
		
		return graphs;
	}
}
