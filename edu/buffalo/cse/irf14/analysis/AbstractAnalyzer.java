package edu.buffalo.cse.irf14.analysis;

import java.util.List;

public abstract class AbstractAnalyzer implements Analyzer {
	protected TokenStream stream;
	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		List<TokenFilter> filterList = getFilterList();
		for(TokenFilter tokenFilter: filterList){
			if(tokenFilter.isStreamEmpty())
				break;
			tokenFilter.reset();
			while(tokenFilter.increment()){
				
			}
		}
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return stream;
	}
	
	protected abstract List<TokenFilter> getFilterList();

}
