package edu.handong.csee.isel.fpcollector.contextextractor.astvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

import edu.handong.csee.isel.fpcollector.refactoring.JavaASTParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class LineVectorGetter {
	public ArrayList<ArrayList<SimpleEntry<Integer, String>>> 
	getViolatedLineVector(ArrayList<String> varPath) {
		ArrayList<ArrayList<SimpleEntry<Integer, String>>> contextInfo = 
				new ArrayList<>();
		
		try {
			for(String pathVariable : varPath) {
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
				ArrayList<ASTNode> lineElement = new ArrayList<>();
				ArrayList<SimpleEntry<Integer, String>> violationLineInfo = new ArrayList<>();
				for(SimpleName name : names) {
					ASTNode temp = null;
					for(ASTNode parent = name.getParent() ; 
							codeAST.getLineNum(name.getStartPosition()) == codeAST.getLineNum(parent.getStartPosition()); 
							parent = parent.getParent()) {
						if(parent.getNodeType() != 31 && parent.getNodeType() != 8) {
							temp = parent;
							}
						}
					if(temp != null)
					lineElement.add(temp);
				}
				for(ASTNode nodeType : lineElement) {
					SimpleEntry<Integer, String> classTypeNum = 
							new SimpleEntry<>(nodeType.getNodeType(), nodeType.getClass().toString());
					violationLineInfo.add(classTypeNum);
					}
				contextInfo.add(violationLineInfo);
				}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return contextInfo;
	}
	
	 @SuppressWarnings("rawtypes")
	private ArrayList<Object> getAllChildren(ASTNode node) {
		 	ArrayList<SimplePropertyDescriptor> simpleProperty = new ArrayList<>();
		 	ArrayList<Object> valueInSimple = new ArrayList<>();
		 	List properties = node.structuralPropertiesForType();
	    	
	    	for(Iterator itertor = properties.iterator(); itertor.hasNext();) {
	    		Object desciptor = itertor.next();
	    		if(desciptor instanceof SimplePropertyDescriptor) {
	    			SimplePropertyDescriptor simple = (SimplePropertyDescriptor)desciptor;
	    			Object value = node.getStructuralProperty(simple);
	    			simpleProperty.add(simple);
	    			valueInSimple.add(value);
	    			//System.out.println(simple.getId() + " SimpleProperty (" + value.toString() + ")");
	    		} else if (desciptor instanceof ChildPropertyDescriptor) {
	    			ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
	    			ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
	    			if(childNode != null) {
	    			//	System.out.println("Child ( " + child.getId() + ") {");
	    				getAllChildren(childNode);
	    			//	System.out.println("}\n");
	    			}
	    		} else { 
	    			ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
	    			//System.out.println("ChildList (" + list.getId()+ ") {");
	    			getAllChildren((List)node.getStructuralProperty(list));
	    			//System.out.println("}\n");
	    		}
	    	}
	    	
//	    	for(Iterator iterator = simpleProperty.iterator(); iterator.hasNext();) {
//	    		Object printFor = iterator.next();
//	    		System.out.println(printFor);
//	    	}
//	    	
//	    	for(Iterator iterator = valueInSimple.iterator(); iterator.hasNext();) {
//	    		Object printFor = iterator.next();
//	    		System.out.println(printFor);
//	    	}
	    	return valueInSimple;
	    }
	 
	    @SuppressWarnings("rawtypes")
		private void getAllChildren(List nodes) {
	    	for ( Iterator iterator = nodes.iterator(); iterator.hasNext();) {
	    		ASTNode node = (ASTNode) iterator.next();
	    		getAllChildren(node);
	    	}
	    }
}
