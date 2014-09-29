package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerAuthor extends AbstractAnalyzer {

	private TokenStream stream;
	
	public AnalyzerAuthor(TokenStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	protected List<TokenFilter> getFilterList() {
		// TODO Auto-generated method stub
		stream.reset();
//		while(stream.hasNext()){
//			Token token = stream.next();
//			token.setTermText(token.getTermText().toLowerCase());
//		}
//		stream.reset();
		ArrayList<TokenFilter> filterList = new ArrayList<TokenFilter>();
		//filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.SYMBOL, stream));
		//filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.ACCENT, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.STOPWORD, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.CAPITALIZATION, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.SPECIALCHARS, stream));		
		
		return filterList;
	}

	
}
