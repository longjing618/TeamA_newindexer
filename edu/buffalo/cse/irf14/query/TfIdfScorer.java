package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.buffalo.cse.irf14.document.DocumentMap;
import edu.buffalo.cse.irf14.index.IndexContainer;
import edu.buffalo.cse.irf14.index.Indexer;
import edu.buffalo.cse.irf14.index.Posting;

public class TfIdfScorer {
	public List<DocIdScorePair> getLogTfIdfScores(Query query, Set<Integer> docIdSet, DocumentMap docMap){
		if(docIdSet == null || docIdSet.isEmpty()){
			return null;
		}
		ArrayList<String> queryTerms = processQueryString(query.toString());
		HashMap<Integer, Double> docIdScoreMap = new HashMap<Integer, Double>();
		ArrayList<DocIdScorePair> returnList = new ArrayList<DocIdScorePair>(docIdSet.size());
		int corpusSize = docMap.getSize();
		for(String str : queryTerms){
			List<Posting> postingsList = getPostingListForTerm(str);
			if(postingsList == null){
				continue;
			}
			double docFreq = postingsList.size();
			double idf = Math.log10((double)(corpusSize/docFreq));
			for(Posting posting:postingsList){
				if(docIdSet.contains(posting.getDocId())){
					int termFrequency = posting.getTermCountInDoc();
					int docLength = docMap.getDocLength(posting.getDocId());
					//double lengthNormalizedFreq = termFrequency/docLength;
					double tfIdfScore = (1 +Math.log10(termFrequency))*idf/docLength;
//					DocIdScorePair docIdScorePair = new DocIdScorePair(posting.getDocId(), tfIdfScore);
//					returnList.add(docIdScorePair);
					if(docIdScoreMap.containsKey(posting.getDocId())){
						double newTfIdfValues = docIdScoreMap.get(posting.getDocId()) + tfIdfScore;
						docIdScoreMap.put(posting.getDocId(), newTfIdfValues);
					}else{
						docIdScoreMap.put(posting.getDocId(), tfIdfScore);
					}
				}
			}
		}
		for(int docId : docIdScoreMap.keySet()){
			returnList.add(new DocIdScorePair(docId, docIdScoreMap.get(docId)));
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	private ArrayList<String> processQueryString(String queryString){
		//TODO: manage phrases
		String temp = queryString.replaceAll("<[^>]*>", " " ).replaceAll("[\\[\\(\\{\\}\\)\\]]", "").replaceAll("AND", "").replaceAll("OR", "").replaceAll("\\s+", " " ).trim();
		ArrayList<String> returnList = new ArrayList<String>(Arrays.asList(temp.split(" ")));
		ArrayList<String> tempList = new ArrayList<String>();
		for(int i = 0; i < returnList.size(); i++){
			String str = returnList.get(i);
			String termText = str.substring(str.indexOf(":") + 1);
			if(termText.startsWith("\"")){
				String temp1 = returnList.get(i+1);
				i++;
				str = str + " " + temp1;
			}
			tempList.add(str);
		}
//		int phraseStart = -1;
//		int phraseEnd = -1;
//		for(int i = 0; i < returnList.size(); i++){
//			String str = returnList.get(i);
//			if(str.startsWith("\"")){
//				phraseStart = i;
//			}
//			if(str.endsWith("\"")){
//				phraseEnd = i;
//				if(phraseEnd > phraseStart){
//					String tempStr = "";
//					for(int j = phraseStart; j <= phraseEnd; j++){
//						tempStr += returnList.get(j); 
//					}
//					for(int j = phraseStart + 1; j <= phraseEnd; j++){
//						returnList.remove(j); 
//					}
//				}
//			}
//		}
		return tempList;
	}
	
	private List<Posting> getPostingListForTerm(String term){
		if(term==null || term.equals(""))
			return null;
		String index = term.substring(0, term.indexOf(":"));
		String termText = term.substring(term.indexOf(":") + 1);
		termText = QueryUtils.getAnalyzedTerm(termText, index);
		if(termText == null || termText.equals(""))
			return null;
		return getIndexer(index).getPostingList(termText);
	}
	
	private Indexer getIndexer(String index){
		if(index.equalsIgnoreCase("term")){
			return IndexContainer.termIndexer;
		}else if(index.equalsIgnoreCase("author")){
			return IndexContainer.authorIndexer;
		}else if(index.equalsIgnoreCase("place")){
			return IndexContainer.placeIndexer;
		}else if(index.equalsIgnoreCase("category")){
			return IndexContainer.categoryIndexer;
		}
		
		return null;
	}
}
