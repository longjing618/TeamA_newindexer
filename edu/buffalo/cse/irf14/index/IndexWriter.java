/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;

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
		if(indexDir == null)
			return;
		if(indexDir.endsWith(File.pathSeparator)){
			this.indexDir = indexDir;
		}else{
			this.indexDir = indexDir = File.pathSeparator;
		}
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
		int docId;
		if(d.getField(FieldNames.DOCID) != null){
		docId = Integer.parseInt(d.getField(FieldNames.DOCID)[0]);
		}else{
			docId = Parser.docMap.add(d.getField(FieldNames.FILEID)[0]);
		}
		
//	   Thread thread = new Thread(new indexWriterRunnable(this, FieldNames.CONTENT, d, IndexContainer.termIndexer, docId));
//	   thread.start();
		List<HashMap<String, IntegerCounter>> termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.CONTENT);
		addToIndex(docId, termMapArray, IndexContainer.termIndexer);
//	   Thread thread1 = new Thread(new indexWriterRunnable(this, FieldNames.TITLE, d, IndexContainer.termIndexer, docId));
//	   thread1.start();
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.TITLE);
		addToIndex(docId, termMapArray, IndexContainer.termIndexer);
//	   Thread thread2 = new Thread(new indexWriterRunnable(this, FieldNames.NEWSDATE, d, IndexContainer.termIndexer, docId));
//	   thread2.start();
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.NEWSDATE);
		addToIndex(docId, termMapArray, IndexContainer.termIndexer);
//	   Thread thread3 = new Thread(new indexWriterRunnable(this, FieldNames.PLACE, d, IndexContainer.placeIndexer, docId));
//	   thread3.start();
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.PLACE);
		addToIndex(docId, termMapArray, IndexContainer.placeIndexer);
//	   Thread thread4 = new Thread(new indexWriterRunnable(this, FieldNames.CATEGORY, d, IndexContainer.categoryIndexer, docId));
//	   thread4.start();
		termMapArray = IndexWriterUtil.processDocumet(d, FieldNames.CATEGORY);
		addToIndex(docId, termMapArray, IndexContainer.categoryIndexer);
//	   Thread thread5 = new Thread(new indexWriterRunnable(this, FieldNames.AUTHOR, d, IndexContainer.authorIndexer, docId));
//	   thread5.start();
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
		IndexContainer.termIndexer.cleanup();
		IndexContainer.authorIndexer.cleanup();
		IndexContainer.placeIndexer.cleanup();
		IndexContainer.categoryIndexer.cleanup();
		System.out.println(IndexContainer.termIndexer.getSizeOfTermDictionary());
		System.out.println(IndexContainer.termIndexer.getSize());
		//System.out.println(IndexContainer.termTermMap.getSortedTerms());
	}
	
	public void addToIndex(int docId, List<HashMap<String, IntegerCounter>> termMapArray, Indexer indexer){
		for(byte index = 0; index < 27; index++){
			if(termMapArray == null)
				return;
			HashMap<String, IntegerCounter> termMap = termMapArray.get(index);
			for(String termText : termMap.keySet()){
				Integer termCountInDoc = termMap.get(termText).getCount();				
//				int termId = indexer.getTermMap().getTermId(termText);
//				Term term = new Term();
//				term.setTermId(termId);
//				term.setNumberOfDocuments(1);
//				term.setTotalCount(termCountInDoc);
				
				Posting posting = new Posting();
				posting.setDocId(docId);
				posting.setTermCountInDoc(termCountInDoc);
				
				indexer.addTerm(termText, posting);
				
			}
		}
	}
}
