package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.irf14.document.FieldNames;

public abstract class AbstractAnalyzer implements Analyzer {

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void analyze(TokenStream stream, FieldNames fieldName) throws TokenizerException{
		List<TokenFilter> filterList = getFilterList(stream);
		for(TokenFilter tokenFilter: filterList){
			while(tokenFilter.increment()){
				
			}
			stream.reset();
		}
	}
	
	protected abstract List<TokenFilter> getFilterList(TokenStream stream);

}
