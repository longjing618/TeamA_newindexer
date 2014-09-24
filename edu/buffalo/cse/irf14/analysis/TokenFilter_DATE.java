package edu.buffalo.cse.irf14.analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFilter_DATE extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;

	String d;
	String m;
	String y;
	String t;
	char endChar;
		
	HashMap<String,String> month;
	public TokenFilter_DATE(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
		month = new HashMap<String,String>();
		month.put("January", "01");
		month.put("February", "02");
		month.put("March", "03");
		month.put("April", "04");
		month.put("May", "05");
		month.put("June", "06");
		month.put("July", "07");
		month.put("August", "08");
		month.put("September", "09");
		month.put("October", "10");
		month.put("November", "11");
		month.put("December", "12");
		
		y = "1900";
		m = "01";
		d = "01";
		t = "00";
		endChar = ' ';
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			
			//Handle month
			if(month.containsKey(currentTokenString))
			{
				currentTokenString = handleMonth(currentTokenString);
			}
			
			//Handle year 
			if(isNumber(currentTokenString) && currentTokenString.length() == 4)
			{
				currentTokenString = handleYear(currentTokenString);
			}
				
			//Handle time
			if(currentTokenString.indexOf(':') != -1)
			{
				currentTokenString = handleTime(currentTokenString);
			}
			
			//Update the current token and move the pointer to the next token
			tempToken = new Token();
			tempToken.setTermText(currentTokenString);
			copy.tokenList.set(count, tempToken);
			count++;
			return true;
		}
		else
			return false;
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	public String[] getDateArray(int monthIndex)
	{
		String tmp1;
		if(monthIndex > 0)
			tmp1 = copy.tokenList.get(monthIndex-1).toString();
		else
			tmp1 = "";
		String tmp2 = copy.tokenList.get(monthIndex+1).toString();
		String tmp3 = copy.tokenList.get(monthIndex+2).toString();
		if(tmp1 != "")
			if(endChar(tmp1.charAt(tmp1.length()-1)))
		{
			if(tmp1.length() > 4)
				endChar = tmp1.charAt(tmp1.length()-1);
			tmp1 = trim(tmp1);
		}
		if(endChar(tmp2.charAt(tmp2.length()-1)))
		{
			if(tmp2.length() > 4)
				endChar = tmp2.charAt(tmp2.length()-1);
			tmp2 = trim(tmp2);
		}
		if(endChar(tmp3.charAt(tmp3.length()-1)))
		{
			if(tmp3.length() > 4)
				endChar = tmp3.charAt(tmp3.length()-1);
			tmp3 = trim(tmp3);
		}
		String[] ret = {tmp1,tmp2,tmp3};
		return ret;
	}
	
	public String getMeridiem(int timeIndex)
	{
		return copy.tokenList.get(timeIndex + 1).toString();
	}
	
	public boolean isNumber(String str) {
	    try { 
	    	str = str.replaceAll(",", "");
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public boolean isNumberChar(char str) {
		return str >='0' && str <='9';
	}

	public String handleMonth(String currentTokenString)
	{
		endChar = ' ';
		//currentTokenString is a month string
		m = month.get(currentTokenString);
		String[] date = getDateArray(count);
		/*
		 * date[0] is the possible day
		 * date[1] is the possible day or year
		 * date[2] is the possible year
		 */
		//1 January 1978
		//December 7, 1941
		if(isNumber(date[0]))
		{
			if(isNumber(date[1]))
			{
				if(date[1].length() == 4)
				{
					d = date[0];
					y = date[1];
					copy.removeNode(count-1);
					count--;
					copy.removeNode(count+1);
					length = copy.tokenList.size();
				}
				else if(date[1].length()<4)
				{
					d = date[1];
					copy.removeNode(count+1);
					length = copy.tokenList.size();
					if(isNumber(date[2]) && trim(date[2]).length() == 4)
					{
						y = date[2];
						copy.removeNode(count+1);
						length = copy.tokenList.size();
					}
				}
			}		
		}
		else if(date[1].length()<4)
		{
			d = date[1];
			copy.removeNode(count+1);
			length = copy.tokenList.size();
			
			if(isNumber(date[2]) && trim(date[2]).length() == 4)
			{
				y = date[2];
				copy.removeNode(count+1);
				length = copy.tokenList.size();
			}	
		}
		
		currentTokenString = getDay(y,m,d);
		if(endChar != ' ')
		{
			currentTokenString += endChar;
			
		}
		return currentTokenString;
	}
	
	public String handleYear(String currentTokenString)
	{
		return currentTokenString + "0101";
	}
	
	public String handleTime(String currentTokenString)
	{
		String temp = currentTokenString.toLowerCase();
		String time = trim(temp);
		String meridiem = "";
		if(temp.indexOf("am") > -1 || temp.indexOf("pm") > -1)
		{
			String pattern = "(.*)(am|pm)";
			Pattern r = Pattern.compile(pattern);
		    Matcher m = r.matcher(temp);
		    if (m.find( ))
		    {
		    	time = m.group(1);
		    	meridiem = m.group(2);
		    }
		}
		else
		{
			meridiem = getMeridiem(count);
			meridiem = meridiem.toLowerCase();
			if(isAM(meridiem) || isPM(meridiem))
			{
				copy.removeNode(count + 1);
				length = copy.tokenList.size();
			}
		}

		temp = formatTime(time,meridiem);
		if (endChar(currentTokenString.charAt(currentTokenString.length()-1)))
			temp += currentTokenString.charAt(currentTokenString.length()-1);
		if (endChar(meridiem.charAt(meridiem.length()-1)))
			temp += meridiem.charAt(meridiem.length()-1);
				
		currentTokenString = temp;	
		return currentTokenString;
	}
	
	public String formatTime(String time, String meridiem)
	{
		String ret = "";
		int h = 0;
		int m = 0;
		int s = 0;
		String[] timecomponent = time.split(":");
		if(timecomponent.length >= 2)
		{
			h = Integer.parseInt(timecomponent[0]);
			m = Integer.parseInt(timecomponent[1]);
		}
		if(timecomponent.length == 3)
			s = Integer.parseInt(timecomponent[2]);
		if(isAM(meridiem) && h == 12)
			h = 0;
		if(isPM(meridiem) && h != 12)
			h += 12;
		ret = getTime(h,m,s);
		return ret;
	}

	public String getDay(String y, String m, String d)
	{
		String ret = "";
		ret += String.format("%04d", Integer.parseInt(y.replaceAll(",", "")));
		ret += String.format("%02d", Integer.parseInt(m.replaceAll(",", "")));
		if(d.indexOf(",") == -1)
			ret += String.format("%02d", Integer.parseInt(d));
		else
			ret += String.format("%02d", Integer.parseInt(d.replaceAll(",",""))) + ",";
		return ret;
	}
	
	public String getTime(int h, int m, int s)
	{
		String ret = "";
		ret += String.format("%02d", h);
		ret += ':';
		ret += String.format("%02d", m);
		ret += ':';
		ret += String.format("%02d", s);
			
		return ret;
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
	
	public String trim(String str)
	{
		str = str.replaceAll(",","");
		str = str.replaceAll("\\.","");
		return str;
	}
	
	public boolean endChar(char c)
	{
		if(!isChar(c) && !isNumberChar(c))
				return true;
		else
			return false;
	}
	
	public boolean isChar(char c)
	{
		return Character.isLetter(c);
	}
}
