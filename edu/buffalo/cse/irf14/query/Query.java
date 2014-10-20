package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
		
		String ret = "";
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
						token = QueryUtils.nleft + token + QueryUtils.nright;
					}
				}
				token = QueryUtils.fillmissing(token);
				s.push(token);
			}
		}
		ret += s.pop();
		ret = QueryUtils.retouch(ret);
		ret ="{ " + ret + " }";
		return ret;
	}
	
	//Bikram doing this
	public HashSet<Integer> getQueryDocIdSet(){
		String parsedString = toString();
//		String parsedString = "{ [ Term:Term1 AND <Term:Term2> ] AND [ Term:Term3 AND <Term:term4> ] }";
		final HashSet<String> operatorSet = new HashSet<String>(Arrays.asList("OR","AND"));
		final HashSet<String> closingBracketSet = new HashSet<String>(Arrays.asList("}","]",")")); 
		HashSet<Integer> docIdSet = new HashSet<Integer>();
		
		if(parsedString == null || parsedString.trim().equals("")){
			return null;
		}
		Stack<String> termStack = new Stack<String>();
		Stack<String> operatorStack = new Stack<String>();
		String [] parsedValues = parsedString.split(" ");
		for(int i = 0; i<parsedValues.length;i++){
			String str = parsedValues[i];
			if(closingBracketSet.contains(str)){
				ArrayList<String> subQuery = getBracketedPart(termStack, operatorStack, str);
				HashSet<Integer> tempDocIds = subQueryEval(subQuery, docIdSet);
				if(tempDocIds == null){
					return null;
				}
				if(docIdSet.isEmpty()){
					docIdSet.addAll(tempDocIds);
				}else{
					if(operatorStack.isEmpty()){
						continue;
					}
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
					String termText = str.substring(str.indexOf(":") + 1);
					if(termText.startsWith("\"")){
						String temp = parsedValues[i + 1];
						i++;
						str = str + " " + temp;
					}
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
			if(str.endsWith("\"")){
				str = stack.pop() + " " + str;
			}
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
		String finalOperation = null;
		for(int i = 0; i<subQuery.size(); i += 2){
			String term = subQuery.get(i);
			String operation = null;
			if(i > 0){
				operation = subQuery.get(i - 1);
				if(term.equals("")){					
					if(!tempDocIds.isEmpty()){
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
					}
					continue;
				}
			}
			if(i == 0 && term.equals("")){
				if(subQuery.size() > 1){
					finalOperation = subQuery.get(i + 1);
				}
				continue;
			}
			if(term.startsWith("<") && term.endsWith(">")){
				isNot = true;
				term = term.substring(1, term.length() - 1);
			}
			String index = term.substring(0, term.indexOf(":"));
			String termText = term.substring(term.indexOf(":") + 1);
			termText = QueryUtils.getAnalyzedTerm(termText, index);
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
			List<Posting> postingList;
			if(termText.startsWith("\"")){
				//String continuation = "";
//				while(true){
//					String tempTermText = subQuery.get(i+1);
//					i++;
//					termText = termText + " " + tempTermText;
//					if(tempTermText.endsWith("\"")){
//						break;
//					}
//				}
				postingList = getPhrasedPostingList(termText, indexer);
			}else{
				postingList = indexer.getPostingList(termText);
			}
			if(postingList == null){
				return null;
			}
			if(isNot){
				for(Posting posting : postingList){
					negationList.add(posting.getDocId());
				}
				isNot = false;
				continue;
			}
//			if(postingList == null){
//				return null;
//			}
			for(Posting posting: postingList){
				termPostings.add(posting.getDocId());
			}
			if(operation == null || tempDocIds.isEmpty()){
				tempDocIds.addAll(termPostings);
			}else if(operation.equals("OR")){
				tempDocIds.addAll(termPostings);
			}else{
				//AND
				tempDocIds.retainAll(termPostings);
			}
		}
		tempDocIds.removeAll(negationList);
		if(finalOperation != null && !tempDocIds.isEmpty()){
			if(finalOperation == null || docIdSet.isEmpty()){
				docIdSet.addAll(tempDocIds);
			}else if(finalOperation.equals("OR")){
				docIdSet.addAll(tempDocIds);
			}else{
				//AND
				docIdSet.retainAll(tempDocIds);
			}
		}
		//docIdSet.removeAll(negationList);
		return tempDocIds;
		
	}
	
	
	
	private List<Posting> getPhrasedPostingList(String termText, Indexer indexer){
		termText = termText.replaceAll("\"", "");
		String [] terms = termText.split(" ");
		List<Posting> firstPosting = indexer.getPostingList(terms[0]);
		if(firstPosting == null){
			return null;
		}
		List<Posting> postingList = new LinkedList<Posting>();
		List<Posting> secondPostingList =  indexer.getPostingList(terms[1]);
		HashMap<Integer, HashSet<Integer>> secondPosting = new HashMap<Integer, HashSet<Integer>>();
		for(Posting posting:secondPostingList){
			secondPosting.put(posting.getDocId(), new HashSet<Integer>(posting.getPositionLsit()));
		}
		secondPostingList = null;
		for(Posting posting : firstPosting){
			if(secondPosting.containsKey(posting.getDocId())){
				for(int position : posting.getPositionLsit()){
					int nextPosition = position + 1;
					if(secondPosting.get(posting.getDocId()).contains(nextPosition)){
						postingList.add(posting);
						break;
					}
				}
			}
		}
		
		return postingList;
	}
}
