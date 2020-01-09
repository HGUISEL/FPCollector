package edu.handong.csee.isel.fpcollector.graph;
/*
*State
1.Define
	1) DIN for definition + Initialization(with Null)
	2) DI for definition + Initialization(with none-Null)
	3) D for definition without Initialization
2.Reference
	1) Ass for being used to be Assigned, but Not Initialization
	2) Ref for being used
--------------------------------------
In Condition
1. I for in conditional statement/loop
2. O for not in conditional statement/loop
---------------------------------------
About Type
1. ArrIdxC for ArrayType whose index is composed with ++, -- or var
2. ArrIdxF for ArrayType whose index is fixed integer
3. NArr for None-ArrayType
 * */
public enum VarState {
	DIN, DI, D, I, O, Ass, Ref, ArrIdxC, ArrIdxF, NArr
}
