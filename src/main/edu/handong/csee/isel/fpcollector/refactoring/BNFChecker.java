package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

public class BNFChecker {
	Info info = new Info();
	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	ArrayList<ASTNode> violatedNodeInRange = new ArrayList<>();
	ArrayList<PatternNode> patterns = new ArrayList<>();
	
	public BNFChecker(Info info, PatternVector patternVector) {
		this.info = info;
		buildAST(patternVector);
	}
	
	private void buildAST(PatternVector patternVector) {
		JavaASTParser javaParser = new JavaASTParser(info.source);
		violatedNode = javaParser.getViolatedNames(info.varName);
		
		checkInRange(patternVector);
	}
	
	private void checkInRange(PatternVector patternVector) {

		int start = 0;
		int methodStart = 0;
		int end = 0;
		String tempSource = "" + info.source;

		String[] lines = tempSource.split("\n");
		String tempLine = lines[Integer.parseInt(info.start) -1];
		start = tempSource.indexOf(tempLine);
		tempLine = lines[Integer.parseInt(info.end) - 1];
		tempSource = tempSource.substring(start);
		end = tempSource.indexOf(tempLine) + start;	
		
		JavaASTParser parserInRange = new JavaASTParser(info.source, start, end);
		ArrayList<MethodDeclaration> methods = parserInRange.getMethodDeclarations();
		
		int methodHit = 0;
		for(MethodDeclaration tempMethod : methods) {
			int tempMethodPosition = tempMethod.getStartPosition();
			if(tempMethodPosition <= end && tempMethodPosition >= start) {
				System.out.println(tempMethod);
				methodHit = 1;
			}
		}
		int min = 99999999;
		if(methodHit == 0 ) {
			
			for(MethodDeclaration tempMethod : methods) {
				if(tempMethod.getStartPosition() <= start && start - tempMethod.getStartPosition() < min) {
					min = start - tempMethod.getStartPosition();
					methodStart = tempMethod.getStartPosition();
				}
			}
			parserInRange = new JavaASTParser(info.source, methodStart, end);
		}
		
		violatedNodeInRange.addAll(parserInRange.getInRangeNode());
		
		for(ASTNode tempNode : violatedNodeInRange) {
//			System.out.println(tempNode);
			System.out.println(tempNode.getClass().getSimpleName());
		}
		
		getBNF(patternVector);
		
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
	
	
	
}
