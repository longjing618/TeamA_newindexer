package edu.buffalo.cse.irf14.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFilter_SYMBOL extends TokenFilter
{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	public TokenFilter_SYMBOL(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			
			/**
			 * Any punctuation marks that possibly mark the end of a sentence (. ! ?) should be removed.
			 * Obviously if the symbol appears within a token it should be retained (a.out for example).
			 */
			currentTokenString = currentTokenString.replaceAll("(.|!|\\?)$", "");
			
			
			/**
			 * Any possessive apostrophes should be removed (‘s s’ or just ‘ at the end of a word).
			 * Common contractions should be replaced with expanded forms but treated as one token. (e.g. should’ve => should have).
			 * All other apostrophes should be removed.
			 */
			currentTokenString = currentTokenString.replaceAll("('s|s'|)$", "");
			currentTokenString = currentTokenString.replaceAll("'ve$", " have");
			
			
			/**
			 * If a hyphen occurs within a alphanumeric token
			 * it should be retained (B-52, at least one of the two constituents must have a number).
			 * If both are alphabetic, it should be replaced with a whitespace and retained as a single token (week-day => week day).
			 * Any other hyphens padded by spaces on either or both sides should be removed.
			 */
			//How about multiple hyphen
			String pattern = "([^-]+)-([^-]+)";
		    Pattern r = Pattern.compile(pattern);
		    Matcher m = r.matcher(currentTokenString);
		    if (m.find( )) 
		    {
		    	if(isNumber(m.group(1)) || isNumber(m.group(2)))
		    		;
		    	else currentTokenString = currentTokenString.replaceAll("-", " ");
		    }
			//Do we need to remove the space here?
			currentTokenString = currentTokenString.replaceAll("( -|- | - )", " ");
			
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
	
	public boolean isNumber(String str) {
	    try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}
