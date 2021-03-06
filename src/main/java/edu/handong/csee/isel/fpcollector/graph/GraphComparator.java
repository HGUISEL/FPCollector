package edu.handong.csee.isel.fpcollector.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class GraphComparator {
	public HashMap <Integer, ArrayList<GraphInfo>> clusterByTotalNum = new HashMap<>();
	public ArrayList<Entry<Integer, ArrayList<GraphInfo>>> clusterByTotalNumRank = new ArrayList<>();
	public ArrayList<Entry<String, ArrayList<GraphInfo>>> clusterByTotalNodeRank = new ArrayList<>();
	public HashMap <String, ArrayList<GraphInfo>> clusterByTotalNode = new HashMap<>();
	
	public int totalGraphSize = 0;
		
	public void run(ArrayList<GraphInfo> graphInfos) {
		totalGraphSize = graphInfos.size();
		
		for (GraphInfo g : graphInfos)
			clusterByTotalNodeNum(g);
		
		clusterByTotalNodeNumRank();
	}
	
	public void cluster(ArrayList<GraphInfo> graphInfos) {
		totalGraphSize = graphInfos.size();
		
		for (GraphInfo g : graphInfos)
			clusterByTotalNode(g);
		
		clusterByTotalNodeRank();
	}

	private void clusterByTotalNodeNum(GraphInfo g) {
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
	

	private void clusterByTotalNodeNumRank() {
		for (Entry<Integer, ArrayList<GraphInfo>> g : clusterByTotalNum.entrySet())
			clusterByTotalNumRank.add(g);
		
		clusterByTotalNumRank.sort(new Comparator<Entry<Integer, ArrayList<GraphInfo>>>() {
			@Override
			public int compare(Entry<Integer, ArrayList<GraphInfo>> o1, Entry<Integer, ArrayList<GraphInfo>> o2) {
				// TODO Auto-generated method stub
				int size1 = o1.getValue().size();
				int size2 = o2.getValue().size();
				
				if (size1 == size2)
					return 0;
				else if (size1 > size2)
					return -1;
				else
					return 1;
			}
		});
	}
	
	private void clusterByTotalNodeRank() {
		for (Entry<String, ArrayList<GraphInfo>> g : clusterByTotalNode.entrySet())
			clusterByTotalNodeRank.add(g);
		
		clusterByTotalNumRank.sort(new Comparator<Entry<Integer, ArrayList<GraphInfo>>>() {
			@Override
			public int compare(Entry<Integer, ArrayList<GraphInfo>> o1, Entry<Integer, ArrayList<GraphInfo>> o2) {
				// TODO Auto-generated method stub
				int size1 = o1.getValue().size();
				int size2 = o2.getValue().size();
				
				if (size1 == size2)
					return 0;
				else if (size1 > size2)
					return -1;
				else
					return 1;
			}
		});
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

