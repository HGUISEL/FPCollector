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
		String projectName = pathLineErrVar.get(0).split("git/")[1].split("/")[0];
		try {
			for(String pathVariable : pathLineErrVar) {
				if(flag == FALSE_POSITIVE) {
					path = pathVariable.split(",")[0];
					var = pathVariable.split(",")[3].trim();
				} else {
					path = pathVariable.split(",")[0].replace(projectName, projectName.split("_Past")[0]);
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
}
