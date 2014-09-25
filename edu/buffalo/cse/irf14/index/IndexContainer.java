package edu.buffalo.cse.irf14.index;

public class IndexContainer {
	
	public static TermMap termTermMap = new TermMap();
	public static TermMap authorTermMap = new TermMap();
	public static TermMap placeTermMap = new TermMap();
	public static TermMap categoryTermMap = new TermMap();
	public static Indexer termIndexer  = new Indexer(termTermMap);
	public static Indexer authorIndexer = new Indexer(authorTermMap);
	public static Indexer categoryIndexer = new Indexer(categoryTermMap);
	public static Indexer placeIndexer = new Indexer(placeTermMap);
	
}
