package edu.buffalo.cse.irf14.query;

public class QueryUtils 
{
	static String AND = "AND";
	static String OR = "OR";
	static String space = " ";
	static String colon = ":";
	static String TermPrefix = "Term:";
	
	public String getAnd()
	{
		return AND;
	}
	
	public String getOr()
	{
		return OR;
	}
	
	public String getSpace()
	{
		return space;
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
