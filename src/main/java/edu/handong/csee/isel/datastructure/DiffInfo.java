package edu.handong.csee.isel.datastructure;

import java.io.File;

import org.eclipse.jgit.diff.EditList;

public class DiffInfo {
	public CommitInfo fix = new CommitInfo() ;
	public CommitInfo prefix = new CommitInfo() ;
	public CommitInfo bi = new CommitInfo() ;
	
//	public File prefixFile ;
//	public File fixFile ;
//	
	public String prefixCode ;
	public String fixCode ;
	public String biCode ;
	
	public File A ;
	public File B ;
	
	public EditList editList ;
	
	public boolean isBi ;
	
	public void print_info() {
		System.out.println("Prefix source: " + prefixCode) ;
		System.out.println("Fix source: " + fixCode) ;
		System.out.println();
	}
}
