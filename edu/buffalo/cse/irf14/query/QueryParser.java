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
	
	static String AND = "AND";
	static String OR = "OR";
	static String space = " ";
	
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
			 StringBuilder sbToken = new StringBuilder(token);

			 if(isOperator(token) == false)
			 {
				 operCount += getLeftBracketsCountandTrim(sbToken);
				 operCount -= getRightBracketsCountandTrim(sbToken);
				 ret += sbToken.toString() + space;
			 }
			 
			 if(isOperator(sbToken.toString()))
			 {
				 if(operatorStack.empty())
					 operatorStack.push(sbToken.toString());
				 else
				 {
					 if(operCount == 0)
					 {
						 ret += operatorStack.pop() + space;
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
			ret += operatorStack.pop() + space;
		
		return new Query(ret);
	}
		
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
}
