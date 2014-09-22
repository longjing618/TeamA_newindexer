/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter {
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	
	private String indexDir = ""; 
	
	public IndexWriter(String indexDir) {
		this.indexDir = indexDir;
	}
	
	/**
	 * Method to add the given Document to the index
	 * This method should take care of reading the filed values, passing
	 * them through corresponding analyzers and then indexing the results
	 * for each indexable field within the document. 
	 * @param d : The Document to be added
	 * @throws IndexerException : In case any error occurs
	 * @throws TokenizerException 
	 */
	public void addDocument(Document d) throws IndexerException {
		//TODO : YOU MUST IMPLEMENT THIS
		List<HashMap<String, LinkedList<Integer>>> termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.CONTENT);
		int docId = Integer.parseInt(d.getField(FieldNames.DOCID)[0]);
		//the part below can be multithreaded
		for(byte index = 0; index < 27; index++){
			HashMap<String, LinkedList<Integer>> termMap = termMapArray.get(index);
			for(String termText : termMap.keySet()){
				LinkedList<Integer> positionLsit = termMap.get(termText);
				int termId = IndexContainer.termMap.getTermId(termText);
				Term term = new Term();
				term.setTermId(termId);
				term.setNumberOfDocuments(1);
				
				LinkedList<Posting> postingList = new LinkedList<Posting>();
				Posting posting = new Posting();
				posting.setDocId(docId);
				posting.setPositionLsit(positionLsit);
				postingList.add(posting);
				
				term.setPostingList(postingList);
				
				IndexContainer.indexer.addTerm(termText, term);
				
			}
		}
		
	}
	
	/**
	 * Method that indicates that all open resources must be closed
	 * and cleaned and that the entire indexing operation has been completed.
	 * @throws IndexerException : In case any error occurs
	 */
	public void close() throws IndexerException {
		//TODO
		
	}
}
