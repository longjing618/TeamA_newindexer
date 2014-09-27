package edu.buffalo.cse.irf14.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TermMap {

	private int termId = 0;
	private HashMap<String, Integer> termMap;
	//private HashMap<Integer, String> reverseMap;
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
	
	public int getTermIdWithoutAdding(String term){
		if(termMap.containsKey(term))
			return termMap.get(term);
		
		return -1;
	}

	public int getSize(){
		return termId;
	}
	
	public String getTermText(int termId){
		for(String str : termMap.keySet()){
			if(termMap.get(str) == termId)
				return str;
		}
		return null;
	}
	
	public List<String> getSortedTerms(){
		List<String> terms = new ArrayList<String>(termMap.keySet());
		Collections.sort(terms);
		return terms;
	}
}
