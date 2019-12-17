package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

public class BNFChecker {
	Info info = new Info();
	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	public ArrayList<MethodAST> methodASTs = new ArrayList<>();
	ArrayList<PatternNode> patterns = new ArrayList<>();
	ArrayList<ASTNode> children = new ArrayList<>();
	
	public void run(Info info, PatternVector patternVector) {
		this.info = info;
		buildAST(patternVector);
	}
	
	private void buildAST(PatternVector patternVector) {
		int start = 0;
		int end = 0;
		ArrayList<MethodDeclaration> methods = new ArrayList<>();
		JavaASTParser javaParser = new JavaASTParser(info.source);
		violatedNode = javaParser.getViolatedNames(info.varName);
		start = Integer.parseInt(info.start);
		end = Integer.parseInt(info.end);
		methods.addAll(javaParser.getMethodDeclarations());
		checkInRange(patternVector, start, end, methods);
	}
	
	private void checkInRange(PatternVector patternVector, int start, int end, ArrayList<MethodDeclaration> methods) {
		int methodStart = 0;
		int min = 99999999;
		JavaASTParser parserInRange;
		ASTNode methodInRange = null;
		
		for(MethodDeclaration tempMethod : methods) {
			int lineNumber = ((CompilationUnit) tempMethod.getRoot()).getLineNumber(tempMethod.getStartPosition()) - 1;
			if(lineNumber <= start && start - lineNumber < min) {
				min = start - lineNumber;
				methodStart = lineNumber;
				methodInRange = tempMethod;
			}
		}
		
		printChild(methodInRange, children);
		
		for(ASTNode tempnode : children) {
			System.out.println(tempnode.getClass().getSimpleName());
		}
		
		parserInRange = new JavaASTParser(info.source, methodStart, end);
		
		for(ASTNode temp : parserInRange.getInRangeNode()) {
			System.out.println(temp.getClass().getSimpleName());
		}
		MethodAST methodAST = new MethodAST();
		methodAST.asts.addAll(parserInRange.getInRangeNode());
		
		methodASTs.add(methodAST);
		int size = methodASTs.size();
		System.out.println("" + methodASTs.get(size-1) + ":::::" + size );
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
	private static void printChild(ASTNode node, ArrayList<ASTNode> children) {
    	List properties = node.structuralPropertiesForType();
    	
    	for(Iterator itertor = properties.iterator(); itertor.hasNext();) {
    		Object desciptor = itertor.next();
    		if(desciptor instanceof SimplePropertyDescriptor) {
    			SimplePropertyDescriptor simple = (SimplePropertyDescriptor)desciptor;
    			Object value = node.getStructuralProperty(simple);
//    			System.out.println(simple.getId() + " SimpleProperty (" + value.toString() + ")");
    		} else if (desciptor instanceof ChildPropertyDescriptor) {
    			ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
    			ASTNode childNode = (ASTNode) node.getStructuralProperty(child);    			
    			if(childNode != null) {
//    				System.out.println("Child ( " + child.getId() + ") {");
    				children.add(childNode);
    				printChild(childNode, children);
//    				System.out.println("}\n");
    			}
    		} else { 
    			ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
//    			System.out.println("ChildList (" + list.getId()+ ") {");
    			printChild((List)node.getStructuralProperty(list), children);
//    			System.out.println("}\n");
    		}
    	}
    }
	
	 @SuppressWarnings("rawtypes")
		private static void printChild(List nodes, ArrayList<ASTNode> children) {
	    	for ( Iterator iterator = nodes.iterator(); iterator.hasNext();) {
	    		ASTNode node = (ASTNode) iterator.next();
	    		children.add(node);
	    		printChild(node, children);
	    	}
	    }
	
	
}
