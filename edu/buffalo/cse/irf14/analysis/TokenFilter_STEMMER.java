package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.index.IndexContainer;

public class TokenFilter_STEMMER extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	public TokenFilter_STEMMER(TokenStream stream) {
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
		if(token.getTermText().matches("[a-zA-Z]+")){
			IndexContainer.unstemmedTermMap.add(token.getTermText());
			Stemmer stemmer = new Stemmer();
			stemmer.add(token.getTermBuffer(), token.getTermBuffer().length);
			stemmer.stem();
			token.setTermText(stemmer.toString());
		}
		if(token.getTermText().trim().equals("")||token.getTermText().matches("\\W+")){
			copy.remove();
		}
	}
}
