package edu.handong.csee.isel.fpcollector.graph;

/*
 * Graph2String Rule
 * <String> := <Level> <ControlState><ASTNodeType> | <Level><DataState><ASTNodeType>
 * <Level> := {x | 0 < x < 100 && x is Integer}
 * <ASTNodeType> := {1(SimpleName), 2(ThisExpression), 3(DoStatement), 4(IfStatement), 5(ConditionalExpression), 6(ForStatement), 7(WhileStatement),
 * 8(EnhancedForStatement), 9(TryStatement), 10(CatchClause), 11(SwitchStatement), 12(ReutrnStatement), 13(ThrowStatement)}
 * <ControlState> := <L> | <T> | <C>
 * <DataState> := (<Define> | <Reference>)<InCondition><Type><getFrom>
 * 
 * <L> := 00000
 * <T> := 00001
 * <C> := 00002
 * 
 * <Define> := 0(DIN) | 1(DI) | 2(D) | 3(FDIN) | 4(FDI) | 5(FD)
 * <Reference> := 6(Ass) | 7(FAss) | 8(Ref) | 9(FRef) | 10(NAss) | 11(FNAss)
 * <InCondition> := 0(I) | 1(O)
 * <Type> := 0(ArrIdxC) | 1(ArrIdxF) | 2(NArr)
 * <getFrom> := 0(null) | 1(not null)
 * 
 */

public class NodeInterpreter {
	public String interpret(String graphString) {
		String pattern = "";
		
	}
}
