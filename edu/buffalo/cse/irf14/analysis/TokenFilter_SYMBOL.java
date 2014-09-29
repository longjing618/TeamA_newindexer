package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFilter_SYMBOL extends TokenFilter
{
	int length;
	//int count = 0;
	//String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	HashMap<String, String> contraction;
	Pattern p;
	Pattern normalWord;
	Matcher m;
	private boolean isEndOfSentence = false;
	public TokenFilter_SYMBOL(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
		normalWord = Pattern.compile("[a-zA-Z0-9]+");
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
		contraction.put("they'd", "they had");
		contraction.put("they'd", "they would");
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
		
		contraction.put("Ain't", "Am not");
		contraction.put("Aren't", "Are not");
		contraction.put("Can't", "Cannot");
		contraction.put("Could've", "Could have");
		contraction.put("Couldn't", "Could not");
		contraction.put("Couldn't've", "Could not have");
		contraction.put("Didn't", "Did not");
		contraction.put("Doesn't", "Does not");
		contraction.put("Don't", "Do not");
		contraction.put("Hadn't", "Had not");
		contraction.put("Hadn't've", "Had not have");
		contraction.put("Hasn't", "Has not");
		contraction.put("Haven't", "Have not");
		contraction.put("He'd", "He had");
		contraction.put("He'd've", "He would have");
		contraction.put("He'll", "He will");
		contraction.put("He's", "He is");
		contraction.put("How'd", "How did");
		contraction.put("How'll", "How will");
		contraction.put("How's", "How is");
		contraction.put("I'd", "I had");
		contraction.put("I'd've", "I would have");
		contraction.put("I'll", "I will");
		contraction.put("I'm", "I am");
		contraction.put("I've", "I have");
		contraction.put("Isn't", "Is not");
		contraction.put("It'd", "Is had");
		contraction.put("It'd've", "It would have");
		contraction.put("It'll", "It will");
		contraction.put("It's", "It is");
		contraction.put("Let's", "Let us");
		contraction.put("Ma'am", "Madam");
		contraction.put("Mightn't", "Moght not");
		contraction.put("Mightn't've", "Might not have");
		contraction.put("Might've", "Might have");
		contraction.put("Mustn't", "Must not");
		contraction.put("Mustn've", "Must have");
		contraction.put("Needn't", "Need not");
		contraction.put("Not've", "Not have");
		contraction.put("O'clock", "Of the clock");
		contraction.put("Shan't", "Shall not");
		contraction.put("She'd", "She had");
		contraction.put("She'd've", "She would have");
		contraction.put("She'll", "She will");
		contraction.put("She's", "She is");
		contraction.put("Should've", "Should have");
		contraction.put("Shouldn't", "Should not");
		contraction.put("Shouldn't've", "Should not have");
		contraction.put("That's", "That is");
		contraction.put("There'd", "There had");
		contraction.put("There'd've", "There would have");
		contraction.put("There're", "There are");
		contraction.put("There's", "There is");
		contraction.put("They'd", "They had");
		contraction.put("They'd", "They would");
		contraction.put("They'd've", "There would have");
		contraction.put("They'll", "They will");
		contraction.put("They're", "They are");
		contraction.put("They've", "They have");
		contraction.put("Wasn't", "Was not");
		contraction.put("We'd", "We had");
		contraction.put("We'd've", "We would have");
		contraction.put("We'll", "We will");
		contraction.put("We're", "We are");
		contraction.put("We've", "We have");
		contraction.put("Weren't", "Were not");
		contraction.put("What'll", "What will");
		contraction.put("What're", "What are");
		contraction.put("What's", "What is");
		contraction.put("What've", "What have");
		contraction.put("When's", "When is");
		contraction.put("Where'd", "Where did");
		contraction.put("Where's", "Where is");
		contraction.put("Where've", "Where have");
		contraction.put("Who'd", "Who had");
		contraction.put("Who'll", "Who will");
		contraction.put("Who're", "Who are");
		contraction.put("Who's", "Who is");
		contraction.put("Who've", "Who have");
		contraction.put("Why'll", "Why will");
		contraction.put("Why're", "Why are");
		contraction.put("Why's", "Why is");
		contraction.put("Won't", "Will not");
		contraction.put("Would've", "Would have");
		contraction.put("Wouldn't", "Would not");
		contraction.put("Wouldn't've", "Would not have");
		contraction.put("Y'all", "You all");
		contraction.put("Y'all'd've", "You all should have");
		contraction.put("Y'd", "You had");
		contraction.put("You'd've", "You would have");
		contraction.put("You'll", "You will");
		contraction.put("You're", "You are");
		contraction.put("You've", "You have");
	}

	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();
		removeSymbol(token);
		return copy.hasNext();
	}
	
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void removeSymbol(Token token){
		if(isEndOfSentence){
			token.setStartOfSentence(true);
			isEndOfSentence = false;
		}
		
		String currentTokenString = token.getTermText();
//		Matcher matcher = normalWord.matcher(currentTokenString);
//		if(matcher.matches())
//			return;
		boolean shouldProcess = false;
		for(char ch : token.getTermBuffer()){
			if(!Character.isLetterOrDigit(ch)){
				shouldProcess = true;
				break;
			}
		}
		if(!shouldProcess)
			return;
		//Handle contraction
		if(contraction.containsKey(currentTokenString))
			currentTokenString = contraction.get(currentTokenString);
		if(contraction.containsKey(currentTokenString.toLowerCase()))
			currentTokenString = contraction.get(currentTokenString.toLowerCase());
		
		/**
		 * Any punctuation marks that possibly mark the end of a sentence (. ! ?) should be removed.
		 * Obviously if the symbol appears within a token it should be retained (a.out for example).
		 */
		//Setting end of line flag
		if(currentTokenString.endsWith(".")||currentTokenString.endsWith("!")||currentTokenString.endsWith("?")){
			token.setEndOfSentence(true);
			isEndOfSentence = true;
		}
		currentTokenString = currentTokenString.replaceAll("(\\.|!|\\?)+$", "");
		
		currentTokenString = currentTokenString.replaceAll(" \\'ve$", " have");
		currentTokenString = currentTokenString.replaceAll("\\'em$", "them");
		
		/**
		 * Any possessive apostrophes should be removed (‘s s’ or just ‘ at the end of a word).
		 * Common contractions should be replaced with expanded forms but treated as one token. (e.g. should’ve => should have).
		 * All other apostrophes should be removed.
		 */
		currentTokenString = currentTokenString.replaceAll("\\'s$", "");
		
		currentTokenString = currentTokenString.replaceAll("s\\'$", "s");
		currentTokenString = currentTokenString.replaceAll("\\'", "");
		
		/**
		 * If a hyphen occurs within a alphanumeric token
		 * it should be retained (B-52, at least one of the two constituents must have a number).
		 * If both are alphabetic, it should be replaced with a whitespace and retained as a single token (week-day => week day).
		 * Any other hyphens padded by spaces on either or both sides should be removed.
		 */
		//How about multiple hyphen
		String pattern = "(.+)-(.+)";
	    Pattern r = Pattern.compile(pattern);

	    currentTokenString = handleHyphen(r,currentTokenString);
		
		//Update the current token and move the pointer to the next token
		token.setTermText(currentTokenString);
	}
	
	public static String handleHyphen(Pattern r, String currentTokenString)
	{
	    Matcher m = r.matcher(currentTokenString);
	    if (m.find( )) 
	    {
	    	if(m.group(1).matches(".*\\d.*") || m.group(2).matches(".*\\d.*"))
	    	{
	    		currentTokenString = handleHyphen(r,m.group(1)) + '-' + handleHyphen(r,m.group(2));
	    	}
	    	else
	    	{
	    		currentTokenString = handleHyphen(r,m.group(1)) + ' ' + handleHyphen(r,m.group(2));
	    	}
	    }
	    else
	    {
		    currentTokenString = currentTokenString.replaceAll("\\s?-\\s?", "");
	    }
	    return currentTokenString;
	}
	
}
