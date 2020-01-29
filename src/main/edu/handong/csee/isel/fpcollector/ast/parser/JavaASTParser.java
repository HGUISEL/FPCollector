package edu.handong.csee.isel.fpcollector.ast.parser;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import edu.handong.csee.isel.fpcollector.refactoring.Info;

public class JavaASTParser {
	CompilationUnit cUnit;
	Info info;
	
	public void run(Info info){
		this.info = info;
		
		parse();
	}

	private void parse() {
		ASTParser parser = ASTParser.newParser(AST.JLS12);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = info.source.toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_7);
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

//			try {
//				unit.accept(new ASTVisitor() {
//
//					public boolean visit(MethodDeclaration node) {
//						lstMethodDeclaration.add(node);
////						System.out.println("level : " + level);
//						checkScope(node);
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(SimpleName node) {
//						//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						IBinding binding = node.resolveBinding();
//						if(info.varName.get(0) == null && getLineNum(node.getStartPosition()) == Integer.parseInt(info.start) && binding instanceof IVariableBinding) {
//							System.out.println(""+getLineNum(node.getStartPosition()));
//							info.varName.add(node.getIdentifier());
//						}
//						
//						if(isScope && isDefine)
//							System.out.println(""+getLineNum(node.getStartPosition()) + node.resolveBinding());
//						
//						lstSimpleName.add(node);
//						if (isScope) {
//							if (!isDefine)
//								checkDefine(node);
//							else if (info.varName.contains(node.getIdentifier())  /*&& getLineNum(node.getStartPosition()) >= Integer.parseInt(info.start)*/){
//								DataNode n = new DataNode(node, level);
//								
//								if(isD(node) == VarState.D)
//									n.setState(VarState.D);
//								else if(isD(node) == VarState.DI)
//									n.setState(VarState.DI);
//								else if(isD(node) == VarState.Ref)
//									n.setState(VarState.Ref);
//								else if(isD(node) == VarState.DIN)
//									n.setState(VarState.DIN);
//								else if(isD(node) == VarState.Ass)
//									n.setState(VarState.Ass);
//								
//								if(checkType(node) == VarState.ArrIdxC)
//									n.setType(VarState.ArrIdxC);
//								else if(checkType(node) == VarState.ArrIdxF)
//									n.setType(VarState.ArrIdxF);
//								else if(checkType(node) == VarState.NArr)
//									n.setType(VarState.NArr);
//								
//								if(isInCondition(node)) {
//									n.setInCondition(VarState.I);
//								} else {
//									n.setInCondition(VarState.O);
//								}													
//																							
//								root.nexts.add(n);
//								
//								for (int i = 0; i <= level; i++)
//									lstUseVar.set(i, true);
//							}
//						}
//
//						return super.visit(node);
//					}
//					
//					public boolean visit(DoStatement node) {
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.L);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//
//					public boolean visit(IfStatement node) {
//						lstIfStatement.add(node);
//						
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.C);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(ForStatement node) {
//						lstForStatement.add(node);
//						
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.L);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(WhileStatement node) {
//						lstWhileStatement.add(node);
//						
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.L);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(EnhancedForStatement node) {
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.L);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(TryStatement node) {
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.C);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(CatchClause node) {
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.C);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(SwitchStatement node) {
//						if (isDefine) {
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							n.setProperty(ControlState.C);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(ReturnStatement node) {						
//						if (isDefine) {
//							isTerm = true;
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.E, level);
//							n.setProperty(ControlState.T);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(ThrowStatement node) {
//						if (isDefine) {
//							isTerm = true;
//							level ++;
//							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							ControlNode n = new ControlNode(node, ControlState.E, level);
//							n.setProperty(ControlState.T);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
//						
//						return super.visit(node);
//					}
//					
//					public boolean visit(final VariableDeclarationFragment node) {
//						lstVariableDeclarationFragment.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(MethodInvocation node) {
//						lstMethodInvocation.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(TypeDeclaration node) {
//						lstTypeDeclaration.add(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final FieldDeclaration node) {
//						lstFieldDeclaration.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(final SingleVariableDeclaration node) {
//						lstSingleVariableDeclaration.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(final ClassInstanceCreation node) {
//						lstClassInstanceCreation.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(final FieldAccess node) {
//						lstFieldAccess.add(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(InfixExpression node) {
//						lstInfixExpression.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(final ImportDeclaration node) {
//						lstImportDeclaration.add(node);
//						return super.visit(node);
//					}
//					
//					public boolean visit(final PackageDeclaration node) {
//						pkgDeclaration = node;
//						return super.visit(node);
//					}
//					
//					public boolean visit(final AnonymousClassDeclaration node) {
//						//Log.info("AnonymousClassDeclaration");
//						//Log.info(node);
//						return super.visit(node);
//					}
//					
//					//Expression ? Expression : Expression
//					public boolean visit(final ConditionalExpression node) {
//						lstConditionalExpression.add(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(EnumConstantDeclaration node) {
////						list.add(node.getName().toString());
//						return super.visit(node);
//					}
//					public boolean visit(EnumDeclaration node) {
////						list.add("EnumDeclaration");
////						list.add(node.getName().toString());
//						return super.visit(node);
//					}
//
//					public boolean visit(AssertStatement node) {
////						list.add("AssertStatement");
//						return super.visit(node);
//					} 
//					public boolean visit(ContinueStatement node) {
////						list.add("ContinueStatement");
//						return super.visit(node);
//					}
//
//					public boolean visit(SwitchCase node) {
////						list.add("SwitchCase");
//						return super.visit(node);
//					}
//					public boolean visit(SynchronizedStatement node) {
////						list.add("SynchronizedStatement");
//						return super.visit(node);
//					}
//					public boolean visit(ThisExpression node) {
////						list.add("ThisExpression");
//						return super.visit(node);
//					}
//
//					public boolean visit(final Block node) {
//
//						return super.visit(node);
//					}
//
//					public boolean visit(final Assignment node) {
//
//						return super.visit(node);
//					}
//
//					public boolean visit(final ExpressionStatement node) {
//
//						return super.visit(node);
//					}
//
//					public boolean visit(final AnnotationTypeDeclaration node) {
//						//Log.info("AnnotationTypeDeclaration");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final AnnotationTypeMemberDeclaration node) {
//						//Log.info("AnnotationTypeMemberDeclaration");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//
//					public boolean visit(final ArrayAccess node) {
//						//Log.info("ArrayAccess");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final ArrayCreation node) {
//						//Log.info("ArrayCreation");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final ArrayInitializer node) {
//						//Log.info("ArrayInitializer");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final ArrayType node) {
//						//Log.info("ArrayType");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//
//					public boolean visit(final BlockComment node) {
//						//Log.info("BlockComment");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final BooleanLiteral node) {
//						//Log.info("BooleanLiteral");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final CastExpression node) {
//						//Log.info("CastExpression");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final CharacterLiteral node) {
//						//Log.info("CharacterLiteral");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//
//
//					public boolean visit(final CompilationUnit node) {
//						//Log.info("CompilationUnit");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					
//
//					public boolean visit(final ConstructorInvocation node) {
//						//Log.info("ConstructorInvocation");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final CreationReference node) {
//						//Log.info("CreationReference");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final Dimension node) {
//						//Log.info("Dimension");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//					public boolean visit(final EmptyStatement node) {
//						//Log.info("EmptyStatement");
//						//Log.info(node);
//						return super.visit(node);
//					}
//
//				public boolean visit(final Initializer node) {
//					//Log.info("Initializer");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final InstanceofExpression node) {
//					//Log.info("InstanceofExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final IntersectionType node) {
//					//Log.info("IntersectionType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final Javadoc node) {
//					//Log.info("Javadoc");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final LabeledStatement node) {
//					//Log.info("LabeledStatement");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final LambdaExpression node) {
//					//Log.info("LambdaExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final LineComment node) {
//					//Log.info("LineComment");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final MarkerAnnotation node) {
//					//Log.info("MarkerAnnotation");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final MemberRef node) {
//					//Log.info("MemberRef");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final MemberValuePair node) {
//					//Log.info("MemberValuePair");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final MethodRef node) {
//					//Log.info("MethodRef");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final MethodRefParameter node) {
//					//Log.info("MethodRefParameter");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final Modifier node) {
//					//Log.info("Modifier");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final NameQualifiedType node) {
//					//Log.info("NameQualifiedType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final NormalAnnotation node) {
//					//Log.info("NormalAnnotation");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final NullLiteral node) {
//					//Log.info("NullLiteral");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final NumberLiteral node) {
//					//Log.info("NumberLiteral");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final ParameterizedType node) {
//					//Log.info("ParameterizedType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final ParenthesizedExpression node) {
//					//Log.info("ParenthesizedExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final PostfixExpression node) {
//					//Log.info("PostfixExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final PrefixExpression node) {
//					//Log.info("PrefixExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final PrimitiveType node) {
//					//Log.info("PrimitiveType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final QualifiedName node) {
//					//Log.info("QualifiedName");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final QualifiedType node) {
//					//Log.info("QualifiedType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final SimpleType node) {
//					//Log.info("SimpleType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final SingleMemberAnnotation node) {
//					//Log.info("SingleMemberAnnotation");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final StringLiteral node) {
//					//Log.info("StringLiteral");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final SuperConstructorInvocation node) {
//					//Log.info("SuperConstructorInvocation");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final SuperFieldAccess node) {
//					//Log.info("SuperFieldAccess");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final SuperMethodInvocation node) {
//					//Log.info("SuperMethodInvocation");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final SuperMethodReference node) {
//					//Log.info("SuperMethodReference");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final TagElement node) {
//					//Log.info("TagElement");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final TextElement node) {
//					//Log.info("TextElement");
//					//Log.info(node);
//					return super.visit(node);
//				}
//
//				public boolean visit(final TypeDeclarationStatement node) {
//					//Log.info("TypeDeclarationStatement");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final TypeLiteral node) {
//					//Log.info("TypeLiteral");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final TypeMethodReference node) {
//					//Log.info("TypeMethodReference");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final TypeParameter node) {
//					//Log.info("UnionType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final UnionType node) {
//					//Log.info("UnionType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final VariableDeclarationExpression node) {
//					//Log.info("VariableDeclarationExpression");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final VariableDeclarationStatement node) {
//					//Log.info("VariableDeclarationStatement");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				public boolean visit(final WildcardType node) {
//					//Log.info("WildcardType");
//					//Log.info(node);
//					return super.visit(node);
//				}
//				
//				public void endVisit(DoStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						} else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	
//						else {
//							root = root.parent;
//						}
//					}
//					//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//
//				public void endVisit(IfStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						} else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							root = root.parent;
//						}
//					} 
//					//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(ForStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						} else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	  
//						else {
//							root = root.parent;
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(WhileStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}
//						else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							root = root.parent;
//						}
//					}
//					
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(EnhancedForStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						} else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	  
//						else {
//							root = root.parent;
//						}
//					}
//					
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(TryStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						} else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	  
//						else {
//							root = root.parent;
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(CatchClause node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (root.parent.node instanceof TryStatement) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}
//						else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							root = root.parent;
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(SwitchStatement node) {
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (lstUseVar.get(level + 1)) {
//							if (isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}
//						else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							root = root.parent;
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(ReturnStatement node) {
//					
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//						}
//						else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				public void endVisit(ThrowStatement node) {
//					
//					if (isDefine) {
//						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//						if (--level < 0) {
//							isDefine = false;
//							isScope = false;
//							isTerm = false;
//
//						}
//						else if (isTerm){
//							if(isEnd()) root.setState(ControlState.E);
//							
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}	 
//						else {
//							ControlNode temp = root;
//							root = root.parent;
//							root.nexts.add(temp);
//						}
//					} 
//						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//				}
//				
//				});
//			} catch (Exception e) {
//				System.out.println("Problem : " + e.toString());
//				e.printStackTrace();
//				System.exit(0);
//			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}
	}
}
