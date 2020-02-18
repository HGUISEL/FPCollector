package edu.handong.csee.isel.fpcollector.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class GraphWriter {
	public String fileName;
	
	public void writeGraph(ArrayList<Entry<Integer, ArrayList<GraphInfo>>> graphs, String type, int totalSize) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(Entry<Integer, ArrayList<GraphInfo>> g : graphs) {
				totalInfo += "Number of Size "+ g.getKey() + " : " + g.getValue().size() + " (" +  (double)g.getValue().size()/totalSize*100 + ")\n";
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(Entry<Integer, ArrayList<GraphInfo>> g : graphs) {
//				Integer i = g.getKey();
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for (GraphInfo tempGraph : g.getValue()) {
					String path = tempGraph.root.info.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraphS(ArrayList<Entry<String, ArrayList<GraphInfo>>> graphs, String type, int totalSize) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(Entry<String, ArrayList<GraphInfo>> g : graphs) {
				totalInfo += "Number of Size "+ g.getKey() + " : " + g.getValue().size() + " (" +  (double)g.getValue().size()/totalSize*100 + ")\n";
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(Entry<String, ArrayList<GraphInfo>> g : graphs) {
//				Integer i = g.getKey();
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for (GraphInfo tempGraph : g.getValue()) {
					String path = tempGraph.root.info.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeRankGraph(GraphComparator fpcGraphCompartor, GraphComparator tpcGraphCompartor) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + "NodeRankGraphRepresentation.csv";
		 
		 ArrayList<Entry<String, ArrayList<GraphInfo>>> fpcGraphs = fpcGraphCompartor.clusterByTotalNodeRank;
		 ArrayList<Entry<String, ArrayList<GraphInfo>>> tpcGraphs = tpcGraphCompartor.clusterByTotalNodeRank;
		 int fpcTotalSize = fpcGraphCompartor.totalGraphSize;
		 int tpcTotalSize = tpcGraphCompartor.totalGraphSize;
		 
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Information \\ EXIST", "TPC: O / FPC: O", "TPC: O / FPC: X", "TPC: X / FPC: O", "TPC: X / FPC: X"));
			) {
			String TPCFPC = "";
			String TPC = "";
			String FPC = "";
			String None = "None";
			NodeInterpreter interpreter = new NodeInterpreter();
			for (Entry<String, ArrayList<GraphInfo>> fpcGraph : fpcGraphs) {
				boolean existTPC = false;
				for (Entry<String, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
					if (tpcGraph.getKey().equals(fpcGraph.getKey())) {
						existTPC = true;
						
						TPCFPC += "Pattern : "+ interpreter.interpret(fpcGraph.getKey()) 
										+ " : (fpc: " + fpcGraph.getValue().size() 
										+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 
										+ ") (tpc: " + tpcGraph.getValue().size() 
										+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
						tpcGraphs.remove(tpcGraph);
						break;
					}
				}
				if (!existTPC) 
					FPC += "Pattern : "+ interpreter.interpret(fpcGraph.getKey()) 
								+ " : (fpc: " + fpcGraph.getValue().size() 
								+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 + ")\n";
			}
			
			for (Entry<String, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
				TPC += "Pattern : "+ interpreter.interpret(tpcGraph.getKey()) 
							+ " : (tpc: " + tpcGraph.getValue().size() 
							+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
			}
			
			csvPrinter.printRecord("Information", TPCFPC, TPC, FPC, None);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeRankGraphTotalNum(GraphComparator fpcGraphCompartor, GraphComparator tpcGraphCompartor) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + "NumRankGraphRepresentation.csv";
		 
		 ArrayList<Entry<Integer, ArrayList<GraphInfo>>> fpcGraphs = fpcGraphCompartor.clusterByTotalNumRank;
		 ArrayList<Entry<Integer, ArrayList<GraphInfo>>> tpcGraphs = tpcGraphCompartor.clusterByTotalNumRank;
		 int fpcTotalSize = fpcGraphCompartor.totalGraphSize;
		 int tpcTotalSize = tpcGraphCompartor.totalGraphSize;
		 
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Information \\ EXIST", "TPC: O / FPC: O", "TPC: O / FPC: X", "TPC: X / FPC: O", "TPC: X / FPC: X"));
			) {
			String TPCFPC = "";
			String TPC = "";
			String FPC = "";
			String None = "None";
					
			for (Entry<Integer, ArrayList<GraphInfo>> fpcGraph : fpcGraphs) {
				boolean existTPC = false;
				for (Entry<Integer, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
					if (tpcGraph.getKey().equals(fpcGraph.getKey())) {
						existTPC = true;
						TPCFPC += "Number of Size "+ fpcGraph.getKey() 
										+ " : (fpc: " + fpcGraph.getValue().size() 
										+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 
										+ ") (tpc: " + tpcGraph.getValue().size() 
										+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
						tpcGraphs.remove(tpcGraph);
						break;
					}
				}
				if (!existTPC) 
					FPC += "Number of Size "+ fpcGraph.getKey() 
								+ " : (fpc: " + fpcGraph.getValue().size() 
								+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 + ")\n";
			}
			
			for (Entry<Integer, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
				TPC += "Number of Size "+ tpcGraph.getKey() 
							+ " : (tpc: " + tpcGraph.getValue().size() 
							+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
			}
			
			csvPrinter.printRecord("Information", TPCFPC, TPC, FPC, None);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraph (HashMap<Integer, ArrayList<GraphInfo>> g, String type) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(Integer totalNodeNum : g.keySet()) {
				totalInfo += "Number of Size "+ totalNodeNum + " : " + g.get(totalNodeNum).size() + "\n";				
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(Integer totalNodeNum : g.keySet()) {
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.info.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraphS (HashMap<String, ArrayList<GraphInfo>> g) {
//		String fileName = ;/* ./Result.csv */
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			NodeInterpreter interpreter = new NodeInterpreter();
			String totalInfo = "";
			for(String totalNodeNum : g.keySet()) {
				totalInfo += "Pattern : "+ interpreter.interpret(totalNodeNum) + " : " + g.get(totalNodeNum).size() + "\n";				
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(String totalNodeNum : g.keySet()) {
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;				
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.info.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = totalNodeNum + ": " + interpreter.interpret(totalNodeNum) + "\n" + tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraph (ArrayList<ControlNode> g) {
//		String fileName = ;/* ./Result.csv */
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph"));
			) {
			String totalInfo = "";			
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(ControlNode tempGraph : g) {							
					String path = tempGraph.info.path;
					String method = tempGraph.node.toString();
					String graph = tempGraph.writeInfo();					
					
					csvPrinter.printRecord(path, method, graph);				
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
}
