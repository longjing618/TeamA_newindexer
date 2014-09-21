package edu.buffalo.cse.irf14.analysis;

public class TokenFilter_SPECIALCHARS extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	public TokenFilter_SPECIALCHARS(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
//		if(count < length)
//		{
//			currentTokenString = copy.tokenList.get(count).toString();
//			currentTokenString.replaceAll("[^\\w\\s]","");
//			
//			//Update the current token and move the pointer to the next token
//			tempToken = new Token();
//			tempToken.setTermText(currentTokenString);
//			copy.tokenList.set(count, tempToken);
//			count++;
//			return true;
//		}
//		else
//			return false;
		Token token = copy.next();
		removeSpecailChars(token);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void removeSpecailChars(Token token){
		String tokenText = token.getTermText();
//		if(tokenText.contains("@")){
//			String [] tokens = tokenText.split("@");
//			token.setTermText(tokens[0]);
//			Token newToken = new Token();
//			newToken.setTermText(tokens[1]);
//			copy.tokenIterator.add(newToken);
//			return;
//		}
		if(tokenText.matches(".*[a-zA-Z]+-[a-zA-Z]+.*")){
			token.setTermText(tokenText.replaceAll("[\\W_&&[^\\.]]", ""));
		}else{
			token.setTermText(tokenText.replaceAll("[\\W_&&[^\\-]&&[^\\.]]", ""));
		}
		//token.setTermText(token.getTermText().replaceAll("\\W", ""));
	}
}

