package edu.handong.csee.isel.fpcollector.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class GraphWriter {
	public String fileName;
	
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
			String totalInfo = "";
			for(String totalNodeNum : g.keySet()) {
				totalInfo += "Number of Size "+ totalNodeNum + " : " + g.get(totalNodeNum).size() + "\n";				
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(String totalNodeNum : g.keySet()) {
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				NodeInterpreter nodeInterpreter = new NodeInterpreter();
				
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.info.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = totalNodeNum + ": " + /*NodeInterpreter.interpret(totalNodeNum)*/ "\n" + tempGraph.getNumberInfo();
		
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
