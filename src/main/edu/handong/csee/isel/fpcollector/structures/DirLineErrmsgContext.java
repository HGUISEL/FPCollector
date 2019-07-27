package edu.handong.csee.isel.fpcollector.structures;

import java.util.ArrayList;


/*
 * Separator : '$'
 * Quote : '#'
 */
public class DirLineErrmsgContext {
	ArrayList<String> dirLineContext;
	
	public DirLineErrmsgContext(ArrayList<ArrayList<String>> context, ArrayList<String> result){
		String contextElement = "";
		ArrayList<String> contextContainer = new ArrayList<>();
		
		System.out.println("----- Context Rearrangement is Started -----");
		
		int counter = 0;
		int index = 0;
		for(ArrayList<String> contexts : context) {
			contextElement = contextElement.concat(result.get(index).split(",")[0] + ",");
			contextElement = contextElement.concat(result.get(index).split(",")[1] + ",");
			contextElement = contextElement.concat(result.get(index).split(",")[2] + ",!@#");
			for(String line : contexts) {
				contextElement = contextElement.concat(line+"\n");
			}
			counter++;
			index++;
			if(counter == context.size() / 4) {
			System.out.println("@@@@@ 25% Complete");
			}
			if(counter == context.size() / 2) {
				System.out.println("@@@@@ 50% Complete");
			}
			if(counter == context.size() * 3 / 4) {
				System.out.println("@@@@@ 75% Complete");
			}
			contextContainer.add(contextElement);
			contextElement = "";
		}
		
		System.out.println("@@@@@ Context Rearrangement is Finished");

		this.dirLineContext = new ArrayList<>(contextContainer.size());
		this.dirLineContext.addAll(contextContainer);
	}
	
	public DirLineErrmsgContext(ArrayList<String> context){
		this.dirLineContext = context;
		
		}
		
	
	public ArrayList<String> getDirLineContext(){
		
		return dirLineContext;
	}
	
}
