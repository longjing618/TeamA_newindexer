package edu.buffalo.cse.irf14.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

public class IndexWriterUtil {
	
	public static List<HashMap<String, Integer>> processDocumet(Document d, FieldNames filedName) throws IndexerException{
		ArrayList<HashMap<String, Integer>> termMapArray = new ArrayList<HashMap<String,Integer>>(27);
		//index 0 is for non alphabetic group
		try{
		if (d == null)
			return null;
		
		
		Tokenizer tokenizer = new Tokenizer();
		TokenStream tokenStream = tokenizer.consume(d.getField(filedName)[0]);
		Analyzer contentAnalyzer = AnalyzerFactory.getInstance().getAnalyzerForField(filedName, tokenStream);
		
		while(contentAnalyzer.increment()){
			
		}
		tokenStream.reset();
		for(int i = 0; i < 27; i++){
			termMapArray.set(i, new HashMap<String, Integer>());
		}
		while(tokenStream.hasNext()){
			Token token = tokenStream.next();
			String tokenString = token.toString();
			if(tokenString == null || tokenString.equals(""))
				continue;
			Map<String, Integer> termMap = termMapArray.get(tokenString.toLowerCase().charAt(0) - 96);
			if(termMap.containsKey(tokenString)){
				termMap.put(tokenString, termMap.get(tokenString) + 1);
			}else{
				//Commenting to remove positional tracking
				//LinkedList<Integer> positionsList = new LinkedList<Integer>();
				//positionsList.add(token.position);
				termMap.put(tokenString, 1);
			}
		}
		}catch(TokenizerException e){
			throw new IndexerException();
		}
		return termMapArray;
		
	}
}
