package edu.handong.csee.isel.fpcollector.ast.graph.node.state;

/*
State
1.Define
	1) DIN for definition + Initialization(with Null)
	2) DI for definition + Initialization(with none-Null)
	3) D for definition without Initialization
2.Reference
	1) RD for being used to be Assigned, but Not Initialization
	2) R for being used
 */

public enum VarState {
	DIN, DI, D, RD, R
}
