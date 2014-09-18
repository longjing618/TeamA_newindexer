package edu.buffalo.cse.irf14.index;

public class Term {
	private int termId;
	private int numberOfDocuments;
	private int totalCount;
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
}
