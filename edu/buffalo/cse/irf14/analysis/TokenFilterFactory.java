/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;



/**
 * Factory class for instantiating a given TokenFilter
 * @author nikhillo
 *
 */
public class TokenFilterFactory {
	/**
	 * Static method to return an instance of the factory class.
	 * Usually factory classes are defined as singletons, i.e. 
	 * only one instance of the class exists at any instance.
	 * This is usually achieved by defining a private static instance
	 * that is initialized by the "private" constructor.
	 * On the method being called, you return the static instance.
	 * This allows you to reuse expensive objects that you may create
	 * during instantiation
	 * @return An instance of the factory
	 */
	private static TokenFilterFactory TFF = new TokenFilterFactory( );
	public static TokenFilterFactory getInstance() {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		return TFF;
	}
	
	/**
	 * Returns a fully constructed {@link TokenFilter} instance
	 * for a given {@link TokenFilterType} type
	 * @param type: The {@link TokenFilterType} for which the {@link TokenFilter}
	 * is requested
	 * @param stream: The TokenStream instance to be wrapped
	 * @return The built {@link TokenFilter} instance
	 */
	public TokenFilter getFilterByType(TokenFilterType type, TokenStream stream){
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		switch(type)
		{
		case SYMBOL:
			return new TokenFilter_SYMBOL(stream);
		case DATE:
			return new TokenFilter_DATE(stream);
		case NUMERIC:
			return new TokenFilter_NUMERIC(stream);
		case CAPITALIZATION:
			return new TokenFilter_CAPITALIZATION_toLowerCase(stream);
		case STOPWORD:
			return new TokenFilter_STOPWORD(stream);
		case STEMMER:
			return new TokenFilter_STEMMER(stream);
		case ACCENT:
			return new TokenFilter_ACCENT(stream);
		case SPECIALCHARS:
			return new TokenFilter_SPECIALCHARS(stream);
		}
		return null;
	}
}
