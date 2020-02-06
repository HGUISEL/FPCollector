package edu.handong.csee.isel.fpcollector.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class GraphWriter {
	public String fileName ="./GraphRepresentation.csv";
	
	public void writeGraph (ArrayList<GraphInfo> g) {
//		String fileName = ;/* ./Result.csv */
		int count = 0;
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Method", "Graph", "Graph Information"));
			) {			
			for(GraphInfo tempGraph : g) {
				count++;
				String method = tempGraph.root.node.toString();
				String graph = tempGraph.root.writeInfo();
				String graphInfo = tempGraph.getNumberInfo();
				csvPrinter.printRecord(method, graph, graphInfo);				
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
}
