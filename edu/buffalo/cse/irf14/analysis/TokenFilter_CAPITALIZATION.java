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

	public void increment() throws TokenizerException
	{
		
	}
	public TokenStream getStream()
	{
		return copy;
	}
}
