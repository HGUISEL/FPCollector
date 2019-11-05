package edu.handong.csee.isel.datastructure;

public class CommitInfo {
	public String id ;
	public String filePath ;
	
	public void print_info() {
		System.out.print("commitID: " + id);
		System.out.print("filePath: " + filePath);
		System.out.println();
		System.out.println();
	}
}
