package edu.buffalo.cse.irf14.document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class DocumentMap implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5212341231705427422L;
	private int docId = 0;
	private HashMap<Integer, DocumentInfo> docMap;
	public DocumentMap()
	{
		docMap = new HashMap<Integer, DocumentInfo>();
	}
	public int add(String fileId)
	{
		//docId++;
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.setFileId(fileId);
		docMap.put(++docId, docInfo);
		return docId;		
	}
	
	public Set<Integer> getKeySet(){
		return docMap.keySet();
	}
	
	public String getFileId(int docId){
		return docMap.get(docId).getFileId();
	}
	
	public int getSize(){
		return docId;
	}
	
	public void addLengthToDoc(int docId, int length){
		docMap.get(docId).setLength(length);
	}
	
	public int getDocLength(int docId){
		if(docMap.containsKey(docId)){
			return docMap.get(docId).getLength();
		}
		return -1;
	}
}
