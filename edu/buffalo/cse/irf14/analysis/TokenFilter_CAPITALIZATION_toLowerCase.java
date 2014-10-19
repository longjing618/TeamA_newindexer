package edu.buffalo.cse.irf14.analysis;

public class TokenFilter_CAPITALIZATION_toLowerCase extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	public TokenFilter_CAPITALIZATION_toLowerCase(TokenStream stream) {
		super(stream);
		copy = stream;
		copy.tokenList.get(0).setStartOfSentence(true);
		length = copy.tokenList.size();
	}
//	private boolean wholeSentenceInCaps = true;
//	private boolean isStartOfSentance = true;
	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();

//		if(token.isStartOfSentence())
//			isStartOfSentance = true;
		
		token.setTermText(token.getTermText().toLowerCase());
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
//	private void analyze(Token token){
//		
//		if(isStartOfSentance){
//			int strCase = isAllCapsOrCamelCased(token.getTermBuffer());
//			isStartOfSentance = false;
//			if(strCase != 1){
//				wholeSentenceInCaps = false;
//			}else{
//				wholeSentenceInCaps = isSentanceAllCaps();
//			}
//			if(wholeSentenceInCaps){
//				skipToBeginingOfNextSentance(true);
//				return;
//			}
//			if(strCase == 0){
//				token.setTermText(token.getTermText().toLowerCase());
//			}
//		}else{
//			if(token.getTermBuffer().length > 0){
//				if(isAllCaps(token.getTermBuffer())&&wholeSentenceInCaps){
//					token.setTermText(token.getTermText().toLowerCase());
//				}else if(Character.isUpperCase(token.getTermBuffer()[0])||isAllCaps(token.getTermBuffer())){
//					mergeFisrtLetterCapital(token);
//				}
//			}
//		}
//		
//		
//	}
//	
//	private void mergeFisrtLetterCapital(Token base){
//		while(copy.hasNext()){
//			Token token = copy.next();
//			if(!token.getTermText().trim().equals("")){
//			if(!(Character.isUpperCase(token.getTermBuffer()[0])||isAllCaps(token.getTermBuffer())) || token.isStartOfSentence()){
//				copy.tokenIterator.previous();
//				copy.tokenIterator.previous();
//				copy.tokenIterator.next();
//				return;
//			}else{
//				base.merge(token);
//				copy.remove();
//			}
//			}
//		}
//	}
//	private boolean isSentanceAllCaps(){
//		boolean isStentenceAllCaps = true;
//		while(copy.hasNext()){
//			Token token = copy.next();
//			if(token.isStartOfSentence())
//				break;
//			if(!isAllCaps(token.getTermBuffer())){
//				isStentenceAllCaps = false;
//				break;
//			}
//			if(token.isEndOfSentence())
//				break;
//		}
//		Token token = copy.tokenIterator.previous();
//		if(token.isStartOfSentence()&&copy.tokenIterator.hasPrevious()){
//			token = copy.tokenIterator.previous();
//			if(isStentenceAllCaps)
//				token.setTermText(token.getTermText().toLowerCase());
//		}
//		if(!copy.tokenIterator.hasPrevious())
//			return isStentenceAllCaps;
//		while(!token.isStartOfSentence()){
//			token = copy.tokenIterator.previous();
//			//token.setTermText(token.getTermText().toLowerCase());
//			if(!copy.tokenIterator.hasPrevious())
//				break;
//		}
//		token = copy.next();
//		return isStentenceAllCaps;
//	}
//	
//	private void skipToBeginingOfNextSentance(boolean convertToLowerCase){
//		Token currentToken = copy.getCurrent();
//		if(currentToken != null){
//			currentToken.setTermText(currentToken.getTermText().toLowerCase());
//		}
//		while(copy.hasNext()){
//			Token token = copy.next();
//			if(token.isStartOfSentence())
//				break;
//			if(convertToLowerCase)
//				token.setTermText(token.getTermText().toLowerCase());
//		}
//		isStartOfSentance = true;
//	}
//	
//	/*
//	 * Method to detect if a character array/string is upper cased, or camel cased
//	 * Returns 0 if normal word, 1 if all upper cased and 2 if camel cased.
//	 */
//	private int isAllCapsOrCamelCased(char [] str){
//		int numOfCaps = 0;
//		boolean isAllCaps = true;
//		for(char ch : str){
//			if(!Character.isLetter(ch))
//				continue;
//			if(Character.isUpperCase(ch)){
//				numOfCaps++;
//			}else{
//				isAllCaps = false;
//			}
//		}
//		if(isAllCaps)
//			return 1;
//		
//		if(isStartOfSentance && Character.isUpperCase(str[0]))
//			numOfCaps--;
//		if(numOfCaps > 0)
//			return 2;
//		
//		return 0;
//	}
//	
//	private boolean isAllCaps(char [] str){
//		for(char ch : str){
//			if(!Character.isLetter(ch))
//				continue;
//			if(Character.isLowerCase(ch))
//				return false;
//		}
//		return true;
//	}
}
