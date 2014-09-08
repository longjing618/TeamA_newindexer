package edu.buffalo.cse.irf14.document;

import java.util.HashMap;
import java.util.Set;

class DocumentMap
{
	private long docId = 0;
	private HashMap<Long, String> docMap;
	public DocumentMap()
	{
		docMap = new HashMap<Long, String>();
	}
	public void add(String fileId)
	{
		docMap.put(docId, fileId);
		docId++;
	}
	
	public Set<Long> getKeySet(){
		return docMap.keySet();
	}
}
