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
	public String fileName ="./GraphRepresentation.csv";
	
	public void writeGraph (HashMap<Integer, ArrayList<GraphInfo>> g) {
//		String fileName = ;/* ./Result.csv */
		
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
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.path;
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
	
}
