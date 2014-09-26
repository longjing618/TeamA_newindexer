/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.PSource;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;

/**
 * @author nikhillo
 * Class that emulates reading data back from a written index
 */
public class IndexReader {
	/**
	 * Default constructor
	 * @param indexDir : The root directory from which the index is to be read.
	 * This will be exactly the same directory as passed on IndexWriter. In case 
	 * you make subdirectories etc., you will have to handle it accordingly.
	 * @param type The {@link IndexType} to read from
	 */
	private String indexDir;
	private IndexType type;
	private Indexer indexer;
	public IndexReader(String indexDir, IndexType type) {
		//TODO
		this.indexDir = indexDir;
		this.type = type;
		if(type == IndexType.TERM){
			indexer = IndexContainer.termIndexer;
		}else if(type == IndexType.PLACE){
			indexer = IndexContainer.placeIndexer;
		}else if(type == IndexType.CATEGORY){
			indexer = IndexContainer.categoryIndexer;
		}else{
			indexer = IndexContainer.authorIndexer;
		}
	}
	
	/**
	 * Get total number of terms from the "key" dictionary associated with this 
	 * index. A postings list is always created against the "key" dictionary
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		//TODO : YOU MUST IMPLEMENT THIS
		return indexer.getSizeOfTermDictionary();
	}
	
	/**
	 * Get total number of terms from the "value" dictionary associated with this 
	 * index. A postings list is always created with the "value" dictionary
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		//TODO: YOU MUST IMPLEMENT THIS
		return Parser.docMap.getSize();
	}
	
	/**
	 * Method to get the postings for a given term. You can assume that
	 * the raw string that is used to query would be passed through the same
	 * Analyzer as the original field would have been.
	 * @param term : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the 
	 * number of occurrences as values if the given term was found, null otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		//TODO:YOU MUST IMPLEMENT THIS
		Map<String, Integer> postingMap = null;
		Tokenizer tokenizer = new Tokenizer();
		try {
			TokenStream tokenStream = tokenizer.consume(term);
			Analyzer analyzer;
			if(type == IndexType.TERM){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, tokenStream);
			}else if(type == IndexType.PLACE){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.PLACE, tokenStream);
			}else if(type == IndexType.CATEGORY){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CATEGORY, tokenStream);
			}else{
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.AUTHOR, tokenStream);
			}
			while(analyzer.increment()){
				
			}
			tokenStream.reset();
			while(tokenStream.hasNext()){
				Token token = tokenStream.next();
				String tokenText = token.toString();
				List<Posting> postingList = indexer.getPostingList(tokenText);
				if(postingList == null || postingList.isEmpty())
					continue;
				if(postingMap == null){
					postingMap = new LinkedHashMap<String, Integer>();
				}
				for(Posting posting: postingList){
					String fileId = Parser.docMap.getFileId(posting.getDocId());
					postingMap.put(fileId, posting.getTermCountInDoc());
				}
				
			}
			return postingMap;
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * @param k : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k fr valid k values
	 * null for invalid k values
	 */
	public List<String> getTopK(int k) {
		//TODO YOU MUST IMPLEMENT THIS
		if(k < 1)
			return null;
		return indexer.getTopK(k);
	}
	
	/**
	 * Method to implement a simple boolean AND query on the given index
	 * @param terms The ordered set of terms to AND, similar to getPostings()
	 * the terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key 
	 * and number of occurrences as the value, the number of occurrences 
	 * would be the sum of occurrences for each participating term. return null
	 * if the given term list returns no results
	 * BONUS ONLY
	 */
	public Map<String, Integer> query(String...terms) {
		//TODO : BONUS ONLY
		return null;
	}
	
	private List<String> getTermsSortedByDocFrequency(String...terms){
		if(terms == null || terms.length == 0)
			return null;
		List<SortingPair> tempList = new ArrayList<SortingPair>();
		for(String termText : terms){
			List<Posting> postingList = indexer.getPostingList(termText);
			if(postingList == null || postingList.isEmpty()){
				continue;
			}
			SortingPair sp = new SortingPair(postingList.size(), termText);
			tempList.add(sp);
		}
		if(tempList.isEmpty())
			return null;
		Comparator<SortingPair> c = new Comparator<SortingPair>() {

			@Override
			public int compare(SortingPair o1, SortingPair o2) {
				// TODO Auto-generated method stub
				Integer i1 = o1.getDocFrequency();
				Integer i2 = o2.getDocFrequency();
				return i1.compareTo(i2);
			}
		};
		Collections.sort(tempList,c);
		List<String> returnList = new ArrayList<String>(tempList.size());
		for(SortingPair sp: tempList){
			returnList.add(sp.getTermText());
		}
		
		return returnList;
		
	}
}
