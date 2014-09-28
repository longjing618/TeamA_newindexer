package edu.buffalo.cse.irf14.index;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Term implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8930369869009602270L;
	private int termId;
	private int arrayIndex;
	private int start;
	private int length;
	private int numberOfDocuments;
	private int totalCount;
	private List<Posting> postingList = new LinkedList<Posting>();
	//Not maintaining pointer to posting list here
	//There will be one posting list for all words starting with a single character.
	//For example, apple, Amsterdam will go to the same posting list. Need to discuss this.
	//We may use ArrayList<ArrayyList<Posting>> to do this. Or will a single ArrayList do? Also should 
	//we use a linked list instead of ArrayList?
	public int getTermId() {
		return termId;
	}
	public void setTermId(int termId) {
		this.termId = termId;
	}
	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}
	public void setNumberOfDocuments(int numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getGlobalFrequency(){
		return totalCount/numberOfDocuments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + termId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (termId != other.termId)
			return false;
		return true;
	}
	public List<Posting> getPostingList() {
		return postingList;
	}
	public void setPostingList(List<Posting> postingList) {
		this.postingList = postingList;
	}
	
	public void setStart(int s)
	{
		start = s;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public void setLength(int l)
	{
		length = l;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public void setArrayIndex(int index)
	{
		arrayIndex = index;
	}
	
	public int getArrayIndex()
	{
		return arrayIndex;
	}
}
