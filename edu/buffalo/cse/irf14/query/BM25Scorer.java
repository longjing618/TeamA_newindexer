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

public class BM25Scorer 
{
	private int N;
	private float k1 = 1.5f;
	private float k3 = 1.5f;
	private float b = 0.75f;
	
	public List<DocIdScorePair> getBM25Scores(Query query, DocumentMap docmap, Set<Integer> docids)
	{
		List<DocIdScorePair> ret = new ArrayList<DocIdScorePair>();
		double partialRSV = 0;
		ArrayList<String> queryTerms = getQueryTerms(query.toString());
		int N = docmap.getSize(); //number of documents
		double docFreq; //df
		double termFreq;//tf
		int docid;
		int ld;
		float lave = docmap.getAvergeDocLength();
		List<Posting> postingslist;
		HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
		double tempscore;
		for(String queryterm : queryTerms)
		{
			postingslist = getPostingsList(queryterm);
			if(postingslist == null)
				return null;
			docFreq = postingslist.size();
			for(Posting p : postingslist)
			{
				docid = p.getDocId();
				ld = docmap.getDocLength(docid);
				termFreq = p.getTermCountInDoc();
				
				if(docids.contains(docid))
				{
					partialRSV = Math.log(N/docFreq) * ( (k1+1)*termFreq / (k1*((1-b)+b*(ld/lave))+termFreq) ) * ((k3+1)*termFreq/(k3+termFreq));
					if(temp.containsKey(docid))
					{
						tempscore = temp.get(docid);
						tempscore += partialRSV;//accumulate RSV
						temp.put(docid, tempscore);
					}
					else
						temp.put(docid, partialRSV);
				}
			}
		}

		for(int key : docids)
			ret.add(new DocIdScorePair(key,temp.get(key)));

		Collections.sort(ret);
		return ret;
	}
	
	public double getleftPart(ArrayList<String> queryTerms)
	{
		double ret = 0;
		int docFreq;
		List<Posting> postingslist;
		for(String queryterm : queryTerms)
		{
			postingslist = getPostingsList(queryterm);
			docFreq = postingslist.size();
			ret += Math.log(N/docFreq);
		}
		return ret;
	}
	
	public List<Posting> getPostingsList(String term)
	{
		//combo 0 is the index name, combo 1 is the term
		String[] combo = term.split(":");
		combo[0] = combo[0].toLowerCase();
		Indexer i;
		if(combo[0].equals("term"))
			i = IndexContainer.termIndexer;
		else if(combo[0].equals("author"))
			i = IndexContainer.authorIndexer;
		else if(combo[0].equals("place"))
			i = IndexContainer.placeIndexer;
		else if(combo[0].equals("category"))
			i = IndexContainer.categoryIndexer;
		else
			i = IndexContainer.termIndexer;
		List<Posting> postingList = i.getPostingList(combo[1]);
		return postingList;
	}
	
	public ArrayList<String> getQueryTerms(String str)
	{
		str = str.replaceAll("[AND|OR|\\[|\\(|\\{|\\}|\\)|\\]|]", "").replaceAll("<[^>*]*>", "").replaceAll("\\s+", " ").trim();
		return new ArrayList<String>(Arrays.asList(str.split(" ")));
	}
}
