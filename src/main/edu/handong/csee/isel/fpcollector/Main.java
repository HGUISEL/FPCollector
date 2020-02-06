package edu.handong.csee.isel.fpcollector;

import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.isel.fpcollector.graph.ControlNode;
import edu.handong.csee.isel.fpcollector.graph.GraphBuilder;
import edu.handong.csee.isel.fpcollector.graph.GraphInfo;
import edu.handong.csee.isel.fpcollector.graph.GraphInfoGetter;
import edu.handong.csee.isel.fpcollector.graph.GraphWriter;
import edu.handong.csee.isel.fpcollector.refactoring.FPCWriter;
import edu.handong.csee.isel.fpcollector.refactoring.GitCheckout;
import edu.handong.csee.isel.fpcollector.refactoring.GitClone;
import edu.handong.csee.isel.fpcollector.refactoring.Info;
import edu.handong.csee.isel.fpcollector.refactoring.InfoCollector;
import edu.handong.csee.isel.fpcollector.refactoring.Input;
import edu.handong.csee.isel.fpcollector.refactoring.ReportComparator;
import edu.handong.csee.isel.fpcollector.refactoring.ReportReader;
import edu.handong.csee.isel.fpcollector.refactoring.RunTool;

public class Main {
	
	final static int FAILED = 0;
	final static int SUCCESS = 1;
	final static int CLONE = 2;
	final static int CHECKOUT = 3;
	
	public static void main(String[] args) {
			
//1. Preparing for Collecting False Positive Candidates
			
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
			
		System.out.println("Step 1 CLEAR");
			
//2. Collecting False Positive Candidates
			
		//1. read report of present project
		//2. read report of past project
		ReportReader currentReport = new ReportReader();
		ReportReader pastReport = new ReportReader();
			
		currentReport.readReport(runPmdOnCurrent.reportPath);
		pastReport.readReport(runPmdOnPast.reportPath);
			
		//3. compare is there anything same
		ReportComparator compareCurrentAndPast = new ReportComparator();
		compareCurrentAndPast.getFPC(currentReport, pastReport);
		
		//4. write file which contains FPC
		FPCWriter fpcWriter = new FPCWriter();
		
		if(input.rule.contains("DataflowAnomalyAnalysis"))
			fpcWriter.writeContextsForDFA(compareCurrentAndPast.FPC);
		else
			fpcWriter.writeContexts(compareCurrentAndPast.FPC);
		
		System.out.println("Step 2 CLEAR");
			
		// 3. Get Pattern of the FPC
			// 1. read input
			ArrayList<Info> infos = new ArrayList<>();
			InfoCollector inforCollector = new InfoCollector();
			try {
				infos = inforCollector.run(fpcWriter.fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int counta = 0;
			ArrayList<ControlNode> graphs = new ArrayList<>();
			// 2. build Graph
			for(Info info : infos) {
//				GraphDrawer gDrawer = new GraphDrawer();
//				counta++;
				System.out.println(infos.indexOf(info));
				GraphBuilder graph = new GraphBuilder();
//				if(counta == 1) {
					graph.run(info);
					graphs.add(graph.root);
//				}
				
//				gDrawer.run(graph.root, counta);
//				if (counta == 1) break;
			}	
			
			System.out.println("Step 3 CLEAR");
			
			//Step 4. Writing Graphs and its info in CSV file
			for(ControlNode g : graphs) {	
				g.printInfo();
				GraphInfo tempGraph = new GraphInfo(g);
				GraphInfoGetter tempGetter = new GraphInfoGetter();
				tempGetter.getNodeNum(tempGraph);
				System.out.println("DataNodeNum: " + tempGraph.dataNodeNum + "\tControlNodeNum: " + tempGraph.controlNodeNum +
						"\nSimpleName: " + tempGraph.nodeNum.get(1) + "\nThisExpression: " + tempGraph.nodeNum.get(2) + "\nDoStatement: " + tempGraph.nodeNum.get(3)
						+ "\nIfStatement: " + tempGraph.nodeNum.get(4) + "\nConditionalExpression: " + tempGraph.nodeNum.get(5) + "\nForStatement: " + tempGraph.nodeNum.get(6)
						+ "\nWhileStatement: " + tempGraph.nodeNum.get(7) + "\nEnhancedForStatement: " + tempGraph.nodeNum.get(8)+ "\nTryStatement: " + tempGraph.nodeNum.get(9)
						+"\nCatchClause: " + tempGraph.nodeNum.get(10) + "\nSwitchStatement: " + tempGraph.nodeNum.get(11) +"\nReturnStatement: " + tempGraph.nodeNum.get(12)
						+"\nThrowStatement: " + tempGraph.nodeNum.get(13));
			}
			
			GraphWriter graphWriter = new GraphWriter();
			graphWriter.writeGraph(graphs);		
			
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
