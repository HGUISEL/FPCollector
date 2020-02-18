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
 * <L> := 99999
 * <T> := 99998
 * <C> := 99997
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
		
		pattern = getLevel(graphString);
		pattern += " "+getState(graphString);
		pattern += getASTNodeType(graphString);
		
		return pattern;
	}
	
	private String getLevel(String g) {
		return "level: " + g.substring(0, 1);
	}
	
	private String getState(String g) {
		String stateVec = g.substring(2, 6);
		String state = "";
		if(stateVec.equals("99999")) {
			state = "Loop ControlNode";
		} else if(stateVec.equals("99998")) {
			state = "Terminate ControlNode";
		} else if(stateVec.equals("99997")) {
			state = "Conditional ControlNode";
		} else {
			switch(stateVec.substring(0, 1)) {
				case "00" : state = "Defined, Initialized with Null"; break;
				case "01" : state = "Defined, Initialized"; break;
				case "02" : state = "Defined DataNode"; break;
				case "03" : state = "Defined, Initialized with Null Field"; break;
				case "04" : state = "Defined, Initialized Field"; break;
				case "05" : state = "Defined Field"; break;
				case "06" : state = "Assigned"; break;
				case "07" : state = "Assigned Field"; break;
				case "08" : state = "Referenced"; break;
				case "09" : state = "Referenced Field"; break;
				case "10" : state = "Assigned with Null"; break;
				case "11" : state = "Assigned with Null Field"; break;			
			}
			switch(stateVec.charAt(2)){
				case '0' : state += ", In Conditional Statement"; break;
				case '1' : ; break;
			}
			switch(stateVec.charAt(3)){
				case '0' : state += ", Array whose Index is Variable"; break;
				case '1' : state += ", Array whose Index is Fixed"; break;
				case '2' : state += ", Not Array"; break;
			}
			switch(stateVec.charAt(4)){
				case '0' : state += " DataNode"; break;
				case '1' : state += " In For-each Statement Data Node"; break;
			}
		}		
		return state;
	}
	
//	<ASTNodeType> := {1(SimpleName), 2(ThisExpression), 3(DoStatement), 4(IfStatement), 5(ConditionalExpression), 6(ForStatement), 7(WhileStatement),
//			 * 8(EnhancedForStatement), 9(TryStatement), 10(CatchClause), 11(SwitchStatement), 12(ReutrnStatement), 13(ThrowStatement)}
	
	private String getASTNodeType(String g) {
		switch(g.substring(7, 8)) {
			case "01" : return "SimpleName";
			case "02" : return "ThisExpression";
			case "03" : return "DoStatement";
			case "04" : return "IfStatement";
			case "05" : return "ConditionalExpression";
			case "06" : return "ForStatement";
			case "07" : return "WhileStatement";
			case "08" : return "EnhancedForStatement";
			case "09" : return "TryStatement";
			case "10" : return "CatchClause";
			case "11" : return "SwitchStatement";
			case "12" : return "ReturnStatement";
			case "13" : return "ThrowStatement";			
		}
		return "";
	}	
}
