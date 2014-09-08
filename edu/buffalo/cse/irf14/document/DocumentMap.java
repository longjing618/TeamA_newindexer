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
	public long add(String fileId)
	{
		//docId++;
		docMap.put(++docId, fileId);
		return docId;		
	}
	
	public Set<Long> getKeySet(){
		return docMap.keySet();
	}
}
