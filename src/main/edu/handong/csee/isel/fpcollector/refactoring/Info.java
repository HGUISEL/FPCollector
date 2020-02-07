package edu.handong.csee.isel.fpcollector.refactoring;

import java.util.ArrayList;

public class Info {
	public String source;
	public ArrayList<String> varName = new ArrayList<>();
	public ArrayList<String> fieldName = new ArrayList<>();
	public String start;
	public String end;
	public String path;
	
	public void printInfo() {
		System.out.println("source: " + this.source);
		if (this.varName.size() >= 1)
			System.out.println("varName0: " + this.varName.get(0));
		System.out.println("start: " + this.start);
		System.out.println("end: " + this.end);
	}
}
