package edu.handong.csee.isel.fpcollector.graph;

import java.util.HashMap;

public class GraphInfo {
	public ControlNode root;
	public int controlNodeNum = 0;
	public int dataNodeNum = 0;
	public HashMap<Integer, Integer> nodeNum = new HashMap<>();
	public GraphInfo(ControlNode root) {
		this.root = root;
	}
}
