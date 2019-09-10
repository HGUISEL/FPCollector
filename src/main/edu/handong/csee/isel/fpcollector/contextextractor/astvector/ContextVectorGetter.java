package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.*;

import edu.handong.csee.isel.fpcollector.structures.VectorNode;
import edu.handong.csee.isel.fpcollector.utils.JavaASTParser;

public class ContextVectorGetter {
	final static int TRUE_POSITIVE = 0;
	final static int FALSE_POSITIVE = 1;
	
	public ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> 
	getContextNode(ArrayList<String> pathLineErrVar, int flag){
		ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> contextInfo = new ArrayList<>();
		String path = "";
		String var = "";
		int progressCount = 0;
		int total = pathLineErrVar.size();
		String projectName = pathLineErrVar.get(0).split("git/")[1].split("/")[0];
		try {
			for(String pathVariable : pathLineErrVar) {
				progressCount++;
//				printProgress(total, progressCount);
				if(progressCount == 1) {
					System.out.print("0%...");
				}
				if(progressCount == total / 4 ) {
					System.out.print("25%...");
				}
				if(progressCount == total / 2) {
					System.out.print("50%...");
				}
				if(progressCount == (total *3) / 4) {
					System.out.print("75%...");
				}
				if(progressCount == total) {
					System.out.print("done...");
				}
				
				
				if(flag == TRUE_POSITIVE) {
					path = pathVariable.split(",")[0].replace(projectName, projectName.split("_Past")[0]);
					var = pathVariable.split(",")[3].trim();
				} else {
					path = pathVariable.split(",")[0];
					var = pathVariable.split(",")[3].trim();
				} 
				
				/* 1. File Existing Check
				 * 2. File Context Extracting
				 */
				
				//file existing checking
				File f = new File(path);
				String source = "";
				if(f.exists()) {
					FileReader fReader = new FileReader(f);
					BufferedReader bufReader = new BufferedReader(fReader);
					String line = "";
					while((line = bufReader.readLine()) != null) {
						source = source.concat(line + "\n");
					}
				bufReader.close();
				}else continue;
				
				JavaASTParser codeAST = new JavaASTParser(source);
				ArrayList<SimpleName> names = new ArrayList<>();
				names = codeAST.getViolatedNames(var);
				ArrayList<ASTNode> contexts = new ArrayList<>();
				SimpleEntry<ASTNode, ArrayList<ASTNode>> contextsWithVar = null;
				if(names.size() == 0) {
					continue;
				}
				point :
				for(SimpleName name : names) {
					for(ASTNode parent = name ; !(parent instanceof TypeDeclaration) ; parent = parent.getParent()) {
						if(!((parent instanceof Annotation) ||
							 (parent instanceof AnnotationTypeDeclaration) ||
							 (parent instanceof AnnotatableType) ||
							 (parent instanceof AnnotationTypeMemberDeclaration) ||
							 (parent instanceof Block) ||
							 (parent instanceof BlockComment) ||
							 (parent instanceof Comment) ||
							 (parent instanceof ImportDeclaration) ||
							 (parent instanceof LineComment)||
							 (parent instanceof MarkerAnnotation) ||
							 (parent instanceof MethodDeclaration) ||
							 (parent instanceof MemberRef)||
							 (parent instanceof MethodRef) ||
							 (parent instanceof MethodReference) ||
							 (parent instanceof MethodRefParameter) ||
							 (parent instanceof NormalAnnotation) ||
							 (parent instanceof PackageDeclaration) ||
							 (parent instanceof SingleMemberAnnotation) ||
							 (parent instanceof TagElement))) {
//							if(parent instanceof PostfixExpression) {
//								//contexts.add(((VariableDeclaration) parent).getInitializer());
//								System.out.print(((PostfixExpression) parent).getNodeType() + " : ");
//								System.out.print(((PostfixExpression) parent));
//								//System.out.print("\nParent : " + ((MethodInvocation) parent).getParent());
//								System.out.println("(" + ((PostfixExpression) parent).getClass() + ")");								
//							}
							if(parent.getParent() == null) {
								continue point;
							}
							contexts.add(parent);
						}	
					}
					contextsWithVar = new SimpleEntry<ASTNode, ArrayList<ASTNode>>(name, contexts);
				}
				
			contextInfo.add(contextsWithVar);
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return contextInfo;
	}
	
	public ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> 
	getContextVector(ArrayList<SimpleEntry<ASTNode, ArrayList<ASTNode>>> contextNodes){
		ArrayList<SimpleEntry<ASTNode, ArrayList<VectorNode>>> contextVector = new ArrayList<>();
		for(SimpleEntry<ASTNode, ArrayList<ASTNode>> nodes : contextNodes) {
			if(nodes == null) {
				continue;
			}
			ArrayList<VectorNode> vectorizedNodes = new ArrayList<>();
			ArrayList<ASTNode> nodesValue = new ArrayList<>();
			nodesValue = nodes.getValue();
			for(ASTNode node : nodesValue) {
				VectorNode tempVectorNode = new VectorNode(node);
				vectorizedNodes.add(tempVectorNode);
			}
			contextVector.add(new SimpleEntry<ASTNode, ArrayList<VectorNode>>(nodes.getKey(), vectorizedNodes));
		}
		
		return contextVector;
	}
	
//	private static void printProgress(int total, int current) {
//
//	    StringBuilder string = new StringBuilder(140);   
//	    int percent = (int) (current * 100 / total);
//	    string
//	        .append('\r')
//	        .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
//	        .append(String.format(" %d%% [", percent))
//	        .append(String.join("", Collections.nCopies(percent, "=")))
//	        .append('>')
//	        .append(String.join("", Collections.nCopies(100 - percent, " ")))
//	        .append(']')
//	        .append(String.join("", Collections.nCopies(current == 0 ? (int) (Math.log10(total)) : (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
//	        .append(String.format(" %d/%d", current, total));
//	    System.out.print(string);
//	}
	
}
