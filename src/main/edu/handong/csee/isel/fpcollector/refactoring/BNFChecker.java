package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class BNFChecker {
	Info info = new Info();
	ArrayList<SimpleName> violatedNode = new ArrayList<>();
	public ArrayList<MethodAST> methodASTs = new ArrayList<>();
	public ArrayList<MethodAST> mASTs = new ArrayList<MethodAST>();
	ArrayList<PatternNode> patterns = new ArrayList<>();
	
	public void run(Info info, PatternVector patternVector) {
		this.info = info;
		buildAST(patternVector);
	}
	
	private void buildAST(PatternVector patternVector) {
		JavaASTParser javaParser = new JavaASTParser(info.source);
		MethodDeclaration m = javaParser.getViolatedMethod(Integer.parseInt(info.start));
//		System.out.println(m);
		
		MethodAST mAST = new MethodAST();

		for (ASTNode c : mAST.asts)
			System.out.println(c.getClass().getSimpleName());
		
//		checkInRange(patternVector);
	}
	
	private void checkInRange(PatternVector patternVector) {
		int flag = 1;
		int start = 0;
		int methodStart = 0;
		int end = 0;
//		String tempSource = "" + info.source;
		
//		String[] lines = tempSource.split("\n");
//		String tempLine = lines[Integer.parseInt(info.start) -1];
//		start = tempSource.indexOf(tempLine);
//		tempLine = lines[Integer.parseInt(info.end) - 1];
//		tempSource = tempSource.substring(start);
//		end = tempSource.indexOf(tempLine) + start;
		
		if(flag == 1) {
		for(int i =0 ; i < Integer.parseInt(info.start) -1; i ++) {
			info.source = info.source.replaceFirst("\n", "/*NewLineOccurred*/");
		}
		start = info.source.lastIndexOf("/*NewLineOccurred*/") + 19;
		for(int i = 0; i < Integer.parseInt(info.end) - Integer.parseInt(info.start) - 1; i ++) {
			info.source = info.source.replaceFirst("\n", "/*NewLineOccurred*/");
		}
		end = info.source.lastIndexOf("/*NewLineOccurred*/") + 19;
		flag--;
		}
		
		JavaASTParser parserInRange = new JavaASTParser(info.source, start, end);
		ArrayList<MethodDeclaration> methods = parserInRange.getMethodDeclarations();
		
		int methodHit = 0;
		for(MethodDeclaration tempMethod : methods) {
			int tempMethodPosition = tempMethod.getStartPosition();
			if(tempMethodPosition <= end && tempMethodPosition >= start) {
//				System.out.println(tempMethod);
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
	
	
}
