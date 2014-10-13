package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexContainer;
import edu.buffalo.cse.irf14.index.Indexer;
import edu.buffalo.cse.irf14.index.Posting;


/**
 * Class that represents a parsed query
 * @author nikhillo
 *
 */
public class Query {
	private String querypostfix;
	public Query(String query)
	{
		querypostfix = new String(query);
	}
	
	/**
	 * Method to convert given parsed query into string
	 */
	public String toString() {
		//TODO: YOU MUST IMPLEMENT THIS
		
		String ret = "{ ";
		String token = "";
		StringTokenizer st = new StringTokenizer(querypostfix);
		String left = "";
		String right = "";
		Stack<String> s = new Stack<String>();
		
		while (st.hasMoreTokens())
		{
			token = st.nextToken();
			
			if(token.indexOf(QueryUtils.quote) != -1)
			{
				String temp = "";
				while(st.hasMoreTokens())
				{
					temp = st.nextToken();
					token += QueryUtils.space + temp;
					if(temp.indexOf(QueryUtils.quote) != -1)
						break;
				}
			}
			
			if(QueryUtils.isOperator(token))
			{
				right = s.pop();
				left = s.pop();
				if(st.hasMoreTokens())
					s.push(QueryUtils.aleft + QueryUtils.space + left + QueryUtils.space +token + QueryUtils.space + right + QueryUtils.space + QueryUtils.aright);
				else
					s.push(left + QueryUtils.space +token + QueryUtils.space + right);	
			}
			else
			{
				if(token.indexOf(QueryUtils.NOT) != -1)
				{
					token = token.substring(0, token.indexOf(QueryUtils.NOT));
					if(st.hasMoreTokens())
					{
						token += st.nextToken();
						token = QueryUtils.fillmissing(token);
						token = QueryUtils.nleft + QueryUtils.space + token + QueryUtils.space + QueryUtils.nright;
					}
				}
				token = QueryUtils.fillmissing(token);
				s.push(token);
			}
		}
		ret += s.pop() + " }";
		return ret;
	}
	
	//Bikram doing this
	public HashSet<Integer> getQueryDocIdSet(){
		String parsedString = toString();
		final HashSet<String> operatorSet = new HashSet<String>(Arrays.asList("OR","AND"));
		final HashSet<String> closingBracketSet = new HashSet<String>(Arrays.asList("}","]",")")); 
		HashSet<Integer> docIdSet = new HashSet<Integer>();
		
		if(parsedString == null || parsedString.trim().equals("")){
			return null;
		}
		Stack<String> termStack = new Stack<String>();
		Stack<String> operatorStack = new Stack<String>();
		String [] parsedValues = parsedString.split(" ");
		for(String str : parsedValues){
			if(closingBracketSet.contains(str)){
				ArrayList<String> subQuery = getBracketedPart(termStack, operatorStack, str);
				HashSet<Integer> tempDocIds = subQueryEval(subQuery, docIdSet);
				if(docIdSet.isEmpty()){
					docIdSet.addAll(tempDocIds);
				}else{
					String operator = operatorStack.peek();
					if(operator.equals("OR")){
						docIdSet.addAll(tempDocIds);
					}else{
						//AND
						docIdSet.retainAll(tempDocIds);
					}
				}
			}else{
				if(operatorSet.contains(str)){
					operatorStack.push(str);
				}else{
					termStack.push(str);
				}
			}
		}
		
		return docIdSet;
	}
	
	private ArrayList<String> getBracketedPart(Stack<String> stack, Stack<String> operatorStack, String bracket){
		ArrayList<String> returnList = new ArrayList<String>();
		
		String openingBracket = "(";
		if(bracket.equals("}")){
			openingBracket = "{";
		}else if(bracket.equals("]")){
			openingBracket = "[";
		}
		if(stack.peek().equals(openingBracket)){
			return returnList;
		}else{
			returnList.add(stack.pop());
		}
		while(true){
			String str = stack.peek();
			if(str.equals(openingBracket)){
				stack.pop();
				stack.push("");
				break;
			}
			str = stack.pop();
			returnList.add(operatorStack.pop());
			returnList.add(str);
		}
		Collections.reverse(returnList);
		return returnList;
	}
	
	private HashSet<Integer> subQueryEval(ArrayList<String> subQuery, HashSet<Integer> docIdSet){
		HashSet<Integer> tempDocIds = new HashSet<Integer>();
		HashSet<Integer> negationList = new HashSet<Integer>();
		boolean isNot = false;
		for(int i = 0; i<subQuery.size(); i += 2){
			String term = subQuery.get(i);
			String operation = null;
			if(i > 0){
				operation = subQuery.get(i - 1);
				if(term.equals("")){					
					tempDocIds.removeAll(negationList);
					if(docIdSet.isEmpty()){
						docIdSet.addAll(tempDocIds);
						tempDocIds.clear();
						continue;
					}
					if(operation.equals("OR")){
						docIdSet.addAll(tempDocIds);
					}else{
						docIdSet.retainAll(tempDocIds);
					}
					tempDocIds.clear();
					continue;
				}
			}
			if(term.startsWith("<") && term.endsWith(">")){
				isNot = true;
				term = term.substring(1, term.length() - 1);
			}
			String index = term.substring(0, term.indexOf(":"));
			String termText = term.substring(term.indexOf(":") + 1);
			termText = getAnalyzedTerm(termText, index);
			if(termText == null || termText.equals("")){
				continue;
			}
			HashSet<Integer> termPostings = new HashSet<Integer>();
			Indexer indexer;
			if(index.equalsIgnoreCase("term")){
				indexer = IndexContainer.termIndexer;
			}else if(index.equalsIgnoreCase("author")){
				indexer = IndexContainer.authorIndexer;
			}else if(index.equalsIgnoreCase("place")){
				indexer = IndexContainer.placeIndexer;
			}else{
				//category
				indexer = IndexContainer.categoryIndexer;
			}
			List<Posting> postingList = indexer.getPostingList(termText);
			if(isNot){
				for(Posting posting : postingList){
					negationList.add(posting.getDocId());
				}
				continue;
			}
			for(Posting posting: postingList){
				termPostings.add(posting.getDocId());
			}
			if(operation == null){
				tempDocIds.addAll(termPostings);
			}else if(operation.equals("OR")){
				tempDocIds.addAll(termPostings);
			}else{
				//AND
				tempDocIds.retainAll(termPostings);
			}
		}
		tempDocIds.removeAll(negationList);
		//docIdSet.removeAll(negationList);
		return tempDocIds;
		
	}
	
	private String getAnalyzedTerm(String termText, String index){
		TokenStream stream;
		try {
			stream = new Tokenizer().consume(termText);
			Analyzer analyzer;
			if(index.equalsIgnoreCase("term")){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			}else if(index.equalsIgnoreCase("category")){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CATEGORY, stream);
			}else if(index.equalsIgnoreCase("author")){
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.AUTHOR, stream);
			}else {
				analyzer = AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.PLACE, stream);
			}
			while(analyzer.increment()){
				
			}
			stream.reset();
			if(stream.isEmpty()){
				return null;
			}else{
				return stream.next().toString();
			}
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
