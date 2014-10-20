package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexContainer;
import edu.buffalo.cse.irf14.index.Indexer;
import edu.buffalo.cse.irf14.index.Posting;
import edu.buffalo.cse.irf14.index.kgramindex;

public class QueryUtils 
{
	static String AND = "AND";
	static String OR = "OR";
	static String NOT = "NOT";
	static String space = " ";
	static String colon = ":";
	static String TermPrefix = "Term:";
	static String aleft = "[";
	static String aright = "]";
	static String nleft = "<";
	static String nright = ">";
	static String quote = "\"";
	static String rleft = "(";
	static String rright = ")";
	static String conbine = colon + "\\" + rleft;
	
	public static boolean isOperator(String operator)
	{
		if(operator.equals("AND") || operator.equals("OR"))
			return true;
		return false;
	}
	
	public static int getLeftBracketsCountandTrim(StringBuilder str)
	{
		if(str == null || str.toString().trim().equals(""))
			return 0;
		int length = str.length();
		int ret = 0;
		for(int i=0;i<length;i++)
			if(str.charAt(i) == '(')
				ret++;
			else
				break;
		if(ret > 0)
			str.delete(0, ret);
		return ret;
	}
	
	public static int getRightBracketsCountandTrim(StringBuilder str)
	{
		if(str == null || str.toString().trim().equals(""))
			return 0;
		int length = str.length();
		int ret = 0;
		for(int i=0;i<length;i++)
			if(str.charAt(i) == ')')
				ret++;
		str.delete(length-ret, length);
		return ret;
	}
	
	public static String fillmissing(String str)
	{
		if(str.indexOf(colon) == -1)
			str = TermPrefix + str;
		return str;
	}
	
	public static String preProcess(String str, String defaultOperator)
	{
		String ret = "";
		StringTokenizer st = new StringTokenizer(str);
	    boolean lock = false;
	    boolean t = false;
	    String temp;
	    String indexName = "";
	    boolean needAddIndexName = false;
	    String[] tsa = {""};
	    while (st.hasMoreTokens())
	    {
	    	temp = st.nextToken();
	    	if(temp.equals(NOT))
	    		if(st.hasMoreTokens())
	    			temp += space + st.nextToken() + space;
	    	
	    	//handle index name with bracket
	    	if(temp.indexOf(":(") != -1)
	    	{
	    		tsa = temp.split(":\\(");
	    		indexName = tsa[0];
	    		temp = rleft + indexName + colon + tsa[1];
	    		needAddIndexName = true;
	    		ret += temp + space;
	    		t = true;
	    		continue;
	    	}
	    	
	    	if(QueryUtils.isOperator(temp) == false)
	    	{
	    		if(t && lock == false) // there is a term in front
	    			ret += defaultOperator + space;
	    		if(needAddIndexName)
	    			ret += indexName + colon;
	    		ret += temp + space;
	    		if(needAddIndexName && temp.charAt(temp.length()-1) == ')')
	    			needAddIndexName = false;
	    		t = true;
	    	}
	    	else
	    	{
	    		ret += temp + space;
	    		t = false;
	    	}
	    	if(temp.matches("\\(*\".+"))
	    		lock = true;
	    	if(temp.matches(".+\"\\)*"))
	    		lock = false;
	    }
	    return ret;
	}
	
	public static boolean isSameIndex(String a, String b)
	{
		a = a.replace("<", "");
		b = b.replace("<", "");
		a = a.split(":")[0];
		b = b.split(":")[0];
		if(a.equals(b))
			return true;
		else
			return false;
	}

	public static String getIndexName(String str)
	{
		return str.replace("<", "").split(":")[0];
	}
	
	public static String retouch(String str)
	{
		StringBuilder sbToken = new StringBuilder();
		StringTokenizer st = new StringTokenizer(str);
		String s1;
		String s2;
		String s3;
		String currentOperator;
		
		if(st.countTokens() < 3)
			return str;
		
		s1 = st.nextToken(); // s1 is the first token
		s2 = st.nextToken(); // s2 is the operator
		s3 = st.nextToken(); // s3 is the second token
		currentOperator = s2;
		
		boolean isset = false;
		boolean isstart = true;
		do
		{
			if(isSameIndex(s1, s3))
			{
				if(currentOperator.equals(s2))
				{
					if(isset == false)
					{
						sbToken.append("[ ");
						isset = true;
					}
					else
						sbToken.append(currentOperator).append(" ");
					sbToken.append(s1).append(" ");
					currentOperator = s2;
				}
				else
				{
					if(isset == false)
					{
						
						isset = true;
						sbToken.append(currentOperator).append(" ");
						sbToken.append("[ ");
						sbToken.append(s1).append(" ");
					}
					else
					{
						sbToken.append(currentOperator).append(" ");
						sbToken.append(s1).append(" ");
						sbToken.append("] ");
						isset = false;
					}
					currentOperator = s2;
				}
			}
			else
			{
				if(isset == false)
				{
					if(isstart == false)
						sbToken.append(s2).append(" ");
					sbToken.append(s1).append(" ");
				}
				else
				{
					sbToken.append(currentOperator).append(" ");
					sbToken.append(s1).append(" ");
					sbToken.append("] ");
					sbToken.append(s2).append(" ");
					isset = false;
				}
				currentOperator = s2;
			}
			
			if(st.hasMoreTokens()) //if it is a valid query, then there must two terms behind
			{
				s1 = s3;
				s2 = st.nextToken();
				s3 = st.nextToken();
			}
			else
			{
				sbToken.append(currentOperator).append(" ");
				sbToken.append(s3);
				if(isset)
					sbToken.append(" ]");
				break;
			}
			isstart = false;
		}
		while (true);
		str = sbToken.toString();
		//System.out.print(str.length() - str.replaceAll("(\\[|\\])","").length());
		if( str.length() - str.replaceAll("(\\[|\\])","").length() == 2)
		{
			if(str.charAt(0) == '[' && str.charAt(str.length()-1) == ']')
			{	
				str = str.replaceAll("\\[ ","");
				str = str.replaceAll(" ]","");
			}
		}
		return str;
	}
	
	public static ArrayList<TermidClosenessPair> getSpellingCorrection(String queryterm)
	{
		ArrayList<TermidClosenessPair> sc = new ArrayList<TermidClosenessPair>();
		ArrayList<String> kgrams = convertToKgram(queryterm,3);
		kgramindex indexer = IndexContainer.kgramIndexer;
		LinkedList<Integer> postingList;
		HashSet<Integer> termIdSet = new HashSet<Integer>();
		for(String kgram : kgrams)
		{
			postingList = indexer.getPostingsList(kgram);
			if(termIdSet.size() == 0)
				termIdSet = new HashSet(postingList);
			else
				termIdSet.retainAll(postingList);
		}
		for(int termid : termIdSet)
		{
			sc.add(new TermidClosenessPair(termid,1));
		}
		Collections.sort(sc);
		return sc;
	}
	
	public static ArrayList<String> convertToKgram(String str,int k)
	{
		String temp;
		ArrayList<String> ret = new ArrayList<String>();
		int m;
		for(m=0;m<str.length()-k;m++)
		{
			temp = str.substring(m,m+k);
			ret.add(temp);
		}
		temp = str.substring(m);
		ret.add(temp);
		return ret;
	}
	
	public static String getsnippets(Document doc, String query)
	{
		ArrayList<SentenceMatchCountPair> sc = new ArrayList<SentenceMatchCountPair>();
		String ret = doc.getField(FieldNames.TITLE)[0];
		String body = doc.getField(FieldNames.CONTENT)[0];
		
		String[] tempsentences = body.split(" ");
		String tempsentence = "";
		ArrayList<String> sentences = new ArrayList<String>();
		for(int i=0;i<tempsentences.length-10;i=i+10)
		{
			tempsentence = "";
			for(int m=0;m<10;m++)
				tempsentence += " " + tempsentences[i+m];
			sentences.add(tempsentence);
		}
		
		tempsentence = "";
		for(int i =0;i<tempsentences.length;i++)
			tempsentence += tempsentences[i];
		sentences.add(tempsentence);
		
		for(String sentence : sentences)
		{
			sentence = sentence.toLowerCase();
			sc.add(new SentenceMatchCountPair(sentence,LCS(sentence,query)));
		}
		
		Collections.sort(sc);
		return ret + " " + sc.get(0).getsentence() + " " + sc.get(1).getsentence();
	}
	
	public static int LCS(String sentence, String query)
	{
		int ret = 0;
		String[] sentencewords = sentence.split(" ");
		String[] querywords = query.split(" ");
		int matrix[][] = new int[querywords.length+1][sentencewords.length+1];
		for(int i = 1; i<=sentencewords.length;i++)
			matrix[0][i] = 0;
		for(int i = 1; i<=querywords.length;i++)
			matrix[i][0] = 0;
		
		for(int m=0;m<querywords.length;m++)
		{
			for(int n=0;n<sentencewords.length;n++)
			{
				if(sentencewords[n].equals(querywords[m]))
					matrix[m+1][n+1] = matrix[m][n] + 1;
				else
					matrix[m+1][n+1] = Math.max(matrix[m][n+1],matrix[m+1][n]);
			}
		}
		ret = matrix[querywords.length][sentencewords.length];
		return ret;
	}
	
	public static String getAnalyzedTerm(String termText, String index){
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
	
	public static String getAnalyzedTerm(String termTextWithIndex){
		String index = termTextWithIndex.substring(0, termTextWithIndex.indexOf(":"));
		String termText = termTextWithIndex.substring(termTextWithIndex.indexOf(":") + 1);
		return getAnalyzedTerm(termText, index);
	}
}
