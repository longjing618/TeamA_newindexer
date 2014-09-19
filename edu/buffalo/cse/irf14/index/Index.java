package edu.buffalo.cse.irf14.index;

import java.util.Collections;
import java.util.HashMap;

public class Index {
	private HashMap<Integer, Term> indexMap = new HashMap<Integer, Term>();
	//The term will need to be prepared earlier.
	public void add(Term term){
		if(indexMap.containsKey(term.getTermId())){
			Term existingTerm = indexMap.get(term.getTermId());
			existingTerm.getPostingList().addAll(term.getPostingList());
			existingTerm.setNumberOfDocuments(existingTerm.getNumberOfDocuments()+term.getNumberOfDocuments());
			existingTerm.setTotalCount(existingTerm.getTotalCount() + term.getTotalCount());
		}else{
			indexMap.put(term.getTermId(), term);
		}
	}
	
	public Term getTerm(Integer termId){
		return indexMap.get(termId);
	}
	
	public void sort(){
		for(Term term: indexMap.values()){
			Collections.sort(term.getPostingList());
		}
	}
}
