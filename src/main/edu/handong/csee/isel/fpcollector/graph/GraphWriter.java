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
	
	public void writeGraph (ArrayList<ControlNode> g) {
//		String fileName = ;/* ./Result.csv */
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Method", "Graph"));
			) {			
			for(ControlNode root : g) {
				System.out.println(g.indexOf(root));
				String Method = root.node.toString();
				String Graph = root.writeInfo();				
				csvPrinter.printRecord(Method, Graph);				
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
}
