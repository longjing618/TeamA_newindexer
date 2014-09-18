package edu.buffalo.cse.irf14.index;

import java.io.Serializable;

public class Posting implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -42264468374129378L;
	private int docId;
	private int termId;
	private int position; //Need to see if we maintain positions
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getTermId() {
		return termId;
	}
	public void setTermId(int termId) {
		this.termId = termId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
}
