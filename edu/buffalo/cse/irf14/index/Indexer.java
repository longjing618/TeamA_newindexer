package edu.buffalo.cse.irf14.index;

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
	
	public void serializeBucket(byte index, String indexDir)
	{
		
	}
	
	public void deSerializeBucket(String indexDir)
	{
		
	}
}
