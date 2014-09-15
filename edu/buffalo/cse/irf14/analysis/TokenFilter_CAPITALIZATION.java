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
		length = copy.tokenList.size();
	}
	private boolean wholeSentenceInCaps = false; 
	public boolean increment() throws TokenizerException
	{
		while(copy.hasNext()){
			Token token = copy.next();
			if(token.getTermText().endsWith(".")){
				copy.tokenList.get(copy.tokenIterator.nextIndex()).setStartOfSentence(true);
			}
			boolean isAllCaps = true;
			for(char chr: token.getTermBuffer()){
				if(!Character.isUpperCase(chr)){
					isAllCaps = false;
					break;
				}
			}
			if(isAllCaps && !token.isStartOfSentence()){
				token.setTermText(token.getTermText().toLowerCase());
			}else if(isAllCaps){
				//TODO: scan the entire sentence to check if all caps.
			}
		}
		
		return false;//need to discuss what to do with it
	}
	public TokenStream getStream()
	{
		return copy;
	}
}
