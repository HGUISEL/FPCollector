package edu.handong.csee.isel.datastructure;

import org.eclipse.jgit.diff.EditList;

public class DiffInfo {
	public CommitInfo fix = new CommitInfo() ;
	public CommitInfo BI = new CommitInfo() ;
	
	public String filePath ;
	public int prefixLine ;
	
//	public File prefixFile ;
//	public File fixFile ;
//	
	public String prefixCode ;
	public String fixCode ;
	
	public EditList editList ;
	
	public void print_info() {
		System.out.println("File path: " + filePath) ;
		System.out.println("Prefix line: " + prefixLine) ;
		System.out.println("Prefix source: " + prefixCode) ;
		System.out.println("Fix source: " + fixCode) ;
		System.out.println();
	}
}
