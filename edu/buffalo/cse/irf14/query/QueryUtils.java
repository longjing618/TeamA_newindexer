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
	    	{//System.out.print("555555555");
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
