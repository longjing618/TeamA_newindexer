package edu.buffalo.cse.irf14.analysis;

import java.util.List;

public abstract class AbstractAnalyzer implements Analyzer {

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		List<TokenFilter> filterList = getFilterList();
		for(TokenFilter tokenFilter: filterList){
			tokenFilter.getStream().reset();
			while(tokenFilter.increment()){
				
			}
			//stream.reset();
		}
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract List<TokenFilter> getFilterList();

}
