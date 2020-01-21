package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

public class JavaASTParserInRefac {
	CompilationUnit cUnit;
	String source;
	ArrayList<ImportDeclaration> lstImportDeclaration = new ArrayList<ImportDeclaration>();
	ArrayList<MethodDeclaration> lstMethodDeclaration = new ArrayList<MethodDeclaration>();
	ArrayList<MethodInvocation> lstMethodInvocation = new ArrayList<MethodInvocation>();
	ArrayList<FieldDeclaration> lstFieldDeclaration = new ArrayList<FieldDeclaration>();
	ArrayList<FieldAccess> lstFieldAccess = new ArrayList<FieldAccess>();
	ArrayList<IfStatement> lstIfStatement = new ArrayList<IfStatement>();
	ArrayList<ForStatement> lstForStatement = new ArrayList<ForStatement>();
	ArrayList<WhileStatement> lstWhileStatement = new ArrayList<WhileStatement>();
	ArrayList<VariableDeclarationFragment> lstVariableDeclarationFragment = new ArrayList<VariableDeclarationFragment>();
	ArrayList<ClassInstanceCreation> lstClassInstanceCreation = new ArrayList<ClassInstanceCreation>();
	ArrayList<SingleVariableDeclaration> lstSingleVariableDeclaration = new ArrayList<SingleVariableDeclaration>();
	ArrayList<SimpleName> lstSimpleName = new ArrayList<SimpleName>();
	ArrayList<TypeDeclaration> lstTypeDeclaration = new ArrayList<TypeDeclaration>();
	ArrayList<InfixExpression> lstInfixExpression = new ArrayList<InfixExpression>();
	ArrayList<ConditionalExpression> lstConditionalExpression = new ArrayList<ConditionalExpression>();
	ArrayList<ASTNode> lstInRangeNode = new ArrayList<ASTNode>();
	ArrayList<ASTNode> lstMethodLevel = new ArrayList<ASTNode>();
	
	
	PackageDeclaration pkgDeclaration;

	public JavaASTParserInRefac(String source){
		this.source = source;
		praseJavaFile(source);
	}
	
	public JavaASTParserInRefac(String source, int start, int end) {
		this.source = source;
		praseJavaFile(source, start, end);
	}
	
	public int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}

	public String getStringCode(){
		return source;
	}

	public CompilationUnit getCompilationUnit(){
		return cUnit;
	}

	public void praseJavaFile(String source){
		//String sourceCode = source;
		ASTParser parser = ASTParser.newParser(AST.JLS11);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = source.toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);
		
		try {
			final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			cUnit = unit;

			// Process the main body
			try {
				unit.accept(new ASTVisitor() {

					public boolean visit(MethodDeclaration node) {
						lstMethodDeclaration.add(node);
						return super.visit(node);
					}
					
					public boolean visit(MethodInvocation node) {
						lstMethodInvocation.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(TypeDeclaration node) {
						lstTypeDeclaration.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final FieldDeclaration node) {
						lstFieldDeclaration.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final SingleVariableDeclaration node) {
						lstSingleVariableDeclaration.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final VariableDeclarationFragment node) {
						lstVariableDeclarationFragment.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final FieldAccess node) {
						lstFieldAccess.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(IfStatement node) {
						lstIfStatement.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(ForStatement node) {
						lstForStatement.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(WhileStatement node) {
						lstWhileStatement.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(InfixExpression node) {
						lstInfixExpression.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(SimpleName node) {
						lstSimpleName.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final ImportDeclaration node) {
						lstImportDeclaration.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final PackageDeclaration node) {
						pkgDeclaration = node;
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					public boolean visit(final AnonymousClassDeclaration node) {
						//Log.info("AnonymousClassDeclaration");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					
					//Expression ? Expression : Expression
					public boolean visit(final ConditionalExpression node) {
						lstConditionalExpression.add(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(CatchClause node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(ClassInstanceCreation node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(DoStatement node) {
						lstMethodLevel.add(node);

						return super.visit(node);
					}
					public boolean visit(EnumConstantDeclaration node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(EnumDeclaration node) {
						lstMethodLevel.add(node);

						return super.visit(node);
					}

					public boolean visit(EnhancedForStatement node) {
						lstMethodLevel.add(node);

						return super.visit(node);
					}

					public boolean visit(AssertStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					} 
					public boolean visit(ContinueStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(SwitchCase node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(SynchronizedStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(ThisExpression node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(ThrowStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(TryStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}
					public boolean visit(final Block node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final Assignment node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final ExpressionStatement node) {
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeDeclaration node) {
						//Log.info("AnnotationTypeDeclaration");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeMemberDeclaration node) {
						//Log.info("AnnotationTypeMemberDeclaration");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayAccess node) {
						//Log.info("ArrayAccess");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayCreation node) {
						//Log.info("ArrayCreation");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayInitializer node) {
						//Log.info("ArrayInitializer");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayType node) {
						//Log.info("ArrayType");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}


					public boolean visit(final BlockComment node) {
						//Log.info("BlockComment");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final BooleanLiteral node) {
						//Log.info("BooleanLiteral");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final CastExpression node) {
						//Log.info("CastExpression");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final CharacterLiteral node) {
						//Log.info("CharacterLiteral");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}



					public boolean visit(final CompilationUnit node) {
						//Log.info("CompilationUnit");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					

					public boolean visit(final ConstructorInvocation node) {
						//Log.info("ConstructorInvocation");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final CreationReference node) {
						//Log.info("CreationReference");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final Dimension node) {
						//Log.info("Dimension");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

					public boolean visit(final EmptyStatement node) {
						//Log.info("EmptyStatement");
						//Log.info(node);
						lstMethodLevel.add(node);
						return super.visit(node);
					}

				public boolean visit(final Initializer node) {
					//Log.info("Initializer");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final InstanceofExpression node) {
					//Log.info("InstanceofExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final IntersectionType node) {
					//Log.info("IntersectionType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final Javadoc node) {
					//Log.info("Javadoc");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final LabeledStatement node) {
					//Log.info("LabeledStatement");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final LambdaExpression node) {
					//Log.info("LambdaExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final LineComment node) {
					//Log.info("LineComment");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final MarkerAnnotation node) {
					//Log.info("MarkerAnnotation");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final MemberRef node) {
					//Log.info("MemberRef");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final MemberValuePair node) {
					//Log.info("MemberValuePair");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRef node) {
					//Log.info("MethodRef");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRefParameter node) {
					//Log.info("MethodRefParameter");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final Modifier node) {
					//Log.info("Modifier");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final NameQualifiedType node) {
					//Log.info("NameQualifiedType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final NormalAnnotation node) {
					//Log.info("NormalAnnotation");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final NullLiteral node) {
					//Log.info("NullLiteral");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final NumberLiteral node) {
					//Log.info("NumberLiteral");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final ParameterizedType node) {
					//Log.info("ParameterizedType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final ParenthesizedExpression node) {
					//Log.info("ParenthesizedExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final PostfixExpression node) {
					//Log.info("PostfixExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final PrefixExpression node) {
					//Log.info("PrefixExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final PrimitiveType node) {
					//Log.info("PrimitiveType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final QualifiedName node) {
					//Log.info("QualifiedName");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final QualifiedType node) {
					//Log.info("QualifiedType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final SimpleType node) {
					//Log.info("SimpleType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final SingleMemberAnnotation node) {
					//Log.info("SingleMemberAnnotation");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final StringLiteral node) {
					//Log.info("StringLiteral");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final SuperConstructorInvocation node) {
					//Log.info("SuperConstructorInvocation");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final SuperFieldAccess node) {
					//Log.info("SuperFieldAccess");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodInvocation node) {
					//Log.info("SuperMethodInvocation");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodReference node) {
					//Log.info("SuperMethodReference");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final TagElement node) {
					//Log.info("TagElement");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final TextElement node) {
					//Log.info("TextElement");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final TypeDeclarationStatement node) {
					//Log.info("TypeDeclarationStatement");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final TypeLiteral node) {
					//Log.info("TypeLiteral");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final TypeMethodReference node) {
					//Log.info("TypeMethodReference");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final TypeParameter node) {
					//Log.info("UnionType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final UnionType node) {
					//Log.info("UnionType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationExpression node) {
					//Log.info("VariableDeclarationExpression");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}

				public boolean visit(final VariableDeclarationStatement node) {
					//Log.info("VariableDeclarationStatement");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				public boolean visit(final WildcardType node) {
					//Log.info("WildcardType");
					//Log.info(node);
					lstMethodLevel.add(node);
					return super.visit(node);
				}
				});
			} catch (Exception e) {
				System.out.println("Problem : " + e.toString());
				e.printStackTrace();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}

	}
	
	public void praseJavaFile(String source, int start, int end){
		//String sourceCode = source;
		ASTParser parser = ASTParser.newParser(AST.JLS11);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = source.toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);
		
		try {
			final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			cUnit = unit;

			// Process the main body
			try {
				unit.accept(new ASTVisitor() {

					public boolean visit(MethodDeclaration node) {
						lstMethodDeclaration.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(MethodInvocation node) {
						lstMethodInvocation.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(TypeDeclaration node) {
						lstTypeDeclaration.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final FieldDeclaration node) {
						lstFieldDeclaration.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final SingleVariableDeclaration node) {
						lstSingleVariableDeclaration.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final VariableDeclarationFragment node) {
						lstVariableDeclarationFragment.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final ClassInstanceCreation node) {
						lstClassInstanceCreation.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final FieldAccess node) {
						lstFieldAccess.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(IfStatement node) {
						lstIfStatement.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(ForStatement node) {
						lstForStatement.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(WhileStatement node) {
						lstWhileStatement.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(InfixExpression node) {
						lstInfixExpression.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(SimpleName node) {
						lstSimpleName.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final ImportDeclaration node) {
						lstImportDeclaration.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final PackageDeclaration node) {
						pkgDeclaration = node;
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(final AnonymousClassDeclaration node) {
						//Log.info("AnonymousClassDeclaration");
						//Log.info(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					
					//Expression ? Expression : Expression
					public boolean visit(final ConditionalExpression node) {
						lstConditionalExpression.add(node);
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(CatchClause node) {						
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(DoStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}

						return super.visit(node);
					}
					public boolean visit(EnumConstantDeclaration node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(EnumDeclaration node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(EnhancedForStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}

						return super.visit(node);
					}

					public boolean visit(AssertStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					} 
					public boolean visit(ContinueStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(SwitchCase node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(SynchronizedStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(ThisExpression node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(ThrowStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}
					public boolean visit(TryStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final Block node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final Assignment node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final ExpressionStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeDeclaration node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeMemberDeclaration node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final ArrayAccess node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final ArrayCreation node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final ArrayInitializer node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final ArrayType node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}


					public boolean visit(final BlockComment node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final BooleanLiteral node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final CastExpression node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final CharacterLiteral node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}



					public boolean visit(final CompilationUnit node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					

					public boolean visit(final ConstructorInvocation node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final CreationReference node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final Dimension node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}

					public boolean visit(final EmptyStatement node) {
						int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
						if(lineNumber <= end && lineNumber >= start) {
							lstInRangeNode.add(node);
						}
						return super.visit(node);
					}				

				public boolean visit(final Initializer node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final InstanceofExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final IntersectionType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final Javadoc node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final LabeledStatement node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final LambdaExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final LineComment node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final MarkerAnnotation node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final MemberRef node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final MemberValuePair node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				
				public boolean visit(final MethodRef node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final MethodRefParameter node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final Modifier node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final NameQualifiedType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final NormalAnnotation node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final NullLiteral node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final NumberLiteral node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final ParameterizedType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final ParenthesizedExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final PostfixExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final PrefixExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final PrimitiveType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final QualifiedName node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final QualifiedType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final SimpleType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final SingleMemberAnnotation node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final StringLiteral node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final SuperConstructorInvocation node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final SuperFieldAccess node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final SuperMethodInvocation node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final SuperMethodReference node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final TagElement node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final TextElement node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}

				public boolean visit(final TypeDeclarationStatement node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final TypeLiteral node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final TypeMethodReference node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final TypeParameter node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final UnionType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationExpression node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}				
				public boolean visit(final VariableDeclarationStatement node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				public boolean visit(final WildcardType node) {
					int lineNumber = unit.getLineNumber(node.getStartPosition()) - 1;
					if(lineNumber <= end && lineNumber >= start) {
						lstInRangeNode.add(node);
					}
					return super.visit(node);
				}
				});
			} catch (Exception e) {
				System.out.println("Problem : " + e.toString());
				e.printStackTrace();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}

	}
	
	public ArrayList<MethodDeclaration> getMethodDeclarations() {
		return lstMethodDeclaration;
	}
	
	public ArrayList<MethodInvocation> getMethodInvocations() {
		return lstMethodInvocation;
	}
	
	public ArrayList<TypeDeclaration> getTypeDeclarations() {
		return lstTypeDeclaration;
	}

	public ArrayList<FieldDeclaration> getFieldDeclarations() {
		return lstFieldDeclaration;
	}
	
	public HashMap<String,VariableDeclarationFragment> getMapForFieldDeclarations() {
		
		HashMap<String,VariableDeclarationFragment> maps = new HashMap<String,VariableDeclarationFragment>();
		
		for(FieldDeclaration fieldDec:lstFieldDeclaration){
			
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fieldDecFrags = fieldDec.fragments();
			
			for(VariableDeclarationFragment varDecFrag:fieldDecFrags){
				maps.put(varDecFrag.getName().toString(), varDecFrag);
			}
		}
		
		return maps;
	}

	public ArrayList<FieldAccess> getFieldAccesses() {
		return lstFieldAccess;
	}
	
	public ArrayList<IfStatement> getIfStatements() {
		return lstIfStatement;
	}
	
	public ArrayList<ForStatement> getForStatements() {
		return lstForStatement;
	}
	
	public ArrayList<WhileStatement> getWhileStatements() {
		return lstWhileStatement;
	}
	
	public ArrayList<InfixExpression> getInfixExpressions() {
		return lstInfixExpression;
	}

	public ArrayList<SimpleName> getSimpleNames() {
		return lstSimpleName;
	}
	
	public ArrayList<VariableDeclarationFragment> getVariableDeclarationFragments() {
		return lstVariableDeclarationFragment;
	}

	public ArrayList<ClassInstanceCreation> getClassInstanceCreations() {
		return lstClassInstanceCreation;
	}
	
	public ArrayList<SingleVariableDeclaration> getSingleVariableDeclarations() {
		return lstSingleVariableDeclaration;
	}
	
	public ArrayList<ImportDeclaration> getImportDeclarations() {
		return lstImportDeclaration;
	}
	
	public ArrayList<ConditionalExpression> getConditionalExpressions() {
		return lstConditionalExpression;
	}
	
	public PackageDeclaration getPackageDeclaration(){
		return pkgDeclaration;
	}
	
	public ArrayList<ASTNode> getInRangeNode(){
		return lstInRangeNode;
	}
	
	public ArrayList<ASTNode> getMethodLevel(){
		return lstMethodLevel;
	}
	
	public String getTypeOfSimpleName(ASTNode astNode,String name) {
		
		// TODO need to find a target name in a hierarchy but not globally in a file
		final ArrayList<SingleVariableDeclaration> lstSingleVarDecs = new ArrayList<SingleVariableDeclaration>();
		final ArrayList<VariableDeclarationStatement> lstVarDecStmts = new ArrayList<VariableDeclarationStatement>();
		final ArrayList<FieldDeclaration> lstFieldDecs = new ArrayList<FieldDeclaration>();
		final ArrayList<VariableDeclarationExpression> lstVarDecExps = new ArrayList<VariableDeclarationExpression>();
		
		astNode.accept(new ASTVisitor() {
			public boolean visit(SingleVariableDeclaration node) {
				lstSingleVarDecs.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationStatement node) {
				lstVarDecStmts.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationExpression node) {
				lstVarDecExps.add(node);
				return super.visit(node);
			}
			public boolean visit(FieldDeclaration node) {
				lstFieldDecs.add(node);
				return super.visit(node);
			}
		}
		);
		
		for(SingleVariableDeclaration dec:lstSingleVarDecs){
			if (dec.getName().toString().equals(name))
				return dec.getType().toString();
		}
		for(VariableDeclarationStatement dec:lstVarDecStmts){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		for(VariableDeclarationExpression dec:lstVarDecExps){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		
		for(FieldDeclaration dec:lstFieldDecs){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		
		if(astNode.getParent() == null)
			return "";
		
		return getTypeOfSimpleName(astNode.getParent(),name);
	}

	public ArrayList<SimpleName> getSimpleNames(ASTNode node) {
		
		final ArrayList<SimpleName> simpleNames = new ArrayList<SimpleName>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(SimpleName node) {
				simpleNames.add(node);
				return super.visit(node);
			}
			
		});
		
		return simpleNames;
	}

	public ArrayList<QualifiedName> getQualifiedNames(ASTNode exp) {
		final ArrayList<QualifiedName> qualifiedNames = new ArrayList<QualifiedName>();
		
		exp.accept(new ASTVisitor() {
			
			public boolean visit(QualifiedName node) {
				qualifiedNames.add(node);
				return super.visit(node);
			}
			
		});
		
		return qualifiedNames;
	}

	public ArrayList<MethodInvocation> getMethodInvocations(ASTNode node) {
		final ArrayList<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(MethodInvocation node) {
				methodInvocations.add(node);
				return super.visit(node);
			}
			
		});
		
		return methodInvocations;
	}

	public ArrayList<InfixExpression> getInfixExpressions(Expression exp) {
		final ArrayList<InfixExpression> infixExps = new ArrayList<InfixExpression>();
		
		exp.accept(new ASTVisitor() {
			
			public boolean visit(InfixExpression node) {
				infixExps.add(node);
				return super.visit(node);
			}
			
		});
		
		return infixExps;
	}

	public ArrayList<ArrayAccess> getArrayAccesses(ASTNode node) {
		final ArrayList<ArrayAccess> arrayAccesses = new ArrayList<ArrayAccess>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ArrayAccess node) {
				arrayAccesses.add(node);
				return super.visit(node);
			}
			
		});
		
		return arrayAccesses;
	}

	public ArrayList<ExpressionStatement> getExpressionStatements(ASTNode node) {
		final ArrayList<ExpressionStatement> expStmts = new ArrayList<ExpressionStatement>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ExpressionStatement node) {
				expStmts.add(node);
				return super.visit(node);
			}
			
		});
		
		return expStmts;
	}

	public ArrayList<VariableDeclarationFragment> getVariableDeclarationFragments(ASTNode node) {
		final ArrayList<VariableDeclarationFragment> varDecFrags = new ArrayList<VariableDeclarationFragment>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				varDecFrags.add(node);
				return super.visit(node);
			}
			
		});
		
		return varDecFrags;
	}

	public ArrayList<VariableDeclaration> getVariableDeclaration(ASTNode node) {
		final ArrayList<VariableDeclaration> varDecs = new ArrayList<VariableDeclaration>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				varDecs.add(node);
				return super.visit(node);
			}
			
			public boolean visit(SingleVariableDeclaration node) {
				varDecs.add(node);
				return super.visit(node);
			}
			
		});
		
		return varDecs;
	}
	
	public HashMap<String,VariableDeclaration> getMapForVariableDeclaration(ASTNode node) {
		final HashMap<String, VariableDeclaration> mapVarDecs = new HashMap<String,VariableDeclaration>();
		
		if(node==null) return mapVarDecs;
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}
			
			public boolean visit(SingleVariableDeclaration node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}
			
		});
		
		return mapVarDecs;
	}

	public ArrayList<String> getFieldNames() {
		
		ArrayList<String> fieldNames = new ArrayList<String>();
		
		for(FieldDeclaration fieldDec:getFieldDeclarations()){
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment>  varDecFrags = fieldDec.fragments();
			for(VariableDeclarationFragment varDecFrag:varDecFrags){
				fieldNames.add(varDecFrag.getName().toString());
			}
		}
		
		return fieldNames;
	}

	public ArrayList<String> getVariableNames(ASTNode node) {
		ArrayList<String> localVarNames = new ArrayList<String>();
		
		ArrayList<VariableDeclarationFragment> varDecFrags = getVariableDeclarationFragments(node);		
		for(VariableDeclarationFragment verDecFrag:varDecFrags){
			localVarNames.add(verDecFrag.getName().toString());
		}
		
		ArrayList<SingleVariableDeclaration> singleVarDecs = getSingleVariableDeclarations(node);
		for(SingleVariableDeclaration singleVarDec:singleVarDecs){
			localVarNames.add(singleVarDec.getName().toString());
		}
		
		return localVarNames;
	}

	private ArrayList<SingleVariableDeclaration> getSingleVariableDeclarations(ASTNode node) {
		final ArrayList<SingleVariableDeclaration> singleVarDecs = new ArrayList<SingleVariableDeclaration>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(SingleVariableDeclaration node) {
				singleVarDecs.add(node);
				return super.visit(node);
			}
			
		});
		
		return singleVarDecs;
	}
	
	public AbstractTypeDeclaration getTypeDeclationOf(ASTNode node) {
		
		if(node==null) // null can be happen when there is no TypeDeclaration but EnumDeclaration
			return null;
		
		if(node.getParent() instanceof AbstractTypeDeclaration)
			return (AbstractTypeDeclaration) node.getParent();
		
		return getTypeDeclationOf(node.getParent());
	}

	public ArrayList<String> getFieldNames(AbstractTypeDeclaration classWhereMethodExists) {
		ArrayList<String> fieldNames = new ArrayList<String>();
		
		for(FieldDeclaration fieldDec:getFieldDeclarations()){
			
			if(!fieldDec.getParent().equals(classWhereMethodExists)) continue;
			
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment>  varDecFrags = fieldDec.fragments();
			for(VariableDeclarationFragment varDecFrag:varDecFrags){
				fieldNames.add(varDecFrag.getName().toString());
			}
		}
		
		return fieldNames;
	}

	public ArrayList<FieldAccess> getFieldAccesses(ASTNode node) {
		final ArrayList<FieldAccess> fieldAccesses = new ArrayList<FieldAccess>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(FieldAccess node) {
				fieldAccesses.add(node);
				return super.visit(node);
			}
			
		});
		
		return fieldAccesses;
	}

	public MethodDeclaration getMethodDec(ASTNode node) {
		
		if(node.getParent() == null)
			return null;
		
		if(node.getParent() instanceof MethodDeclaration){
			return (MethodDeclaration) node.getParent();
		}
		
		return getMethodDec(node.getParent());
	}

	public TypeDeclaration getTypeDeclaration(ASTNode node) {
		
		if(node.getParent() == null) return null;
		
		if(node.getParent() instanceof TypeDeclaration)
			return (TypeDeclaration) node.getParent();
		
		return getTypeDeclaration(node.getParent());
	}

	public MethodDeclaration getMethodDecBelongTo(ASTNode node) {
		
		if(node.getParent() == null) return null;
		
		if(node.getParent() instanceof MethodDeclaration)
			return (MethodDeclaration) node.getParent();
		
		
		return getMethodDecBelongTo(node.getParent());
	}

	public ArrayList<ThrowStatement> getThrowStatements(ASTNode node) {
		final ArrayList<ThrowStatement> throwStatements = new ArrayList<ThrowStatement>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ThrowStatement node) {
				throwStatements.add(node);
				return super.visit(node);
			}
			
		});
		
		return throwStatements;
	}

	public ArrayList<Assignment> getAssignments(ASTNode node) {
		final ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(Assignment node) {
				assignments.add(node);
				return super.visit(node);
			}
			
		});
		
		return assignments;
	}

	public ASTNode getInterface(ASTNode node) {
		
		if(node == null) return null;
		
		if(node.getParent() instanceof TypeDeclaration)
			return node.getParent();
		
		return getInterface(node.getParent());
	}
	
	public ArrayList<SimpleName> getViolatedNames(String name){
		final ArrayList<SimpleName> violatedNames = new ArrayList<SimpleName>();
		for(SimpleName simpleName : lstSimpleName) {
			int flag = 0;
			if(simpleName.getParent() != null && name.equals(simpleName.toString())) {
				
				if(simpleName.getParent().getNodeType() == 40) {
					ASTNode temp = simpleName.getParent();
					
					while(temp.getNodeType() == 40) {
						temp = temp.getParent();
					}
					
					if(temp.getNodeType() == 35 || temp.getNodeType() == 15 ||
							temp.getNodeType() == 26 ) {
						flag = 1;
					}
				}
				
				if(flag == 0)
					violatedNames.add(simpleName);
			}
		}
		return violatedNames;
	}
	
	public MethodDeclaration getViolatedMethod(int start) {
		for (int i = lstMethodDeclaration.size() - 1; i >= 0; i--) {
			MethodDeclaration m = lstMethodDeclaration.get(i);
			if (cUnit.getLineNumber(m.getStartPosition()) - 1 <= start)
				return m;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static void printChild(ASTNode node) {
    	List properties = node.structuralPropertiesForType();
    	
    	for(Iterator itertor = properties.iterator(); itertor.hasNext();) {
    		Object desciptor = itertor.next();
    		if(desciptor instanceof SimplePropertyDescriptor) {
    			SimplePropertyDescriptor simple = (SimplePropertyDescriptor)desciptor;
    			Object value = node.getStructuralProperty(simple);
    			System.out.println(simple.getId() + " SimpleProperty (" + value.toString() + ")");
    		} else if (desciptor instanceof ChildPropertyDescriptor) {
    			ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
    			ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
    			if(childNode != null) {
    				System.out.println("Child ( " + child.getId() + ") {");
    				printChild(childNode);
    				System.out.println("}\n");
    			}
    		} else { 
    			ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
    			System.out.println("ChildList (" + list.getId()+ ") {");
    			printChild((List)node.getStructuralProperty(list));
    			System.out.println("}\n");
    		}
    	}
    }
    @SuppressWarnings("rawtypes")
	private static void printChild(List nodes) {
    	for ( Iterator iterator = nodes.iterator(); iterator.hasNext();) {
    		ASTNode node = (ASTNode) iterator.next();
    		printChild(node);
    	}
    }
}
