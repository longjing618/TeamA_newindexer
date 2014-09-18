package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;

public class TokenFilter_DATE extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;

	String d;
	String m;
	String y;
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
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			
			if(month.containsKey(currentTokenString))
			{
				//currentTokenString is a month string
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
						else if(date[1].length()<3)
						{
							d = date[1];
							copy.removeNode(count+1);
							length = copy.tokenList.size();
							if(isNumber(date[2]) && date[2].length() == 4)
							{
								y = date[2];
								copy.removeNode(count+2);
								length = copy.tokenList.size();
							}
						}
					}		
				}
				else if(date[1].length()<3)
				{
					d = date[1];
					if(isNumber(date[2]) && date[2].length() == 4)
						y = date[2];
				}
					
				currentTokenString = getDay(y,m,d);
				
			}
			
			//Handle year 
			
			//Handle time
			
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
		String tmp1 = copy.tokenList.get(monthIndex-1).toString().replaceAll(",", "");
		String tmp2 = copy.tokenList.get(monthIndex+1).toString().replaceAll(",", "");
		String tmp3 = copy.tokenList.get(monthIndex+2).toString().replaceAll(",","");
		String[] ret = {tmp1,tmp2,tmp3};
		return ret;
	}
	
	public boolean isNumber(String str) {
	    try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
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
