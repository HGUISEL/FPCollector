package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class BNFChecker {
	Info info = new Info();
//	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	public ArrayList<MethodAST> mASTs = new ArrayList<MethodAST>();
	ArrayList<PatternNode> patterns = new ArrayList<>();
	
	public void run(Info info, PatternVector patternVector) {
		this.info = info;
		buildAST(patternVector);
	}
	
	private void buildAST(PatternVector patternVector) {
//		ArrayList<MethodDeclaration> methods = new ArrayList<>();
		
		JavaASTParserInRefac javaParser = new JavaASTParserInRefac(info.source);
		MethodDeclaration m = javaParser.getViolatedMethod(info.start);
		
		MethodAST mAST = new MethodAST();
		mAST.asts.add(m);
		printChild(m, mAST.asts);
		for(ASTNode temp : mAST.asts) {
			if(temp instanceof SimpleName) {
				System.out.println(temp.getClass().getSimpleName() + " (" + temp + ") " + "Parent : " + temp.getParent().getClass().getSimpleName());
			}
			else System.out.println(temp.getClass().getSimpleName() + " Parent : " + temp.getParent().getClass().getSimpleName());
		}
		mASTs.add(mAST);
	}
	
	public void getBNF(ArrayList<MethodAST> methodASTs){
		for (MethodAST mAST : methodASTs) {
			int pIdx = 0;
			if (patterns.isEmpty()) {
				for (ASTNode ast : mAST.asts) {
					patterns.add(new PatternNode(ast));
				}
			} else {
				int mIdx = 0;
				while(mIdx != mAST.asts.size() && pIdx != patterns.size()) {
					if (patterns.get(pIdx).node.getClass().getSimpleName().equals(mAST.asts.get(mIdx).getClass().getSimpleName())) {
						patterns.get(pIdx).count++;
						pIdx ++;
						mIdx ++;
					} else pIdx ++;
				}
				if (pIdx == patterns.size()) {
					while(mIdx != mAST.asts.size()) {
						patterns.add(new PatternNode(mAST.asts.get(mIdx)));
						mIdx ++;
					}
				}
			}
		}
	}
	
	public void printPattern() {
		for(PatternNode nod : patterns) {
			System.out.println(nod.node.getClass().getSimpleName() + "(" + nod.count + ")");
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void printChild(ASTNode node, ArrayList<ASTNode> children) {
		ArrayList<ASTNode> tempBranch = new ArrayList<>();
    	List properties = node.structuralPropertiesForType();
    	
    	for(Iterator itertor = properties.iterator(); itertor.hasNext();) {
    		Object desciptor = itertor.next();
    		if(desciptor instanceof SimplePropertyDescriptor) {

    		} else if (desciptor instanceof ChildPropertyDescriptor) {
    			ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
    			ASTNode childNode = (ASTNode) node.getStructuralProperty(child);    			
    			if(childNode != null) {
    				if(isLeaf(childNode)) {
    					if(childNode instanceof SimpleName 
    							&& ((SimpleName) childNode).getIdentifier().equals(info.varName)) {
    						for(ASTNode temp = childNode; !(temp instanceof MethodDeclaration);temp = temp.getParent()) {
    							if(!(temp instanceof Block))
    								tempBranch.add(temp);
    						}
    						Collections.reverse(tempBranch);
    						for(ASTNode tempElem : tempBranch) {
    							if(!children.contains(tempElem)) {
    								children.add(tempElem);
    							}
    						}
    						tempBranch.clear();
    					} else {
    						for(ASTNode temp = childNode; !(temp instanceof MethodDeclaration);temp = temp.getParent()) {
    							if((temp instanceof IfStatement ||
    									temp instanceof DoStatement ||
    									temp instanceof SwitchStatement ||
    									temp instanceof BreakStatement ||
    									temp instanceof ContinueStatement ||
    									temp instanceof ReturnStatement ||
    									temp instanceof TryStatement ||
    									temp instanceof ForStatement ||
    									temp instanceof ThrowStatement ||
    									temp instanceof EnhancedForStatement ||
    									temp instanceof ConditionalExpression ||
    									temp instanceof SwitchCase)
//    									temp.getParent() instanceof MethodDeclaration
    									&& !(temp instanceof Block)
    		    						) {
    								tempBranch.add(temp);
    							}
    						}
    						Collections.reverse(tempBranch);
    						for(ASTNode tempElem : tempBranch) {
    							if(!children.contains(tempElem)) {
    								children.add(tempElem);
    							}
    						}
    						tempBranch.clear();
    					}
    				}
    				printChild(childNode, children);
    			}
    		} else { 
    			ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
    			printChild((List)node.getStructuralProperty(list), children);
    		}
    	}
    }
	
	 @SuppressWarnings("rawtypes")
		private void printChild(List nodes, ArrayList<ASTNode> children) {		 	
		 ArrayList<ASTNode> tempBranch = new ArrayList<>();
		    	for ( Iterator iterator = nodes.iterator(); iterator.hasNext();) {
		    		ASTNode node = (ASTNode) iterator.next();
		    		if(isLeaf(node)) {
		    			if(node instanceof SimpleName 
    							&& ((SimpleName) node).getIdentifier().equals(info.varName)) {
    						for(ASTNode temp = node; !(temp instanceof MethodDeclaration);temp = temp.getParent()) {
    							if(!(temp instanceof Block))
    								tempBranch.add(temp);
    						}
    						Collections.reverse(tempBranch);
    						for(ASTNode tempElem : tempBranch) {
    							if(!children.contains(tempElem)) {
    								children.add(tempElem);
    							}
    						}
    						tempBranch.clear();
    					} else {
    						for(ASTNode temp = node; !(temp instanceof MethodDeclaration);temp = temp.getParent()) {
    							if((temp instanceof IfStatement ||
    									temp instanceof DoStatement ||
    									temp instanceof SwitchStatement ||
    									temp instanceof BreakStatement ||
    									temp instanceof ContinueStatement ||
    									temp instanceof ReturnStatement ||
    									temp instanceof TryStatement ||
    									temp instanceof ForStatement ||
    									temp instanceof ThrowStatement ||
    									temp instanceof EnhancedForStatement ||
    									temp instanceof ConditionalExpression ||
    									temp instanceof SwitchCase)
//    									temp.getParent() instanceof MethodDeclaration
    									&& !(temp instanceof Block)
    		    						) {
    								tempBranch.add(temp);
    							}
    						}
    						Collections.reverse(tempBranch);
    						for(ASTNode tempElem : tempBranch) {
    							if(!children.contains(tempElem)) {
    								children.add(tempElem);
    							}
    						}
    						tempBranch.clear();
    					}
    				}
//		    		System.out.println(node);
		    		printChild(node, children);	
		    	}
	    }
	 
	 @SuppressWarnings("rawtypes")
	private boolean isLeaf(ASTNode node) {
		 ASTNode tempNode = node;
		 List properties = tempNode.structuralPropertiesForType();
		 for(Iterator itertor = properties.iterator(); itertor.hasNext();) {
	    		Object desciptor = itertor.next();
	    		if(desciptor instanceof SimplePropertyDescriptor) {
	    		} else if (desciptor instanceof ChildPropertyDescriptor) {
	    			ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
	    			ASTNode childNode = (ASTNode) tempNode.getStructuralProperty(child);    			
	    			if(childNode != null) {
	    				return false;
	    			}
	    		} else { 
	    			ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
	    			for ( Iterator iterator = ((List)tempNode.getStructuralProperty(list)).iterator(); iterator.hasNext();) {
			    		return false;			    		
			    	}
	    		}
	    	}
		return true;
	 }
	
	
}
