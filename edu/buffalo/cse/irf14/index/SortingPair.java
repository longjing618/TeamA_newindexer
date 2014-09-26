package edu.buffalo.cse.irf14.index;

public class SortingPair {
	private int docFrequency;
	private String termText;
	
	public SortingPair(int docFrequency, String termText) {
		super();
		this.docFrequency = docFrequency;
		this.termText = termText;
	}
	public int getDocFrequency() {
		return docFrequency;
	}
	public void setDocFrequency(int docFrequency) {
		this.docFrequency = docFrequency;
	}
	public String getTermText() {
		return termText;
	}
	public void setTermText(String termText) {
		this.termText = termText;
	}
	
}
