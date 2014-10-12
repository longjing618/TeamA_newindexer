package edu.buffalo.cse.irf14.query;

import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

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
				if(token.equals(QueryUtils.NOT))
				{
					if(st.hasMoreTokens())
					{
						token = st.nextToken();
						token = QueryUtils.fillmissing(token);
						token = QueryUtils.nleft + QueryUtils.space + token + QueryUtils.space + QueryUtils.nright;
					}
				}
				s.push(token);
			}
		}
		ret += s.pop() + " }";
		return ret;
	}
	
	//Bikram doing this
	public HashSet<Integer> getQueryDocIdSet(){
		
		return null;
	}
}
