package edu.buffalo.cse.irf14.index;

public class IndexContainer {
	
	public static TermMap termTermMap = new TermMap();
	public static TermMap authorTermMap = new TermMap();
	public static TermMap placeTermMap = new TermMap();
	public static TermMap categoryTermMap = new TermMap();
	
	public static TermMap kgramTermMap = new TermMap();
	
	public static Indexer termIndexer  = new Indexer(termTermMap, "_termIndex");
	public static Indexer authorIndexer = new Indexer(authorTermMap, "_authorIndex");
	public static Indexer categoryIndexer = new Indexer(categoryTermMap, "_categoryIndex");
	public static Indexer placeIndexer = new Indexer(placeTermMap, "_placeIndex");
	
	//This is the k-gram indexer
	public static kgramindex kgramIndexer = new kgramindex();
}
