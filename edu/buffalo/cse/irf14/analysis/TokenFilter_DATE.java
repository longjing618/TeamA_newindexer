package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;

public class TokenFilter_DATE extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	String defaultyear;
	HashMap<String,String> month;
	public TokenFilter_DATE(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
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
		
		defaultyear = "1900";
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			
			String[] date = currentTokenString.split(" ");
			if(month.containsKey(date[0]))
			{
				currentTokenString = "";
				currentTokenString += month.get(date[0]);
				date[1] = date[1].replace(",",""); //trim the comma Ex. January 1, 1900
				currentTokenString += String.format("%02d", Integer.parseInt(date[1]));
				if(date.length == 3)
					currentTokenString += date[2];
				else
					currentTokenString += defaultyear;
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
}
