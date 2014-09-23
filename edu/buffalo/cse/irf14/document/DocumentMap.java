package edu.buffalo.cse.irf14.document;

import java.util.HashMap;
import java.util.Set;

public class DocumentMap
{
	private int docId = 0;
	private HashMap<Integer, String> docMap;
	public DocumentMap()
	{
		docMap = new HashMap<Integer, String>();
	}
	public int add(String fileId)
	{
		//docId++;
		docMap.put(++docId, fileId);
		return docId;		
	}
	
	public Set<Integer> getKeySet(){
		return docMap.keySet();
	}
	
	public String getFileId(int docId){
		return docMap.get(docId);
	}
}
