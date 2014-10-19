package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Indexer {
	// Index buckets
	private Index aIndex = new Index();
	private Index bIndex = new Index();
	private Index cIndex = new Index();
	private Index dIndex = new Index();
	private Index eIndex = new Index();
	private Index fIndex = new Index();
	private Index gIndex = new Index();
	private Index hIndex = new Index();
	private Index iIndex = new Index();
	private Index jIndex = new Index();
	private Index kIndex = new Index();
	private Index lIndex = new Index();
	private Index mIndex = new Index();
	private Index nIndex = new Index();
	private Index oIndex = new Index();
	private Index pIndex = new Index();
	private Index qIndex = new Index();
	private Index rIndex = new Index();
	private Index sIndex = new Index();
	private Index tIndex = new Index();
	private Index uIndex = new Index();
	private Index vIndex = new Index();
	private Index wIndex = new Index();
	private Index xIndex = new Index();
	private Index yIndex = new Index();
	private Index zIndex = new Index();
	private Index otherIndex = new Index();

	private TermMap termMap;
	private String fileNameSuffix;
	public TermMap getTermMap() {
		return termMap;
	}

	public Indexer(TermMap termMap, String fileNameSuffix) {
		super();
		this.termMap = termMap;
		this.fileNameSuffix = fileNameSuffix;
	}

	private Index getIndexBucket(String term) {
		char firstLetter = term.toLowerCase().charAt(0);
		switch (firstLetter) {
		case 'a':
			return aIndex;
		case 'b':
			return bIndex;
		case 'c':
			return cIndex;
		case 'd':
			return dIndex;
		case 'e':
			return eIndex;
		case 'f':
			return fIndex;
		case 'g':
			return gIndex;
		case 'h':
			return hIndex;
		case 'i':
			return iIndex;
		case 'j':
			return jIndex;
		case 'k':
			return kIndex;
		case 'l':
			return lIndex;
		case 'm':
			return mIndex;
		case 'n':
			return nIndex;
		case 'o':
			return oIndex;
		case 'p':
			return pIndex;
		case 'q':
			return qIndex;
		case 'r':
			return rIndex;
		case 's':
			return sIndex;
		case 't':
			return tIndex;
		case 'u':
			return uIndex;
		case 'v':
			return vIndex;
		case 'w':
			return wIndex;
		case 'x':
			return xIndex;
		case 'y':
			return yIndex;
		case 'z':
			return zIndex;
		default:
			return otherIndex;
		}
	}
	
	public void addTerm(String termText, Posting posting){
		Index index = getIndexBucket(termText);
		int termId = getTermMap().getTermId(termText);
		index.add(termId, posting);
	}
	
	public void serializeAll(String indexDir){
		for(byte b = 0; b < 27; b++){
			serializeBucket(b, indexDir);
		}
	}
	public void deSerializeAll(String indexDir){
		for(byte b = 0; b < 27; b++){
			deSerializeBucket(b, indexDir);
		}
	}
	public void serializeBucket(byte i, String indexDir)
	{
		String fileName = indexDir + i + fileNameSuffix;
		Index index = getIndex(i);
		writeDisk(fileName,index.getIndexMap());
	}
	
	public void deSerializeBucket(byte i, String indexDir)
	{
		String fileName = indexDir + i + fileNameSuffix;
		HashMap<Integer, Term> indexMap = readDisk(fileName);
		Index index = getIndex(i);
		index.setIndexMap(indexMap);
	}

	public HashMap<Integer,Term> readDisk(String fileName)
	{
		HashMap<Integer, Term> indexMap = null;
	    try
	    {
	    	FileInputStream fInput = new FileInputStream(fileName);
	        ObjectInputStream oInput = new ObjectInputStream(fInput);
	        indexMap = (HashMap<Integer,Term>) oInput.readObject();
	        oInput.close();
	        fInput.close();
	        return indexMap;
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	        return null;
	    }
	    catch(ClassNotFoundException c)
	    {
	    	System.out.println("Class not found");
	    	c.printStackTrace();
	    	return null;
	    }
	}
	
	public void writeDisk(String fileName, HashMap<Integer, Term> indexMap)
	{
		try 
		{
			//Open the index file. Create the index file if does not exist. 
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
	 
            FileOutputStream fWriter = new FileOutputStream(file,false);
            ObjectOutputStream oWriter = new ObjectOutputStream(fWriter);
			oWriter.writeObject(indexMap);
			oWriter.close();
			fWriter.close();
 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Index getIndex(byte indexId){
		char ch = (char) (indexId + 96);
		return getIndexBucket(Character.toString(ch));
	}
	
	public List<Posting> getPostingList(String termText){
//		System.out.println(termText);
//		if(termText.equalsIgnoreCase("term1")){
//			int [] docIds = {1,2,3,4,5};
//			return makeDummyPostingList(docIds);
//		}else if(termText.equalsIgnoreCase("term2")){
//			int [] docIds = {3,4,5};
//			return makeDummyPostingList(docIds);
//		}else if(termText.equalsIgnoreCase("term3")){
//			int [] docIds = {1,2,3};
//			return makeDummyPostingList(docIds);
//		}else if(termText.equalsIgnoreCase("term4")){
//			int [] docIds = {3,4,5};
//			return makeDummyPostingList(docIds);
//		}else if(termText.equalsIgnoreCase("term5")){
//			int [] docIds = {4,6,7};
//			return makeDummyPostingList(docIds);
//		}else if(termText.equalsIgnoreCase("term6")){
//			int [] docIds = {11,9};
//			return makeDummyPostingList(docIds);
//		}
//		return null;
		Index index = getIndexBucket(termText);
		return index.getPostings(termText, termMap);
	}
	
	public List<String> getTopK(int k){
		List<Term> globalTopList = new ArrayList<Term>(27*k);
		for(byte i = 0; i < 27; i++){
			globalTopList.addAll(getIndex(i).getTopK(k));
		}
		Comparator<Term> termComparator = new Comparator<Term>() {

			@Override
			public int compare(Term o1, Term o2) {
				// TODO Auto-generated method stub
				Integer term1Occourances = o1.getTotalCount();
				Integer term2Occourances = o2.getTotalCount();
				return -term1Occourances.compareTo(term2Occourances);
			}
		};
		Collections.sort(globalTopList, termComparator);
		List<String> topKTerms = new ArrayList<String>(k);
		int endIndex = globalTopList.size() > k ? k : globalTopList.size();
		for(int i = 0; i < endIndex; i++){
			String termText = termMap.getTermText(globalTopList.get(i).getTermId());
			topKTerms.add(termText);
		}
		return topKTerms;
	}
	
	public int getSizeOfTermDictionary(){
		return termMap.getSize();
	}
	
	public int getSize(){
		int count = 0;
		for(byte b = 0; b < 27; b++){
			count += getIndex(b).getSize();
		}
		return count;
	}
	
	public void cleanup(){
		for(byte b = 0; b < 27; b++){
			getIndex(b).cleanup();
		}
	}
	private List<Posting> makeDummyPostingList(int [] docIds){
		ArrayList<Posting> returnList = new ArrayList<Posting>();
		for(int i : docIds){
			Posting posting = new Posting();
			posting.setDocId(i);
			returnList.add(posting);
		}
		return returnList;
	}
}
