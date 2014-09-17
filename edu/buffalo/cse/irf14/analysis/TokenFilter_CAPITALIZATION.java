package edu.buffalo.cse.irf14.analysis;

public class TokenFilter_CAPITALIZATION extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	public TokenFilter_CAPITALIZATION(TokenStream stream) {
		super(stream);
		copy = stream;
		copy.tokenList.get(0).setStartOfSentence(true);
		length = copy.tokenList.size();
	}
	private boolean wholeSentenceInCaps = true;
	private boolean isStartOfSentance = true;
	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();
		if(token.isStartOfSentence())
			isStartOfSentance = true;
		analyze(token);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void analyze(Token token){
		
		if(isStartOfSentance){
			int strCase = isAllCapsOrCamelCased(token.getTermBuffer());
			isStartOfSentance = false;
			if(strCase != 1){
				wholeSentenceInCaps = false;
			}else{
				wholeSentenceInCaps = isSentanceAllCaps();
			}
			if(wholeSentenceInCaps){
				skipToBeginingOfNextSentance();
				return;
			}
			if(strCase == 0){
				token.setTermText(token.getTermText().toLowerCase());
			}
		}else{
			if(isAllCaps(token.getTermBuffer())&&wholeSentenceInCaps){
				token.setTermText(token.getTermText().toLowerCase());
			}else if(Character.isUpperCase(token.getTermBuffer()[0])){
				mergeFisrtLetterCapital(token);
			}
		}
		
		
	}
	
	private void mergeFisrtLetterCapital(Token base){
		while(copy.hasNext()){
			Token token = copy.next();
			if(!Character.isUpperCase(token.getTermBuffer()[0]) || token.isStartOfSentence()){
				copy.tokenIterator.previous();
				copy.tokenIterator.previous();
				copy.tokenIterator.next();
				return;
			}else{
				base.merge(token);
				copy.remove();
			}
		}
	}
	private boolean isSentanceAllCaps(){
		boolean isStentenceAllCaps = true;
		while(copy.hasNext()){
			Token token = copy.next();
			if(token.isStartOfSentence())
				break;
			if(!isAllCaps(token.getTermBuffer())){
				isStentenceAllCaps = false;
				break;
			}
			if(token.getTermText().endsWith("."))
				break;
		}
		Token token = copy.tokenIterator.previous();
		if(token.isStartOfSentence()){
			token = copy.tokenIterator.previous();
			if(isStentenceAllCaps)
				token.setTermText(token.getTermText().toLowerCase());
		}
		while(!token.isStartOfSentence()){
			token = copy.tokenIterator.previous();
			token.setTermText(token.getTermText().toLowerCase());
		}
		token = copy.next();
		return isStentenceAllCaps;
	}
	
	private void skipToBeginingOfNextSentance(){
		while(copy.hasNext()){
			Token token = copy.next();
			if(token.isStartOfSentence())
				break;
		}
		isStartOfSentance = true;
	}
	
	/*
	 * Method to detect if a character array/string is upper cased, or camel cased
	 * Returns 0 if normal word, 1 if all upper cased and 2 if camel cased.
	 */
	private int isAllCapsOrCamelCased(char [] str){
		int numOfCaps = 0;
		boolean isAllCaps = true;
		for(char ch : str){
			if(Character.isUpperCase(ch)){
				numOfCaps++;
			}else{
				isAllCaps = false;
			}
		}
		if(isAllCaps)
			return 1;
		
		if(isStartOfSentance && Character.isUpperCase(str[0]))
			numOfCaps--;
		if(numOfCaps > 0)
			return 2;
		
		return 0;
	}
	
	private boolean isAllCaps(char [] str){
		for(char ch : str){
			if(Character.isLowerCase(ch))
				return false;
		}
		return true;
	}
}
