package edu.buffalo.cse.irf14.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Index {
	private HashMap<Integer, Term> indexMap = new HashMap<Integer, Term>();
	//Construct function used for deserializeBucket
	public Index(HashMap<Integer, Term> IM)
	{
		indexMap = IM;
	}
	//Default construct function
	public Index()
	{
		//indexMap = null;
	}
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
	
	public List<Posting> getPostings(String termText, TermMap termMap){
		int termId = termMap.getTermIdWithoutAdding(termText);
		if(termId == -1)
			return null;
		Term term = indexMap.get(termId);
		return term.getPostingList();
	}
	
	public List<Term> getTopK(int k){
		List<Term> termList = new ArrayList<Term>(indexMap.values());
		if(termList.isEmpty())
			return new ArrayList<Term>(0);
		Comparator<Term> termComparator = new Comparator<Term>() {

			@Override
			public int compare(Term o1, Term o2) {
				// TODO Auto-generated method stub
				Integer term1Occourances = o1.getTotalCount();
				Integer term2Occourances = o2.getTotalCount();
				return -term1Occourances.compareTo(term2Occourances);
			}
		};
		Collections.sort(termList, termComparator);
		List<Term> topKList = new ArrayList<Term>(k);
		int endIndex = termList.size() > k ? k : termList.size();
		for(int i = 0; i < endIndex; i++){
			topKList.add(termList.get(i));
		}
		return topKList;
	}
	
	public HashMap<Integer, Term> getIndexMap()
	{
		return indexMap;
	}
	public void setIndexMap(HashMap<Integer, Term> indexMap) {
		this.indexMap = indexMap;
	}
}
