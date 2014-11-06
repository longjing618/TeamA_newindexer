package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.SearchRunner;
import edu.buffalo.cse.irf14.index.IndexContainer;

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
		String termText = token.getTermText().toLowerCase();
		token.setTermText(termText);
//		if(!copy.hasNext()){
//			addToUnstemmedIndex();
//		}
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
//	private void addToUnstemmedIndex(){
//		if(SearchRunner.isQueryMode){
//			return;
//		}
//		copy.reset();
//		while(copy.hasNext()){
//			Token token = copy.next();
//			String termText = token.getTermText();
//			if(termText.matches("[a-zA-Z]+")){
//				IndexContainer.unstemmedTermMap.add(termText);
//			}
//		}
//	}
}
