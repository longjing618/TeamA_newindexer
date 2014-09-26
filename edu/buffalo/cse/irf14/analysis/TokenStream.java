/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

/**
 * @author nikhillo
 * Class that represents a stream of Tokens. All {@link Analyzer} and
 * {@link TokenFilter} instances operate on this to implement their
 * behavior
 */
public class TokenStream implements Iterator<Token>{
	
	/**
	 * Method that checks if there is any Token left in the stream
	 * with regards to the current pointer.
	 * DOES NOT ADVANCE THE POINTER
	 * @return true if at least one Token exists, false otherwise
	 */
	
	//ArrayList<Token> tokenList = new ArrayList<Token>();
	//Using linked list as we need to delete elements also and traversal is faster
	//Implementation with Arraylist is easier, but there will be a lot
	//of stopwords. Will use the iterator provided by the LinkedList itself
	LinkedList<Token> tokenList = new LinkedList<Token>();
	ListIterator<Token> tokenIterator = tokenList.listIterator();
	//Token previous = null;
	Token current = null;
	
	@Override
	public boolean hasNext() {
		// TODO YOU MUST IMPLEMENT THIS
		return tokenIterator.hasNext();
	}

	/**
	 * Method to return the next Token in the stream. If a previous
	 * hasNext() call returned true, this method must return a non-null
	 * Token.
	 * If for any reason, it is called at the end of the stream, when all
	 * tokens have already been iterated, return null
	 */
	@Override
	public Token next() {
		// TODO YOU MUST IMPLEMENT THIS
		//if(tokenIterator.nextIndex() == tokenList.size())
		//	return null;
		if(tokenIterator.hasNext()){
			current = tokenIterator.next();
		}else{
		current = null;
		}
		return current;
		
	}
	
	/**
	 * Method to remove the current Token from the stream.
	 * Note that "current" token refers to the Token just returned
	 * by the next method. 
	 * Must thus be NO-OP when at the beginning of the stream or at the end
	 */
	@Override
	public void remove() {
		// TODO YOU MUST IMPLEMENT THIS
		if((!isValidRemove())&&current==null)
			return;
		tokenIterator.remove();
		current = null;
	}
	
	/**
	 * Method to reset the stream to bring the iterator back to the beginning
	 * of the stream. Unless the stream has no tokens, hasNext() after calling
	 * reset() must always return true.
	 */
	public void reset() {
		//TODO : YOU MUST IMPLEMENT THIS
		this.tokenIterator = this.tokenList.listIterator();
	}
	
	/**
	 * Method to append the given TokenStream to the end of the current stream
	 * The append must always occur at the end irrespective of where the iterator
	 * currently stands. After appending, the iterator position must be unchanged
	 * Of course this means if the iterator was at the end of the stream and a 
	 * new stream was appended, the iterator hasn't moved but that is no longer
	 * the end of the stream.
	 * @param stream : The stream to be appended
	 */
	public void append(TokenStream stream) {
		//TODO : YOU MUST IMPLEMENT THIS
		//Need to test the case when the iterator is at the end.
		if(stream == null)
			return;
		
		
		if(!stream.isEmpty()){
			//Commenting to remove positional tracking
//			int index = tokenList.size();
//			for(Token token : stream.tokenList){
//				token.position = ++index;
//			}
			int nextIndex = tokenIterator.nextIndex();
			tokenList.addAll(stream.tokenList);
			tokenIterator = tokenList.listIterator();
			if(nextIndex == 0)
				return;
			while(true){
				//Token token = tokenIterator.next();
				tokenIterator.next();
				if(tokenIterator.nextIndex() == nextIndex)
					break;
			}
		}
	}
	
	//Just a utility method.
	public boolean isEmpty(){
		if(tokenList != null){
			return tokenList.isEmpty();
		}else 
			return false;
	}

	//Constructor
	public TokenStream() {
		super();
	}
	
	/**
	 * Populates the tokenList.
	 * @param: str : This is the string to be tokenized.
	 * @param: delim : The delimiter to be used.
	 */
	public void init(String str, String delim){
		//Using StringTokenizer in place of String.split() 
		//as String.split() considers the argument to be a regex
		//If the delimiter is a valid regex my mistake, like ".",
		//then we will have issues.
		//Commenting to remove positional tracking
		//int index = 0;
		StringTokenizer st = new StringTokenizer(str, delim);
		while(st.hasMoreTokens()){
			Token token = new Token();
			token.setTermText(st.nextToken());
			//Commenting to remove positional tracking
			//token.position = ++index;
			tokenList.add(token);
		}
		tokenIterator = tokenList.listIterator();
	}
	
	public Token getPrevious(){
		int previous = tokenIterator.previousIndex()-1;
		if(previous < 0)
			return null;
		return tokenList.get(previous);
	}
	
	/**
	 * Method to get the current Token from the stream without iteration.
	 * The only difference between this method and {@link TokenStream#next()} is that
	 * the latter moves the stream forward, this one does not.
	 * Calling this method multiple times would not alter the return value of {@link TokenStream#hasNext()}
	 * @return The current {@link Token} if one exists, null if end of stream
	 * has been reached or the current Token was removed
	 */
	public Token getCurrent() {
		//TODO: YOU MUST IMPLEMENT THIS
//		if(!tokenIterator.hasNext())
//			return null;
//		if(tokenIterator.hasPrevious()){
//			tokenIterator.previous();
//			return tokenIterator.next();
//		}
		return current;
	}
	public boolean isValidRemove(){
		if(!tokenIterator.hasNext())
			return false;
		if(tokenIterator.hasPrevious()){
			return true;
		}
		return false;
	}
	
	public Token viewNext(){
		if(tokenIterator.hasNext())
			return tokenList.get(tokenIterator.nextIndex());
		return null;
	}
	
	public int getLength()
	{
		return tokenList.size();
	}
	
	public Token viewNextNext()
	{
		if(tokenIterator.nextIndex() < getLength()-2)
			return tokenList.get(tokenIterator.nextIndex()+1);
		return null;
	}
	
	public void removeNext()
	{
		if(tokenIterator.hasNext())
		{
			tokenIterator.next();
			tokenIterator.remove();
		}
	}
	
	public void removePrevious() {
		tokenIterator.previous();
		tokenIterator.previous();
		tokenIterator.remove();
	}
	
}
