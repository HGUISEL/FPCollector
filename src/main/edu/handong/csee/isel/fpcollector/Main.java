package edu.handong.csee.isel.fpcollector;

import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.graph.ControlNode;
import edu.handong.csee.isel.fpcollector.graph.GraphComparator;
import edu.handong.csee.isel.fpcollector.graph.GraphInfo;
import edu.handong.csee.isel.fpcollector.graph.GraphInfoGetter;
import edu.handong.csee.isel.fpcollector.graph.GraphNodeClusterer;
import edu.handong.csee.isel.fpcollector.graph.GraphNodeNumClusterer;
import edu.handong.csee.isel.fpcollector.graph.GraphRankWriter;
import edu.handong.csee.isel.fpcollector.graph.GraphWriter;
import edu.handong.csee.isel.fpcollector.graph.NodeResolver;
import edu.handong.csee.isel.fpcollector.refactoring.GitCheckout;
import edu.handong.csee.isel.fpcollector.refactoring.GitClone;
import edu.handong.csee.isel.fpcollector.refactoring.InfoCollector;
import edu.handong.csee.isel.fpcollector.refactoring.Input;
import edu.handong.csee.isel.fpcollector.refactoring.InstanceChecker;
import edu.handong.csee.isel.fpcollector.refactoring.ReportComparator;
import edu.handong.csee.isel.fpcollector.refactoring.ReportReader;
import edu.handong.csee.isel.fpcollector.refactoring.RunTool;
import edu.handong.csee.isel.fpcollector.refactoring.TFPCWriter;

public class Main {
	final static int FAILED = 0;
	final static int SUCCESS = 1;
	final static int CLONE = 2;
	final static int CHECKOUT = 3;
	final static int FPC = 4;
	final static int TPC = 5;
	
	private static String[] getPMDReport(String[] args) {
		String[] strs = new String[4];
				
		//1. read input file
		Input input = new Input();
		if(input.getInput(args) == FAILED) {
			System.out.println("Wrong Input : Please, Input Properly.");
			System.exit(-1);
		}
			
		//2. git clone the project
		GitClone gitClone = new GitClone();
		gitClone.clone(input.gitAddress, input.projectName);
			
		//3. run pmd
		RunTool runPmdOnCurrent = new RunTool();
		runPmdOnCurrent.getReport(CLONE, input.toolCommand, gitClone.clonedPath, input.rule, input.projectName);
			
		//4. clone the cloned project and checkout to a year a go
		GitCheckout gitCheckout = new GitCheckout();
		gitCheckout.checkout(gitClone.clonedPath, input.time, input.projectName);
			
		//5. run pmd to checkouted project
		RunTool runPmdOnPast = new RunTool();
		runPmdOnPast.getReport(CHECKOUT, input.toolCommand, gitCheckout.checkoutPath, input.rule, input.projectName);
			
		strs[0] = input.projectName;
		strs[1] = input.rule;
		strs[2] = runPmdOnCurrent.reportPath;
		strs[3] = runPmdOnPast.reportPath;
		
		return strs;
	}
	
	private static String[] collectFTPC(String currentReportPath, String pastReportPath, String projectName, String ruleName) {
		String[] strs = new String[2];
		
		//1. read report of present project
		//2. read report of past project
		ReportReader currentReport = new ReportReader();
		ReportReader pastReport = new ReportReader();
			
		currentReport.readReport(currentReportPath);
		pastReport.readReport(pastReportPath);
			
		//3. compare is there anything same
		ReportComparator compareCurrentAndPast = new ReportComparator();
		compareCurrentAndPast.run(currentReport, pastReport, projectName);
		
		//4. write file which contains FPC
		TFPCWriter fpcWriter = new TFPCWriter();
		TFPCWriter tpcWriter = new TFPCWriter();
		
		fpcWriter.writeContexts(compareCurrentAndPast.FPC, projectName + "FPC");
		tpcWriter.writeContexts(compareCurrentAndPast.TPC, projectName + "TPC");

		strs[0] = fpcWriter.fileName;
		strs[1] = tpcWriter.fileName;
		
		return strs;
	}
	
	private static ArrayList<ControlNode> drawGraph(String candidateFileName, int phase) {
		// 1. read input
		InfoCollector collector = new InfoCollector();
		
		try {
			collector.run(candidateFileName, phase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("graphSize: " + collector.graphs.size());
		
		return collector.graphs;
	}
	
	private static ArrayList<GraphInfo> getGraphInfo(ArrayList<ControlNode> graphs, int phase) {
		ArrayList<GraphInfo> graphInfos = new ArrayList<>();
			int count = 0;
			for(ControlNode g : graphs) {
				count++;
				System.out.println("GraphInfo " + count);
				if(count == 488) 
					System.out.println("A");
				GraphInfo tempGraphInfo = new GraphInfo(g);
				GraphInfoGetter tempGetter = new GraphInfoGetter();
//				g.printInfo();
				tempGetter.getNodeNum(tempGraphInfo);
				graphInfos.add(tempGraphInfo);
			}
			
			return graphInfos;
	}
	
	private static void clusterGraphByNodeNum(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos, String projectName, int phase) {
		GraphNodeNumClusterer fpcGraphNodeNumClusterer = new GraphNodeNumClusterer(fpcGraphInfos);
		GraphNodeNumClusterer tpcGraphNodeNumClusterer = new GraphNodeNumClusterer(tpcGraphInfos);
		
		fpcGraphNodeNumClusterer.run(fpcGraphInfos);
		tpcGraphNodeNumClusterer.run(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();
		
		//cluster by total node num
		graphWriter.writeTotalNumRankGraph(fpcGraphNodeNumClusterer.clusterByTotalNumRank, projectName + "FPCTNNum", fpcGraphNodeNumClusterer.totalGraphSize);
		graphWriter.writeTotalNumRankGraph(tpcGraphNodeNumClusterer.clusterByTotalNumRank, projectName + "TPCTNNum", tpcGraphNodeNumClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraphTotalNum(fpcGraphNodeNumClusterer, tpcGraphNodeNumClusterer, projectName);
	}
	
	private static void clusterGraphByNode(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos, String projectName, int phase) {
		GraphNodeClusterer fpcGraphNodeClusterer = new GraphNodeClusterer(fpcGraphInfos);
		GraphNodeClusterer tpcGraphNodeClusterer = new GraphNodeClusterer(tpcGraphInfos);
		
		//transform each graph to one string and clustering graphs with there properties
		NodeResolver nodeResolver = new NodeResolver();
		nodeResolver.transformNode(fpcGraphNodeClusterer.clusterByTotalNum);
		nodeResolver.transformNode(tpcGraphNodeClusterer.clusterByTotalNum);
		
		fpcGraphNodeClusterer.run(fpcGraphInfos);
		tpcGraphNodeClusterer.run(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();
		
		//cluster by violated node
		graphWriter.writeNodeRankGraph(fpcGraphNodeClusterer.clusterByTotalNodeRank, projectName + "FPCNode", fpcGraphNodeClusterer.totalGraphSize);
		graphWriter.writeNodeRankGraph(tpcGraphNodeClusterer.clusterByTotalNodeRank, projectName + "TPCNode", tpcGraphNodeClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraph(fpcGraphNodeClusterer, tpcGraphNodeClusterer, projectName);
	}
	
	public static void main(String[] args) {
//        System.out.println("Heap Size(M) : " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MB");
//        System.out.println("Max Heap Size(M) : " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
	    double numOfFPCRun = 0.0;
		// 1. Preparing for Collecting False Positive Candidates
		String[] strs = getPMDReport(args);
		
		String projectName = strs[0];
		String ruleName = strs[1];
		String currentReportPath = strs[2];
		String pastReportPath = strs[3];
		System.out.println("Step 1 CLEAR");
		
		// 2. Collecting False Positive Candidates
		strs = collectFTPC(currentReportPath, pastReportPath, projectName, ruleName);
		
		// remove instances
		String fpcCandidateFileName = strs[0];
		String tpcCandidateFileName = strs[1];
		strs = null;
		System.out.println("Step 2 CLEAR");
		
		// 3. Get Pattern of the FPC
		ArrayList<ControlNode> fpcGraphs = new ArrayList<>();
		ArrayList<ControlNode> tpcGraphs = new ArrayList<>();
		
		InstanceChecker instanceChecker = new InstanceChecker();
		
		instanceChecker.checkNum(fpcCandidateFileName, FPC);
		instanceChecker.checkNum(fpcCandidateFileName, TPC);
		
		if(instanceChecker.fpcInstanceNumber > 2000) {
			numOfFPCRun = Math.ceil((double) instanceChecker.fpcInstanceNumber / 2000);
		}	
		
		ArrayList<GraphInfo> fpcGraphInfos = new ArrayList<>();
		ArrayList<GraphInfo> tpcGraphInfos = new ArrayList<>();
		
		for(int i = 1 ; i <= numOfFPCRun; i ++) {
			fpcGraphs = drawGraph(fpcCandidateFileName, i);
			tpcGraphs = drawGraph(tpcCandidateFileName, i);
			System.out.println("Step 3 CLEAR");
			
			//Step 4. Get Graph Information
			fpcGraphInfos = getGraphInfo(fpcGraphs, i);
			tpcGraphInfos = getGraphInfo(tpcGraphs, i);
			fpcGraphs = null;
			tpcGraphs = null;
			System.out.println("Step 4 Clear");
		
			//Step 5. Graph Clustering
			clusterGraphByNodeNum(fpcGraphInfos, tpcGraphInfos, projectName, i);
			clusterGraphByNode(fpcGraphInfos, tpcGraphInfos, projectName, i);
			System.out.println("Step 5 Clear");
		}
	}
}
