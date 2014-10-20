package edu.buffalo.cse.irf14.query;

public class DocIdScorePair implements Comparable<DocIdScorePair>{
	public DocIdScorePair() {
		super();
		// TODO Auto-generated constructor stub
	}


	public DocIdScorePair(int docId, double score) {
		super();
		this.docId = docId;
		this.score = score;
	}

	private int docId;
	private double score;
	private String snippet;
	
	public DocIdScorePair(int docId, double score, String str) 
	{
		super();
		this.docId = docId;
		this.score = score;
		this.snippet = str;
	}
	
	public int getDocId() {
		return docId;
	}


	public void setDocId(int docId) {
		this.docId = docId;
	}


	public double getScore() {
		return score;
	}


	public void setScore(double score) {
		this.score = score;
	}


	@Override
	public int compareTo(DocIdScorePair o) {
		// TODO Auto-generated method stub
		Double score1 = this.score;
		Double score2 = o.score;
		return -score1.compareTo(score2);
	}
	
}
