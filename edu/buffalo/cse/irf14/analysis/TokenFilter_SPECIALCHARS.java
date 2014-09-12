package edu.buffalo.cse.irf14.analysis;

public class TokenFilter_SPECIALCHARS extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	public TokenFilter_SPECIALCHARS(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
		if(count < length)
		{
			currentTokenString = copy.tokenList.get(count).toString();
			currentTokenString.replaceAll("[^\\w\\s]","");
			
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

