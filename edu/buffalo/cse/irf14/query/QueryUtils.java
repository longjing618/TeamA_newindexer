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
	    while (st.hasMoreTokens())
	    {
	    	temp = st.nextToken();

	    	if(QueryUtils.isOperator(temp) == false)
	    	{
	    		if(t && lock == false) // there is a term in front
	    			ret += AND + space;
	    		ret += temp + space;
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
}
