package edu.buffalo.cse.irf14.index;

import java.util.HashMap;

public class TermMap {

	private int termId = 0;
	private HashMap<String, Integer> termMap;

	public TermMap() {
		termMap = new HashMap<String, Integer>();
	}

	public int add(String term) {
		termMap.put(term, ++termId);
		return termId;
	}
	
	public int getTermId(String term){
		if(termMap.containsKey(term))
			return termMap.get(term);
		
		return add(term);
	}

}
