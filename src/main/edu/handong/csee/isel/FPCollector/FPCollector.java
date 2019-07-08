package FPCollector;

import init.*;

import java.util.ArrayList;

import checkOut.*;
import collector.*;

public class FPCollector{
	String project;
	String rule;
	String outputCurrentPath;
	String outputPastPath;
	String outputResultPath;
	
	public static void main(String[] args) {
		FPCollector start = new FPCollector();
		start.run(args);
	}
	
	public void run(String[] args) {
		outputCurrentPath = init(args);
		outputPastPath = checkOut(project, rule);
		collector(outputCurrentPath, outputPastPath, outputResultPath);
	}
	
	public String init(String[] list) {
		ReadFiles read = new ReadFiles();
		GitCloning clone = new GitCloning();
		RunTool tool = new RunTool();
		String filePath;
		String resultPath;
		
		project = read.readFile(list[0]);
		rule = read.readFile(list[1]);
		outputResultPath = read.readFile(list[2]);
		
		filePath = clone.gitClone(project);
		resultPath = tool.runPMD(filePath, rule);
		
		return resultPath;
	}
	
	public String checkOut(String project, String rule) {
		GitCheckOut checkOut = new GitCheckOut();
		RunTool tool = new RunTool();
		String filePath;
		String resultPath;
		
		filePath = checkOut.gitCheckOut(project);
		resultPath = tool.runPMD(filePath, rule);
				
		return resultPath;
	}
	
	public void collector(String currentPath, String pastPath, String resultPath) {
		CompareResults compare = new CompareResults();
		GetSuspects suspects = new GetSuspects();
		ArrayList<String> suspectsList = new ArrayList<>();
		
		suspectsList = compare.compareResults(currentPath, pastPath);
		suspects.writeFile(suspectsList);
	}

}
