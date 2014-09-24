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

	public static List<HashMap<String, IntegerCounter>> processDocumet(
			Document d, FieldNames filedName) throws IndexerException {
		ArrayList<HashMap<String, IntegerCounter>> termMapArray = new ArrayList<HashMap<String, IntegerCounter>>(
				27);
		// index 0 is for non alphabetic group
		try {
			if (d == null)
				return null;

			if (d.getField(filedName) == null)
				return null;
			Tokenizer tokenizer = new Tokenizer();
			TokenStream tokenStream;
			if (filedName == FieldNames.AUTHOR) {

				TokenStream authorTokenStream = null;
				for (String str : d.getField(filedName)) {
					TokenStream tempTokenStream = tokenizer.consume(str);
					Token first = tempTokenStream.next();
					while (tempTokenStream.hasNext()) {
						Token temp = tempTokenStream.next();
						first.mergeTokens(temp);
						tempTokenStream.remove();
					}
					if (authorTokenStream == null) {
						authorTokenStream = tempTokenStream;
					} else {
						authorTokenStream.append(tempTokenStream);
					}
				}

				tokenStream = authorTokenStream;
			} else
				tokenStream = tokenizer.consume(d.getField(filedName)[0]);
			Analyzer analyzer = AnalyzerFactory.getInstance()
					.getAnalyzerForField(filedName, tokenStream);
			if (analyzer == null)
				System.out.println(filedName);
			while (analyzer.increment()) {

			}
			tokenStream.reset();
			for (int i = 0; i < 27; i++) {
				termMapArray.add(new HashMap<String, IntegerCounter>());
			}
			while (tokenStream.hasNext()) {
				Token token = tokenStream.next();
				String tokenString = token.toString();
				if (tokenString == null || tokenString.equals(""))
					continue;
				int termMapArrayIndex = 0;
				if (Character.isLetter(tokenString.toLowerCase().charAt(0)))
					termMapArrayIndex = tokenString.toLowerCase().charAt(0) - 96;
				Map<String, IntegerCounter> termMap = termMapArray
						.get(termMapArrayIndex);
				if (termMap.containsKey(tokenString)) {
					termMap.get(tokenString).incrementCounter();
				} else {
					// Commenting to remove positional tracking
					// LinkedList<Integer> positionsList = new
					// LinkedList<Integer>();
					// positionsList.add(token.position);
					termMap.put(tokenString, new IntegerCounter());
				}
			}
		} catch (TokenizerException e) {
			throw new IndexerException();
		}
		return termMapArray;

	}
}
