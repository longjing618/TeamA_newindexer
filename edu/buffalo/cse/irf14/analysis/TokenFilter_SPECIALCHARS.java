package edu.buffalo.cse.irf14.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFilter_SPECIALCHARS extends TokenFilter{
	int length;
	int count = 0;
	//String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	Pattern normalWordPattern = Pattern.compile("[a-zA-Z0-9]+");
	public TokenFilter_SPECIALCHARS(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
//		if(count < length)
//		{
//			currentTokenString = copy.tokenList.get(count).toString();
//			currentTokenString.replaceAll("[^\\w\\s]","");
//			
//			//Update the current token and move the pointer to the next token
//			tempToken = new Token();
//			tempToken.setTermText(currentTokenString);
//			copy.tokenList.set(count, tempToken);
//			count++;
//			return true;
//		}
//		else
//			return false;
		Token token = copy.next();
		removeSpecailChars(token);
		return copy.hasNext();
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void removeSpecailChars(Token token){
		String tokenText = token.getTermText();
//		if(tokenText.contains("@")){
//			String [] tokens = tokenText.split("@");
//			token.setTermText(tokens[0]);
//			Token newToken = new Token();
//			newToken.setTermText(tokens[1]);
//			copy.tokenIterator.add(newToken);
//			return;
//		}
//		if(tokenText.matches("[a-zA-Z0-9]*"))
//			return;
//		Matcher matcher = normalWordPattern.matcher(tokenText);
//		if(matcher.matches())
//			return;
		boolean shouldProcess = false;
		for(char ch : token.getTermBuffer()){
			if(!Character.isLetterOrDigit(ch)){
				shouldProcess = true;
				break;
			}
		}
		if(!shouldProcess)
			return;
		if(tokenText.matches("[\\W_]+")){
			copy.remove();
			return;
		}
		if(tokenText.matches(".*[a-zA-Z]+-[a-zA-Z]+.*")){
			token.setTermText(tokenText.replaceAll("[\\W_&&[^\\.]]", ""));
		}else{
			token.setTermText(tokenText.replaceAll("[\\W_&&[^\\-]&&[^\\.]]", ""));
		}
		//token.setTermText(token.getTermText().replaceAll("\\W", ""));
	}
}

