/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author nikhillo
 * Static parser that converts raw text to Query objects
 */
public class QueryParser {
	/**
	 * MEthod to parse the given user query into a Query object
	 * @param userQuery : The query to parse
	 * @param defaultOperator : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) {
		//TODO: YOU MUST IMPLEMENT THIS METHOD
		
		if(userQuery == null || userQuery.trim().equals(""))
			return null;
		
		String ret = new String();
		Stack<String> operatorStack = new Stack<String>();
		StringTokenizer st = new StringTokenizer(userQuery);
		String token;
		int operCount = 0;
		
		while (st.hasMoreTokens())
		{   
			 token = st.nextToken();
			 
			 if(token.equals(QueryUtils.NOT))
				 token += QueryUtils.space + st.nextToken();
			 
			 StringBuilder sbToken = new StringBuilder(token);

			 if(QueryUtils.isOperator(token) == false)
			 {
				 operCount += QueryUtils.getLeftBracketsCountandTrim(sbToken);
				 operCount -= QueryUtils.getRightBracketsCountandTrim(sbToken);
				 ret += sbToken.toString() + QueryUtils.space;
			 }
			 
			 if(QueryUtils.isOperator(sbToken.toString()))
			 {
				 if(operatorStack.empty())
					 operatorStack.push(sbToken.toString());
				 else
				 {
					 if(operCount == 0)
					 {
						 ret += operatorStack.pop() + QueryUtils.space;
						 operatorStack.push(token);
					 }
					 else
					 {
						 operatorStack.push(token);
						 operCount--;
					 }
				 }
			 }
		}
		while(operatorStack.empty() == false)
			ret += operatorStack.pop() + QueryUtils.space;
		
		return new Query(ret);
	}
}
