package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerTitle extends AbstractAnalyzer {

	@Override
	protected List<TokenFilter> getFilterList(TokenStream stream) {
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
