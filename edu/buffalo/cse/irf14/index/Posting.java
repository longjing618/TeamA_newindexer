package edu.buffalo.cse.irf14.index;

import java.io.Serializable;
import java.util.LinkedList;

public class Posting implements Serializable, Comparable<Posting>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -42264468374129378L;
	private int docId;
	//private int termId;
	//Commenting to remove positional tracking
	//private LinkedList<Integer> positionLsit = new LinkedList<Integer>(); //Need to see if we maintain positions
	private int termCountInDoc;
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
//	public int getTermId() {
//		return termId;
//	}
//	public void setTermId(int termId) {
//		this.termId = termId;
//	}
	
	//Commenting to remove positional tracking
//	public LinkedList<Integer> getPositionLsit() {
//		return positionLsit;
//	}
//	public void setPositionLsit(LinkedList<Integer> positionLsit) {
//		this.positionLsit = positionLsit;
//	}
	@Override
	public int compareTo(Posting o) {
		// TODO Auto-generated method stub
		Integer thisValue = docId;
		Integer compareValue = o.docId;
		return thisValue.compareTo(compareValue);
	}
	public int getTermCountInDoc() {
		return termCountInDoc;
	}
	public void setTermCountInDoc(int termCountInDoc) {
		this.termCountInDoc = termCountInDoc;
	}
	
	
}
