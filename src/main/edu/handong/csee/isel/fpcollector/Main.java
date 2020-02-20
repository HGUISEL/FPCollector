package edu.handong.csee.isel.fpcollector;

import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.graph.ControlNode;
import edu.handong.csee.isel.fpcollector.graph.GraphBuilder;
import edu.handong.csee.isel.fpcollector.graph.GraphComparator;
import edu.handong.csee.isel.fpcollector.graph.GraphInfo;
import edu.handong.csee.isel.fpcollector.graph.GraphInfoGetter;
import edu.handong.csee.isel.fpcollector.graph.GraphWriter;
import edu.handong.csee.isel.fpcollector.graph.NodeResolver;
import edu.handong.csee.isel.fpcollector.refactoring.GitCheckout;
import edu.handong.csee.isel.fpcollector.refactoring.GitClone;
import edu.handong.csee.isel.fpcollector.refactoring.Info;
import edu.handong.csee.isel.fpcollector.refactoring.InfoCollector;
import edu.handong.csee.isel.fpcollector.refactoring.Input;
import edu.handong.csee.isel.fpcollector.refactoring.ReportComparator;
import edu.handong.csee.isel.fpcollector.refactoring.ReportReader;
import edu.handong.csee.isel.fpcollector.refactoring.RunTool;
import edu.handong.csee.isel.fpcollector.refactoring.TFPCWriter;

public class Main {
	final static int FAILED = 0;
	final static int SUCCESS = 1;
	final static int CLONE = 2;
	final static int CHECKOUT = 3;
	
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
		
		if(ruleName.contains("DataflowAnomalyAnalysis")) {
			fpcWriter.writeContextsForDFA(compareCurrentAndPast.FPC, projectName + "FPC");
			tpcWriter.writeContextsForDFA(compareCurrentAndPast.TPC, projectName + "TPC");
			
		} else {
			fpcWriter.writeContexts(compareCurrentAndPast.FPC, projectName + "FPC");
			tpcWriter.writeContexts(compareCurrentAndPast.TPC, projectName + "TPC");
		}
		
		strs[0] = fpcWriter.fileName;
		strs[1] = tpcWriter.fileName;
		
		return strs;
	}
	
	private static ArrayList<ControlNode> drawGraph(String candidateFileName) {
		// 1. read input
		InfoCollector collector = new InfoCollector();
		
		try {
			collector.run(candidateFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("graphSize: " + collector.graphs.size());
		
		return collector.graphs;
	}
	
	private static ArrayList<GraphInfo> getGraphInfo(ArrayList<ControlNode> graphs) {
		ArrayList<GraphInfo> graphInfos = new ArrayList<>();
			
			for(ControlNode g : graphs) {
				GraphInfo tempGraphInfo = new GraphInfo(g);
				GraphInfoGetter tempGetter = new GraphInfoGetter();
//				g.printInfo();
				tempGetter.getNodeNum(tempGraphInfo);
				graphInfos.add(tempGraphInfo);
			}
			
			return graphInfos;
	}
	
	private static GraphComparator compareGraph(ArrayList<GraphInfo> graphInfos) {
		GraphComparator graphComparator = new GraphComparator();
		
		graphComparator.run(graphInfos);
		
		return graphComparator;
	}
	
	private static void clusterGraph(GraphComparator fpcGraphComparator, GraphComparator tpcGraphComparator, 
			ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos, String projectName) {
		//transform each graph to one string and clustering graphs with there properties
		NodeResolver nodeResolver = new NodeResolver();
		nodeResolver.transformNode(fpcGraphComparator.clusterByTotalNum);
		nodeResolver.transformNode(tpcGraphComparator.clusterByTotalNum);
		
		fpcGraphComparator.cluster(fpcGraphInfos);
		tpcGraphComparator.cluster(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();

		//cluster by total node num
		graphWriter.writeGraph(fpcGraphComparator.clusterByTotalNumRank, projectName + "FPCTNNum", fpcGraphComparator.totalGraphSize);
		graphWriter.writeGraph(tpcGraphComparator.clusterByTotalNumRank, projectName + "TPCTNNum", tpcGraphComparator.totalGraphSize);
		
		//cluster by violated node
		graphWriter.writeGraphS(fpcGraphComparator.clusterByTotalNodeRank, projectName + "FPCNode", fpcGraphComparator.totalGraphSize);
		graphWriter.writeGraphS(tpcGraphComparator.clusterByTotalNodeRank, projectName + "TPCNode", tpcGraphComparator.totalGraphSize);
		
		//rank graph
		graphWriter.writeRankGraphTotalNum(fpcGraphComparator, tpcGraphComparator, projectName);			
		graphWriter.writeRankGraph(fpcGraphComparator, tpcGraphComparator, projectName);
	}
	
	public static void main(String[] args) {
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
		
		fpcGraphs = drawGraph(fpcCandidateFileName);
		tpcGraphs = drawGraph(tpcCandidateFileName);
		System.out.println("Step 3 CLEAR");
		
		//Step 4. Get Graph Information
		ArrayList<GraphInfo> fpcGraphInfos = new ArrayList<>();
		ArrayList<GraphInfo> tpcGraphInfos = new ArrayList<>();
		
		fpcGraphInfos = getGraphInfo(fpcGraphs);
		tpcGraphInfos = getGraphInfo(tpcGraphs);
		fpcGraphs = null;
		tpcGraphs = null;
		System.out.println("Step 4 Clear");
			
		//Step 5. Graph Comparison
		GraphComparator fpcGraphComparator = new GraphComparator();
		GraphComparator tpcGraphComparator = new GraphComparator();
		
		fpcGraphComparator = compareGraph(fpcGraphInfos);
		tpcGraphComparator = compareGraph(tpcGraphInfos);
		System.out.println("Step 5 Clear");
			
		//Step 6.
		clusterGraph(fpcGraphComparator, tpcGraphComparator, fpcGraphInfos, tpcGraphInfos, projectName);
		System.out.println("Step 6 Clear");
		
//			PatternVector patternVector = new PatternVector();
//			//2. build AST
//			BNFChecker tempBuilder = new BNFChecker();
//			int countB = 0;
//			for(Info info: infos) {
//				tempBuilder.run(info, patternVector);
//				countB ++;
//				System.out.println(countB);
//			}
//			GraphBuilder graph = new GraphBuilder();
//			ArrayList<GraphNode> graphs = new ArrayList<>();
//			graphs = graph.getGraph(tempBuilder.mASTs);
////			tempBuilder.getBNF(tempBuilder.mASTs);
//			tempBuilder.printPattern();
			
////////////////////////////////////////////////////////////////
			
			
			
			
			
			
//			FPCollector getFPSuspects = new FPCollector();
//			TPCollector getTP = new TPCollector();
//			
//			ContextExtractor getFalsePositiveContext = new ContextExtractor();
//			ContextExtractor getTruePositiveContext = new ContextExtractor();
//			ContextExtractor getBuggyContext = new ContextExtractor();
//			Evaluator getScore = new Evaluator();
//			PatternAdjustment adjust = new PatternAdjustment();
//			HashMap<String, Integer> tpCodeContextNodes =new HashMap<>();
//			HashMap<String, Integer> fpCodeContextNodes =new HashMap<>();
//			HashMap<String, Integer> buggyCodeContextNodes =new HashMap<>();
//		
//			ArrayList<ContextPattern> truePositivePattern = new ArrayList<>();
//			ArrayList<ContextPattern> falsePositivePattern = new ArrayList<>();
//			ArrayList<ContextPattern> buggyPattern = new ArrayList<>();
//			
//			String[] information  = getFPSuspects.initiate(args);
			
//			if(!new File(information[2]).exists()) {
//				getFPSuspects.run(information);
//			} else {
//				System.out.println("!!!!! FP Result File is Already Exists");
//			}
//			
//			if(!new File(information[2].split("\\.csv")[0] + "_TP.csv").exists()) {
//				getTP.run(information);
//			} else {
//				System.out.println("!!!!! TP Result File is Already Exists");
//			}
			
//			tpCodeContextNodes = getTruePositiveContext.getPattern(information, TRUE_POSITIVE);
//			buggyCodeContextNodes = getBuggyContext.getPattern(information, BUGGY);
//			fpCodeContextNodes = getFalsePositiveContext.getPattern(information, FALSE_POSITIVE);
//			
//			for(Map.Entry<String, Integer> temp : fpCodeContextNodes.entrySet()) {
//			//	System.out.println(temp.getKey());
//			}
//			
////			tpCodeContextNodes = adjust.adjustTP(tpCodeContextNodes, buggyCodeContextNodes);
////			buggyCodeContextNodes = adjust.adjustBP(tpCodeContextNodes, buggyCodeContextNodes);
//			
//			truePositivePattern = getTruePositiveContext.sortPattern(tpCodeContextNodes, TRUE_POSITIVE);
//			buggyPattern = getBuggyContext.sortPattern(buggyCodeContextNodes, BUGGY);
//			falsePositivePattern = getFalsePositiveContext.sortPattern(fpCodeContextNodes, FALSE_POSITIVE);
//			
//			getScore.run(truePositivePattern, falsePositivePattern, buggyPattern);
		}
}
