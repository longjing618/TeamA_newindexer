package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerTitle extends AbstractAnalyzer {

	private TokenStream stream;
	
	public AnalyzerTitle(TokenStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	protected List<TokenFilter> getFilterList() {
		// TODO Auto-generated method stub
		ArrayList<TokenFilter> filterList = new ArrayList<TokenFilter>();
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.SYMBOL, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.ACCENT, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.SPECIALCHARS, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.NUMERIC, stream));
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.STOPWORD, stream));
		return filterList;
	}

	
}
