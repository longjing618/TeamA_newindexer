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
		int docId = Integer.parseInt(d.getField(FieldNames.DOCID)[0]);
		List<HashMap<String, IntegerCounter>> termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.CONTENT);
		addToIndex(docId, termMapArray, IndexContainer.termIndexer);
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.TITLE);
		addToIndex(docId, termMapArray, IndexContainer.termIndexer);
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.PLACE);
		addToIndex(docId, termMapArray, IndexContainer.placeIndexer);
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.CATEGORY);
		addToIndex(docId, termMapArray, IndexContainer.categoryIndexer);
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.AUTHOR);
		addToIndex(docId, termMapArray, IndexContainer.authorIndexer);
		//the part below can be multithreaded
		
		
	}
	
	/**
	 * Method that indicates that all open resources must be closed
	 * and cleaned and that the entire indexing operation has been completed.
	 * @throws IndexerException : In case any error occurs
	 */
	public void close() throws IndexerException {
		//TODO
		for(byte indexId = 0; indexId < 27; indexId++){
			Index index = IndexContainer.termIndexer.getIndex(indexId);
			index.sort();
			//call serialization methods here.
		}
	}
	
	private void addToIndex(int docId, List<HashMap<String, IntegerCounter>> termMapArray, Indexer indexer){
		for(byte index = 0; index < 27; index++){
			HashMap<String, IntegerCounter> termMap = termMapArray.get(index);
			for(String termText : termMap.keySet()){
				Integer termCountInDoc = termMap.get(termText).getCount();
				int termId = IndexContainer.termMap.getTermId(termText);
				Term term = new Term();
				term.setTermId(termId);
				term.setNumberOfDocuments(1);
				term.setTotalCount(termCountInDoc);
				LinkedList<Posting> postingList = new LinkedList<Posting>();
				Posting posting = new Posting();
				posting.setDocId(docId);
				posting.setTermCountInDoc(termCountInDoc);
				postingList.add(posting);
				
				term.setPostingList(postingList);
				
				IndexContainer.termIndexer.addTerm(termText, term);
				
			}
		}
	}
}
