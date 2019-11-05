package edu.handong.csee.isel.bugpatterncollector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;

import edu.handong.csee.isel.datastructure.DiffInfo;

public class Parser {
	void run (ArrayList<DiffInfo> diffList) throws Exception {
		for (DiffInfo d : diffList) {
//			parse(d) ;
//			create_file(d) ;
			break;
		}
	}
	
	void create_file(DiffInfo d) throws IOException {
		d.A = new File("./A.java") ;
		d.B = new File("./B.java") ;
		
		FileWriter fwA = new FileWriter(d.A) ;
		FileWriter fwB = new FileWriter(d.B) ;
		
		fwA.write(d.fixCode);
		fwA.flush();
		if (d.isBi) fwB.write(d.biCode) ;
		else fwB.write(d.prefixCode) ;
		fwB.flush();
		
		fwA.close();
		fwB.close();
	}
	
	private void parse(DiffInfo d) {
		ASTParser fixParser = ASTParser.newParser(AST.JLS10) ;
		ASTParser biParser = ASTParser.newParser(AST.JLS10) ;
		
		fixParser.setSource(d.fixCode.toCharArray()) ;
		fixParser.setKind(ASTParser.K_COMPILATION_UNIT) ;
		if (d.isBi) biParser.setSource(d.biCode.toCharArray()) ;
		else biParser.setSource(d.prefixCode.toCharArray()) ;
		biParser.setKind(ASTParser.K_COMPILATION_UNIT) ;
		
//		final CompilationUnit fixCu = (CompilationUnit)fixParser.createAST(null) ;
//		final CompilationUnit biCu = (CompilationUnit)biParser.createAST(null) ;
//		
//		fixCu.accept(new ASTVisitor() {
//			
//			public boolean visit(ForStatement node) {
//				
//				return super.visit(node) ;
//			}
//		});
	}
}
