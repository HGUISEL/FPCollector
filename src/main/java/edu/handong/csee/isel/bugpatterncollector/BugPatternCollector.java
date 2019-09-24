package edu.handong.csee.isel.bugpatterncollector;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;

import edu.handong.csee.isel.datastructure.DiffInfo;

public class BugPatternCollector {

	public static void main(String[] args) throws IOException, GitAPIException {
		// TODO Auto-generated method stub
		DataReader reader = new DataReader() ;
		
		ArrayList<DiffInfo> diffList = reader.run("/Users/yujin/ISEL/NewProject/SB:SJ_Result/BIC_zookeeper.csv") ;
		
//		checkoutedFiles.printFileInfo(0) ;
//		checkoutedFiles.printSize();
	}

}
