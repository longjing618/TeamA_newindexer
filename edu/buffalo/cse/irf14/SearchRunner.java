package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.DocumentMap;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.document.SerializeUtil;
import edu.buffalo.cse.irf14.index.IndexContainer;
import edu.buffalo.cse.irf14.query.BM25Scorer;
import edu.buffalo.cse.irf14.query.DocIdScorePair;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;
import edu.buffalo.cse.irf14.query.QueryParserException;
import edu.buffalo.cse.irf14.query.QueryUtils;
import edu.buffalo.cse.irf14.query.TfIdfScorer;


/**
 * Main class to run the searcher.
 * As before implement all TODO methods unless marked for bonus
 * @author nikhillo
 *
 */
public class SearchRunner {
	public enum ScoringModel {TFIDF, OKAPI};
	private String indexDir;
	private String corpusDir;
	private char mode;
	private PrintStream stream;
	private DocumentMap docMap;
	
	/**
	 * Default (and only public) constuctor
	 * @param indexDir : The directory where the index resides
	 * @param corpusDir : Directory where the (flattened) corpus resides
	 * @param mode : Mode, one of Q or E
	 * @param stream: Stream to write output to
	 */
	public SearchRunner(String indexDir, String corpusDir, 
			char mode, PrintStream stream) {
		//TODO: IMPLEMENT THIS METHOD
		if(indexDir != null){
			if(indexDir.endsWith(File.separator)){
				this.indexDir = indexDir;
			}else{
				this.indexDir = indexDir + File.separator;
			}

			IndexContainer.deserializeAll(indexDir);

			IndexContainer.kgramIndexer.deSerializeAll(indexDir);

		}
		if(corpusDir != null){
			if(corpusDir.endsWith(File.separator)){
				this.corpusDir = corpusDir;
			}else{
				this.corpusDir = corpusDir + File.separator;
			}
		}
		this.mode = mode;
		this.stream = stream;
		SerializeUtil su = new SerializeUtil();
		docMap = su.deSerializeDocMap(indexDir);
	}
	
	/**
	 * Method to execute given query in the Q mode
	 * @param userQuery : Query to be parsed and executed
	 * @param model : Scoring Model to use for ranking results
	 */
	public void query(String userQuery, ScoringModel model) {
		//TODO: IMPLEMENT THIS METHOD
		try {
			Query query = QueryParser.parse(userQuery, "OR");
			long startTime = System.currentTimeMillis();
			Set<Integer> docIdSet = query.getQueryDocIdSet();
			List<DocIdScorePair> docIdScorePiarList = null;
			if(model == ScoringModel.TFIDF){
				TfIdfScorer scorer = new TfIdfScorer();
				docIdScorePiarList = scorer.getLogTfIdfScores(query, docIdSet, docMap);			
			}else if(model == ScoringModel.OKAPI)
			{
				BM25Scorer scorer = new BM25Scorer();
				docIdScorePiarList = scorer.getBM25Scores(query, docMap, docIdSet);
			}
			
			ArrayList<String> result = new ArrayList<String>();
			String firstLine = "Query: " + userQuery;
			long endTime = System.currentTimeMillis();
			long timeTaken = endTime - startTime;
			String qureyTime = "Query time: " + timeTaken;
			result.add(firstLine);
			result.add(qureyTime);
			if(docIdScorePiarList == null || docIdScorePiarList.isEmpty()){
				for(int i = 0; i<2; i++){
					stream.println(result.get(i));
				}
				stream.println("No results retrived for this query.");
				return;
			}
			int i = 0;
			for(DocIdScorePair docIdScorePair : docIdScorePiarList){
				result.add("---------------------");
				int docId = docIdScorePair.getDocId();
				String score = Double.toString(docIdScorePair.getScore());
				String fileId = docMap.getFileId(docId);
				String filePath = corpusDir + fileId;
				Document d = Parser.parse(filePath);
				String snippet = QueryUtils.getsnippets(d, userQuery);
				i++;
				String resultRank = "Result Rank: " + i;
				String resulltTitle = d.getField(FieldNames.TITLE)[0];
				String resultRelevancy = "Result relavancy: " + score;
				result.add("FileId: " + fileId);
				result.add(resultRank);
				result.add(resulltTitle);
				result.add(snippet);
				result.add(resultRelevancy);
				result.add("-------------------");
				if(i == 10){
					break;
				}
			}
			for(String str:result){
				stream.println(str);
			}
		} catch (QueryParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to execute queries in E mode
	 * @param queryFile : The file from which queries are to be read and executed
	 */
	public void query(File queryFile) {
		//TODO: IMPLEMENT THIS METHOD
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(queryFile));
			int lineNumber = 0;
			String line = "";
			List<String> queryList = new ArrayList<String>();
			List<String> results = new ArrayList<String>();
			int numberOfQueries = 0;
			while((line = br.readLine())!=null){
				if(lineNumber == 0){
					String numQ = line.split("=")[1];
					numberOfQueries = Integer.parseInt(numQ);
					lineNumber++;
					continue;
				}
				queryList.add(line);
				lineNumber++;
			}
			for(String str : queryList){
				String [] queryParts = str.split(":");
				String queryId = queryParts[0];
				String queryString = queryParts[1];
				Query query = QueryParser.parse(queryString, "OR");
				Set<Integer> docIdSet = query.getQueryDocIdSet();
				TfIdfScorer scorer = new TfIdfScorer();
				List<DocIdScorePair> docIdScoreList = scorer.getLogTfIdfScores(query, docIdSet, docMap);
				if(docIdScoreList == null || docIdScoreList.isEmpty()){
					continue;
				}
				StringBuilder sb = new StringBuilder(queryId);
				sb.append(":{");
				int i = 0;
				for(DocIdScorePair docIdScorePair : docIdScoreList){
					String fileId = docMap.getFileId(docIdScorePair.getDocId());
					String score = Double.toString(docIdScorePair.getScore());
					sb.append(fileId).append("#").append(score);
					i++;
					if(i == 10){
						break;
					}
					if(i < docIdScoreList.size()){
						sb.append(", ");
					}
				}
				sb.append("}");
				results.add(sb.toString());
			}
			String firstOutputLine = "numResults=" + results.size();
			stream.println(firstOutputLine);
			for(String str : results){
				stream.println(str);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QueryParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * General cleanup method
	 */
	public void close() {
		//TODO : IMPLEMENT THIS METHOD
	}
	
	/**
	 * Method to indicate if wildcard queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * @return A Map containing the original query term as key and list of
	 * possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		//TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;
		
	}
	
	/**
	 * Method to indicate if speel correct queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get ordered "full query" substitutions for a given misspelt query
	 * @return : Ordered list of full corrections (null if none present) for the given query
	 */
	public List<String> getCorrections() {
		//TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}

	public DocumentMap getDocMap() {
		return docMap;
	}

//	public void setDocMap(DocumentMap docMap) {
//		this.docMap = docMap;
//	}
}
