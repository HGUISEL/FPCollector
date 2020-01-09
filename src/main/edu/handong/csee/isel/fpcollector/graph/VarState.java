package edu.handong.csee.isel.fpcollector.graph;
/*
 * State
DIN for definition + Initialization(with Null)
DI for definition + Initialization(with none-Null)
D for definition without Initialization
Ass for being used to be Assigned, but Not Initialization
Ref for being used
--------------------------------------
In Condition
I for in conditional statement/loop
O for not in conditional statement/loop
---------------------------------------
About Type
ArrIdxC for ArrayType whose index is composed with ++, -- or var
ArrIdxF for ArrayType whose index is fixed integer
NArr for None-ArrayType
 * */
public enum VarState {
	DIN, DI, D, I, O, Ass, Ref, ArrIdxC, ArrIdxF, NArr
}
