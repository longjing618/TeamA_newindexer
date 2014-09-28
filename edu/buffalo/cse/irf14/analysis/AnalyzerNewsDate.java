package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerNewsDate extends AbstractAnalyzer {

	private TokenStream stream;
	
	public AnalyzerNewsDate(TokenStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	protected List<TokenFilter> getFilterList() {
		// TODO Auto-generated method stub
		ArrayList<TokenFilter> filterList = new ArrayList<TokenFilter>();
		filterList.add(TokenFilterFactory.getInstance().getFilterByType(TokenFilterType.DATE, stream));
		
		return filterList;
	}

	
}
