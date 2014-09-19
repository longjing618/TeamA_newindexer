package edu.buffalo.cse.irf14.analysis;


public class TokenFilter_NUMERIC extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	//private ArrayList<Pattern> patternList;
	public TokenFilter_NUMERIC(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}
	
	public boolean increment() throws TokenizerException
	{
		Token token = copy.next();
		filter(token);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void filter(Token token){
		if(token.getTermText().matches("(\\d*)(\\.\\d+)?")||token.getTermText().matches("(\\d*)(,\\d+)*(\\.\\d+)?")){
			copy.remove();
		}else if(token.getTermText().matches("(\\d*)(\\.\\d+)?[\\D]+")){
			token.setTermText(token.getTermText().replaceFirst("(\\d*)(\\.\\d+)?", ""));
		}else if(token.getTermText().matches("[\\d\\W]+")){
			token.setTermText(token.getTermText().replaceAll("\\d", ""));
		}
	}
}
