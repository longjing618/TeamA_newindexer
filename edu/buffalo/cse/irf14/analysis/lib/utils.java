package edu.buffalo.cse.irf14.analysis.lib;

public class utils
{
	public boolean isNumber(String str) {
	    try { 
	    	str = str.replace("AD", "");
	    	str = str.replace("BC", "");
	        Integer.parseInt(trim(str));
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public boolean isNumberChar(char str) {
		return str >='0' && str <='9';
	}
	
	public char getEndChar(String str)
	{
		if(str.length() > 0)
			return str.charAt(str.length()-1);
		return ' ';
	}
	
	public String trim(String str)
	{
		str = str.replaceAll(",","");
		str = str.replaceAll("\\.","");
		return str;
	}
	
	public boolean isChar(char c)
	{
		return Character.isLetter(c);
	}
	
	public boolean isAM(String str)
	{
		str = str.toLowerCase();
		return str.indexOf("am") != -1;
	}
	
	public boolean isPM(String str)
	{
		str = str.toLowerCase();
		return str.indexOf("pm") != -1;
	}
	
	public boolean containsAMPM(String str)
	{
		if(str.indexOf("am") > -1)
			return true;
		if(str.indexOf("pm") > -1)
			return true;
		if(str.indexOf("AM") > -1)
			return true;
		if(str.indexOf("PM") > -1)
			return true;
		return false;
	}
	
	public String getDay(String y, String m, String d)
	{
		String ret = "";
		ret += String.format("%04d", Integer.parseInt(y));
		ret += String.format("%02d", Integer.parseInt(m));
		ret += String.format("%02d", Integer.parseInt(d));
		return ret;
	}
}