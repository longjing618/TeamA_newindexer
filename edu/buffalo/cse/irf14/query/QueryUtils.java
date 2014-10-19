package edu.buffalo.cse.irf14.query;

import java.util.StringTokenizer;

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
}
