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
	
	public TermMap getTermMap() {
		return termMap;
	}

	public Indexer(TermMap termMap) {
		super();
		this.termMap = termMap;
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
	
	public void addTerm(String termText, Term term){
		Index index = getIndexBucket(termText);
		index.add(term);
	}
	
	public void serializeBucket(byte i, String indexDir)
	{
		String fileName = indexDir + '/' + i;
		Index index = getIndex(i);
		writeDisk(fileName,index.getIndexMap());
	}
	
	public void deSerializeBucket(byte i, String indexDir)
	{
		String fileName = indexDir + '/' + i;
		HashMap<Integer, Term> indexMap = readDisk(fileName);
		Index index = getIndex(i);
		index = new Index(indexMap);
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
		Index index = getIndexBucket(termText);
		return index.getPostings(termText, termMap);
	}
	
	public List<String> getTopK(int k){
		List<Integer> globalTopList = new ArrayList<Integer>(27*k);
		for(byte i = 0; i < 27; i++){
			globalTopList.addAll(getIndex(i).getTopK(k));
		}
		Comparator<Integer> intDescComparator = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		};
		Collections.sort(globalTopList, intDescComparator);
		List<String> topKTerms = new ArrayList<String>(k);
		int endIndex = globalTopList.size() > k ? k : globalTopList.size();
		for(int i = 0; i < endIndex; i++){
			String termText = termMap.getTermText(globalTopList.get(i));
			topKTerms.add(termText);
		}
		return topKTerms;
	}
	
	public int getSizeOfTermDictionary(){
		return termMap.getSize();
	}
}
