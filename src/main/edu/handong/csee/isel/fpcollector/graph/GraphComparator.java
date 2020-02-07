package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphComparator {
	public HashMap <Integer, ArrayList<GraphInfo>> clusterByTotalNum = new HashMap<>();
		
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
}
