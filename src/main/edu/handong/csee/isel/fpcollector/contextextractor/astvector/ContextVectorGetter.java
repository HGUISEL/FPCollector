package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import edu.handong.csee.isel.fpcollector.structures.VectorNode;
import edu.handong.csee.isel.fpcollector.utils.JavaASTParser;

public class ContextVectorGetter {
	public ArrayList<ArrayList<ASTNode>> getContextNode(ArrayList<String> pathLineErrVar){
		ArrayList<ArrayList<ASTNode>> contextInfo = new ArrayList<>();
		
		try {
			for(String pathVariable : pathLineErrVar) {
				String path = pathVariable.split(",")[0];
				String var = pathVariable.split(",")[3].trim();
			
				File f = new File(path);
				FileReader fReader = new FileReader(f);
				BufferedReader bufReader = new BufferedReader(fReader);
				
				String source = "";
				String line = "";
				
				while((line = bufReader.readLine()) != null) {
					source = source.concat(line + "\n");
				}
				bufReader.close();
				
				JavaASTParser codeAST = new JavaASTParser(source);
				ArrayList<SimpleName> names = new ArrayList<>();
				
				names = codeAST.getViolatedNames(var);
				ArrayList<ASTNode> contexts = new ArrayList<>();
				
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
							 (parent instanceof TagElement) ||
							 contexts.contains(parent))) {
							contexts.add(parent);
						}	
					}
				}
				
			contextInfo.add(contexts);
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return contextInfo;
	}
	
	public ArrayList<ArrayList<VectorNode>> getContextVector(ArrayList<ArrayList<ASTNode>> contextNodes){
		ArrayList<ArrayList<VectorNode>> contextVector = new ArrayList<>();
		
		for(ArrayList<ASTNode> nodes : contextNodes) {
			ArrayList<VectorNode> vectorizedNodes = new ArrayList<>();
			for(ASTNode node : nodes) {
				VectorNode tempVectorNode = new VectorNode(node);
				vectorizedNodes.add(tempVectorNode);
			}
			contextVector.add(vectorizedNodes);
		}
		
		return contextVector;
	}
}
