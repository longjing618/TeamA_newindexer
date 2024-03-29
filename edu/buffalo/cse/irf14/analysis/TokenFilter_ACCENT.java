package edu.buffalo.cse.irf14.analysis;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TokenFilter_ACCENT extends TokenFilter{
	int length;
	//int count = 0;
	//String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	public TokenFilter_ACCENT(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();
		process(token);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void process(Token token){
		String currentTokenString = token.getTermText();
		currentTokenString = Normalizer.normalize(currentTokenString, Normalizer.Form.NFD);
		currentTokenString = pattern.matcher(currentTokenString).replaceAll("");
		
		//Update the current token and move the pointer to the next token
		token.setTermText(currentTokenString);
	}
}
