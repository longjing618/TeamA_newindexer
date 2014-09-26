package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.analysis.lib.utils;

public class TokenFilter_DATE extends TokenFilter{
	
	utils util;
	int length;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	boolean isDate;
	String d;
	String m;
	String y;
	String t;
	String ADBC;
	char endChar;
		
	HashMap<String,String> month;
	public TokenFilter_DATE(TokenStream stream) {
		super(stream);
		copy = stream;
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
		isDate = false;
		y = "1900";
		m = "01";
		d = "01";
		t = "00";
		endChar = ' ';
		util = new utils();
	}

	public int getType(String str)
	{
		endChar = util.getEndChar(currentTokenString);
		currentTokenString = util.trim(currentTokenString);
		if(currentTokenString.length() >=2)
			ADBC = currentTokenString.substring(currentTokenString.length()-2);
		
		if(currentTokenString.indexOf("-") != -1)
		{
			String[] multiyear = currentTokenString.split("-");
			if(multiyear.length == 2)
				if(util.isNumber(multiyear[0]) && util.isNumber(multiyear[1]))
					if(multiyear[0].length()<5 && multiyear[1].length() < 5)
						return 6;
		}
		if(month.containsKey(currentTokenString))
			return 1; //monthtype
		else if(util.isNumber(currentTokenString))
		{
			if(currentTokenString.length() == 4)
			{
				ADBC = "";
				return 2; //pure year
			}
			else if(ADBC.equals("BC") || ADBC.equals("AD"))
			{
				currentTokenString = currentTokenString.substring(0, currentTokenString.length()-2);
				return 3; //yearwithADBC
			}
			ADBC = copy.viewNext().getTermText();//handle .
			if(ADBC.equals("BC") || ADBC.equals("AD"))
			{
				copy.removeNext();
				return 4; //year and ADBC
			}
		}
		else if(currentTokenString.indexOf(':') != -1)
		{
			return 5; //time
		}
		if(endChar == ',' || endChar == '.')
			currentTokenString += endChar;
		return 0; // not a date time token
	}
	
	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();
		
		currentTokenString = token.getTermText();
		
		int type = getType(currentTokenString);
		switch(type)
		{
		case 1:
			isDate = true;
			currentTokenString = handleMonth(currentTokenString);
			break;
		case 2:
		case 3:
		case 4:
			isDate = true;
			currentTokenString = handleYear(currentTokenString,ADBC);
			break;
		case 5:
			isDate = true;
			currentTokenString = handleTime(currentTokenString); 
			break;
		case 6:
			isDate = true;
			String[] Arr = currentTokenString.split("-");
			if(Arr[1].length() == 2)
				Arr[1] = Arr[0].substring(0, 2) + Arr[1];
			currentTokenString = util.trim(handleYear(Arr[0],ADBC)) + '-' + handleYear(Arr[1],ADBC);
		}			
		
		//Update the current token and move the pointer to the next token
		token.setTermText(currentTokenString);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	public String handleMonth(String currentTokenString)
	{
		endChar = ' ';
		//currentTokenString is a month string
		m = month.get(currentTokenString);
		String[] date = getDateArray();
		/*
		 * date[0] is the possible day
		 * date[1] is the possible day or year
		 * date[2] is the possible year
		 */
		//1 January 1978
		//December 7, 1941
		if(util.isNumber(date[0]))
		{
			if(util.isNumber(date[1]))
			{
				endChar = util.getEndChar(date[1]);
				date[1] = util.trim(date[1]);
				if(date[1].length() == 4)
				{
					d = date[0];
					y = date[1];
					copy.removePrevious();
					copy.removeNext();
				}
				else if(date[1].length()<4)
				{
					d = date[1];
					copy.removeNext();
					endChar = util.getEndChar(date[2]);
					date[2] = util.trim(date[2]);
					if(util.isNumber(date[2]) && date[2].length() == 4)
					{
						y = date[2];
						copy.removeNext();
					}
				}
			}		
		}
		else if(date[1].length()<4)
		{
			date[1] = util.trim(date[1]);
			d = date[1];
			copy.removeNext();
			endChar = util.getEndChar(date[2]);
			date[2] = util.trim(date[2]);
			if(util.isNumber(date[2]) && date[2].length() == 4)
			{
				y = date[2];
				copy.removeNext();
			}	
		}
		
		currentTokenString = util.getDay(y,m,d);
		if(endChar == ',' || endChar == '.')
			currentTokenString += endChar;
		return currentTokenString;
	}
	
	public String handleYear(String currentTokenString, String ADBC)
	{
		int num = Integer.parseInt(currentTokenString);
		if(ADBC.equals("BC"))
			currentTokenString = "-" + String.format("%04d",num) + "0101";
		else
		{
			currentTokenString = String.format("%04d",num) + "0101";
		}
		if(endChar == ',' || endChar == '.')
			currentTokenString += endChar;
		return currentTokenString;
	}
	
	public String handleTime(String currentTokenString)
	{
		String time = currentTokenString;
		String meridiem = "";
		if(util.containsAMPM(time))
		{
			String pattern = "(.*)(am|pm|AM|PM)";
			Pattern r = Pattern.compile(pattern);
		    Matcher m = r.matcher(time);
		    if (m.find( ))
		    {
		    	time = m.group(1);
		    	meridiem = m.group(2);
		    }
		}
		else
		{
			meridiem = getMeridiem();
			meridiem = meridiem.toLowerCase();
			if(util.isAM(meridiem) || util.isPM(meridiem))
			{
				endChar = util.getEndChar(meridiem);
				copy.removeNext();
			}
		}

		currentTokenString = formatTime(time,meridiem);
		
		if(endChar == ',' || endChar == '.')
			currentTokenString += endChar;
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
		if(util.isAM(meridiem) && h == 12)
			h = 0;
		if(util.isPM(meridiem) && h != 12)
			h += 12;
		ret = getTime(h,m,s);
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
	
	public String[] getDateArray()
	{
		String tmp1 = "";
		if(copy.getPrevious() != null)
			tmp1 = copy.getPrevious().getTermText();
		String tmp2 = copy.viewNext().getTermText();
		String tmp3 = copy.viewNextNext().getTermText();
		String[] ret = {tmp1,tmp2,tmp3};
		return ret;
	}
	
	public String getMeridiem()
	{
		return copy.viewNext().getTermText();
	}
	
}
