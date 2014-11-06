package edu.buffalo.cse.irf14.index;

import edu.buffalo.cse.irf14.document.SerializeUtil;

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
	
	public static void serializeTermMap(String indexDir){
		SerializeUtil su = new SerializeUtil();
		String termTermMapFileName = indexDir + "_termTermMap";
		String authorTermMapFileName = indexDir + "_authorTermMap";
		String placeTermMapFileName = indexDir + "_placeTermMap";
		String categoryTermMapFileName = indexDir + "_categoryTermMap";
		su.serializeTermMap(termTermMapFileName, termTermMap);
		su.serializeTermMap(authorTermMapFileName, authorTermMap);
		su.serializeTermMap(placeTermMapFileName, placeTermMap);
		su.serializeTermMap(categoryTermMapFileName, categoryTermMap);
	}
	
	public static void deserializeAll(String indexDir){
		SerializeUtil su = new SerializeUtil();
		String termTermMapFileName = indexDir + "_termTermMap";
		String authorTermMapFileName = indexDir + "_authorTermMap";
		String placeTermMapFileName = indexDir + "_placeTermMap";
		String categoryTermMapFileName = indexDir + "_categoryTermMap";
		termTermMap = su.deSerializeTermMap(termTermMapFileName);
		authorTermMap = su.deSerializeTermMap(authorTermMapFileName);
		placeTermMap = su.deSerializeTermMap(placeTermMapFileName);
		categoryTermMap = su.deSerializeTermMap(categoryTermMapFileName);
		termIndexer  = new Indexer(termTermMap, "_termIndex");
		authorIndexer = new Indexer(authorTermMap, "_authorIndex");
		categoryIndexer = new Indexer(categoryTermMap, "_categoryIndex");
		placeIndexer = new Indexer(placeTermMap, "_placeIndex");
		termIndexer.deSerializeAll(indexDir);
		authorIndexer.deSerializeAll(indexDir);
		placeIndexer.deSerializeAll(indexDir);
		authorIndexer.deSerializeAll(indexDir);
	
		//This is for kgram index
		kgramIndexer.deSerializeAll(indexDir);
	}

}
