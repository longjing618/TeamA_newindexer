package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFilter_SYMBOL extends TokenFilter
{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	HashMap<String, String> contraction;
	public TokenFilter_SYMBOL(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
		
		contraction = new HashMap<String,String>();
		contraction.put("ain't", "am not");
		contraction.put("aren't", "are not");
		contraction.put("can't", "cannot");
		contraction.put("could've", "could have");
		contraction.put("couldn't", "could not");
		contraction.put("couldn't've", "could not have");
		contraction.put("didn't", "did not");
		contraction.put("doesn't", "does not");
		contraction.put("don't", "do not");
		contraction.put("hadn't", "had not");
		contraction.put("hadn't've", "had not have");
		contraction.put("hasn't", "has not");
		contraction.put("haven't", "have not");
		contraction.put("he'd", "he had");
		contraction.put("he'd've", "he would have");
		contraction.put("he'll", "he will");
		contraction.put("he's", "he is");
		contraction.put("how'd", "how did");
		contraction.put("how'll", "how will");
		contraction.put("how's", "how is");
		contraction.put("I'd", "I had");
		contraction.put("I'd've", "I would have");
		contraction.put("I'll", "I will");
		contraction.put("I'm", "I am");
		contraction.put("I've", "I have");
		contraction.put("isn't", "is not");
		contraction.put("it'd", "is had");
		contraction.put("it'd've", "it would have");
		contraction.put("it'll", "it will");
		contraction.put("it's", "it is");
		contraction.put("let's", "let us");
		contraction.put("ma'am", "madam");
		contraction.put("mightn't", "moght not");
		contraction.put("mightn't've", "might not have");
		contraction.put("might've", "might have");
		contraction.put("mustn't", "must not");
		contraction.put("mustn've", "must have");
		contraction.put("needn't", "need not");
		contraction.put("not've", "not have");
		contraction.put("o'clock", "of the clock");
		contraction.put("shan't", "shall not");
		contraction.put("she'd", "she had");
		contraction.put("she'd've", "she would have");
		contraction.put("she'll", "she will");
		contraction.put("she's", "she is");
		contraction.put("should've", "should have");
		contraction.put("shouldn't", "should not");
		contraction.put("shouldn't've", "should not have");
		contraction.put("that's", "that is");
		contraction.put("there'd", "there had");
		contraction.put("there'd've", "there would have");
		contraction.put("there're", "there are");
		contraction.put("there's", "there is");
		contraction.put("they'd", "there had");
		contraction.put("they'd've", "there would have");
		contraction.put("they'll", "they will");
		contraction.put("they're", "they are");
		contraction.put("they've", "they have");
		contraction.put("wasn't", "was not");
		contraction.put("we'd", "we had");
		contraction.put("we'd've", "we would have");
		contraction.put("we'll", "we will");
		contraction.put("we're", "we are");
		contraction.put("we've", "we have");
		contraction.put("weren't", "were not");
		contraction.put("what'll", "what will");
		contraction.put("what're", "what are");
		contraction.put("what's", "what is");
		contraction.put("what've", "what have");
		contraction.put("when's", "when is");
		contraction.put("where'd", "where did");
		contraction.put("where's", "where is");
		contraction.put("where've", "where have");
		contraction.put("who'd", "who had");
		contraction.put("who'll", "who will");
		contraction.put("who're", "who are");
		contraction.put("who's", "who is");
		contraction.put("who've", "who have");
		contraction.put("why'll", "why will");
		contraction.put("why're", "why are");
		contraction.put("why's", "why is");
		contraction.put("won't", "will not");
		contraction.put("would've", "would have");
		contraction.put("wouldn't", "would not");
		contraction.put("wouldn't've", "would not have");
		contraction.put("y'all", "you all");
		contraction.put("y'all'd've", "you all should have");
		contraction.put("y'd", "you had");
		contraction.put("you'd've", "you would have");
		contraction.put("you'll", "you will");
		contraction.put("you're", "you are");
		contraction.put("you've", "you have");
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			
			//Handle contraction
			if(contraction.containsKey(currentTokenString))
				currentTokenString = contraction.get(currentTokenString);
			if(contraction.containsKey(currentTokenString.toLowerCase()))
				currentTokenString = contraction.get(currentTokenString.toLowerCase());
			
			/**
			 * Any punctuation marks that possibly mark the end of a sentence (. ! ?) should be removed.
			 * Obviously if the symbol appears within a token it should be retained (a.out for example).
			 */
			currentTokenString = currentTokenString.replaceAll("(\\.|!|\\?)+$", "");
			
			
			/**
			 * Any possessive apostrophes should be removed (‘s s’ or just ‘ at the end of a word).
			 * Common contractions should be replaced with expanded forms but treated as one token. (e.g. should’ve => should have).
			 * All other apostrophes should be removed.
			 */
			currentTokenString = currentTokenString.replaceAll("\\'s$", "");
			
			currentTokenString = currentTokenString.replaceAll("s\\'$", "s");
			currentTokenString = currentTokenString.replaceAll("\\'", "");
			
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
