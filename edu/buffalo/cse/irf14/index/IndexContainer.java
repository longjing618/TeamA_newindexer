package edu.buffalo.cse.irf14.index;

import edu.buffalo.cse.irf14.document.SerializeUtil;

public class IndexContainer {
	
	public static TermMap termTermMap = new TermMap();
	public static TermMap authorTermMap = new TermMap();
	public static TermMap placeTermMap = new TermMap();
	public static TermMap categoryTermMap = new TermMap();
	public static TermMap unstemmedTermMap = new TermMap();
	
	public static TermMap kgramTermMap = new TermMap();
	
	public static Indexer termIndexer  = new Indexer(termTermMap, "_termIndex", "termIndexer");
	public static Indexer authorIndexer = new Indexer(authorTermMap, "_authorIndex", "authorIndexer");
	public static Indexer categoryIndexer = new Indexer(categoryTermMap, "_categoryIndex", "categoryIndexer");
	public static Indexer placeIndexer = new Indexer(placeTermMap, "_placeIndex", "placeIndexer");
	
	
	//This is the k-gram indexer
	public static kgramindex kgramIndexer = new kgramindex();
	
	public static void serializeTermMap(String indexDir){
		SerializeUtil su = new SerializeUtil();
		String termTermMapFileName = indexDir + "_termTermMap";
		String authorTermMapFileName = indexDir + "_authorTermMap";
		String placeTermMapFileName = indexDir + "_placeTermMap";
		String categoryTermMapFileName = indexDir + "_categoryTermMap";
		String unstemmedTermMapFileName = indexDir + "_unstemmedTermMap";
		su.serializeTermMap(termTermMapFileName, termTermMap);
		su.serializeTermMap(authorTermMapFileName, authorTermMap);
		su.serializeTermMap(placeTermMapFileName, placeTermMap);
		su.serializeTermMap(categoryTermMapFileName, categoryTermMap);
		su.serializeTermMap(unstemmedTermMapFileName, unstemmedTermMap);
	}
	
	public static void deserializeAll(String indexDir){
		SerializeUtil su = new SerializeUtil();
		String termTermMapFileName = indexDir + "_termTermMap";
		String authorTermMapFileName = indexDir + "_authorTermMap";
		String placeTermMapFileName = indexDir + "_placeTermMap";
		String categoryTermMapFileName = indexDir + "_categoryTermMap";
		String unstemmedTermMapFileName = indexDir + "_unstemmedTermMap";
		termTermMap = su.deSerializeTermMap(termTermMapFileName);
		authorTermMap = su.deSerializeTermMap(authorTermMapFileName);
		placeTermMap = su.deSerializeTermMap(placeTermMapFileName);
		categoryTermMap = su.deSerializeTermMap(categoryTermMapFileName);
		unstemmedTermMap = su.deSerializeTermMap(unstemmedTermMapFileName);
		termIndexer  = new Indexer(termTermMap, "_termIndex", "termIndexer");
		authorIndexer = new Indexer(authorTermMap, "_authorIndex", "authorIndexer");
		categoryIndexer = new Indexer(categoryTermMap, "_categoryIndex", "categoryIndexer");
		placeIndexer = new Indexer(placeTermMap, "_placeIndex", "placeIndexer");
		termIndexer.deSerializeAll(indexDir);
		authorIndexer.deSerializeAll(indexDir);
		placeIndexer.deSerializeAll(indexDir);
		categoryIndexer.deSerializeAll(indexDir);
		//This is for kgram index
		kgramIndexer.deSerializeAll(indexDir);
		
	}

}
