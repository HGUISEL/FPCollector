package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphComparator {
	public HashMap <Integer, ArrayList<GraphInfo>> clusterByTotalNum = new HashMap<>();	
	public HashMap <String, ArrayList<GraphInfo>> clusterByTotalNode = new HashMap<>();
	
	public void clusterByTotalNodeNum(GraphInfo g) {
		Integer totalNodeNum = g.controlNodeNum + g.dataNodeNum;
		if(!clusterByTotalNum.containsKey(totalNodeNum)) {
			clusterByTotalNum.put(totalNodeNum, null);
			ArrayList<GraphInfo> tempGraphInfo = new ArrayList<>();
			tempGraphInfo.add(g);
			clusterByTotalNum.replace(totalNodeNum, tempGraphInfo);
		}
		else{
			clusterByTotalNum.get(totalNodeNum).add(g);
		}
	}
	
	public void clusterByTotalNode(GraphInfo g) {		
		if(g.graph2String.equals("")) {
			return;
		}
		else {
			String graphString2Int = g.graph2String;						
				if(clusterByTotalNode.containsKey(graphString2Int)) {
					clusterByTotalNode.get(graphString2Int).add(g);
					clusterByTotalNode.replace(graphString2Int, clusterByTotalNode.get(graphString2Int));
				} else {
					ArrayList<GraphInfo> temp = new ArrayList<>();
					temp.add(g);
					clusterByTotalNode.put(graphString2Int, temp);
				}
		}
	}
}
