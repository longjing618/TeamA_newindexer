package edu.buffalo.cse.irf14.analysis;

import java.util.HashSet;

public class TokenFilter_STOPWORD extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	//TokenStream copy;
	Token tempToken;
	HashSet<String> stopWordList = new HashSet<String>();
	public TokenFilter_STOPWORD(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
		init();
	}

	public boolean increment() throws TokenizerException
	{
		process();
		return copy.hasNext();
	}
	
	private void process(){
		String token = copy.next().getTermText();
		if(stopWordList.contains(token.toLowerCase())){
			copy.remove();
		}
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void init(){
		stopWordList.add("a");
		stopWordList.add("able");
		stopWordList.add("about");
		stopWordList.add("across");
		stopWordList.add("after");
		stopWordList.add("all");
		stopWordList.add("almost");
		stopWordList.add("also");
		stopWordList.add("am");
		stopWordList.add("among");
		stopWordList.add("an");
		stopWordList.add("and");
		stopWordList.add("any");
		stopWordList.add("are");
		stopWordList.add("as");
		stopWordList.add("at");
		stopWordList.add("be");
		stopWordList.add("because");
		stopWordList.add("been");
		stopWordList.add("but");
		stopWordList.add("by");
		stopWordList.add("can");
		stopWordList.add("cannot");
		stopWordList.add("could");
		stopWordList.add("dear");
		stopWordList.add("did");
		stopWordList.add("do");
		stopWordList.add("does");
		stopWordList.add("either");
		stopWordList.add("else");
		stopWordList.add("ever");
		stopWordList.add("every");
		stopWordList.add("for");
		stopWordList.add("from");
		stopWordList.add("get");
		stopWordList.add("got");
		stopWordList.add("had");
		stopWordList.add("has");
		stopWordList.add("have");
		stopWordList.add("he");
		stopWordList.add("her");
		stopWordList.add("hers");
		stopWordList.add("him");
		stopWordList.add("his");
		stopWordList.add("how");
		stopWordList.add("however");
		stopWordList.add("i");
		stopWordList.add("if");
		stopWordList.add("in");
		stopWordList.add("into");
		stopWordList.add("is");
		stopWordList.add("it");
		stopWordList.add("its");
		stopWordList.add("just");
		stopWordList.add("least");
		stopWordList.add("let");
		stopWordList.add("like");
		stopWordList.add("likely");
		stopWordList.add("may");
		stopWordList.add("me");
		stopWordList.add("might");
		stopWordList.add("most");
		stopWordList.add("must");
		stopWordList.add("my");
		stopWordList.add("neither");
		stopWordList.add("no");
		stopWordList.add("nor");
		stopWordList.add("not");
		stopWordList.add("of");
		stopWordList.add("off");
		stopWordList.add("often");
		stopWordList.add("on");
		stopWordList.add("only");
		stopWordList.add("or");
		stopWordList.add("other");
		stopWordList.add("our");
		stopWordList.add("own");
		stopWordList.add("rather");
		stopWordList.add("said");
		stopWordList.add("say");
		stopWordList.add("says");
		stopWordList.add("she");
		stopWordList.add("should");
		stopWordList.add("since");
		stopWordList.add("so");
		stopWordList.add("some");
		stopWordList.add("than");
		stopWordList.add("that");
		stopWordList.add("the");
		stopWordList.add("their");
		stopWordList.add("them");
		stopWordList.add("then");
		stopWordList.add("there");
		stopWordList.add("these");
		stopWordList.add("they");
		stopWordList.add("this");
		stopWordList.add("tis");
		stopWordList.add("to");
		stopWordList.add("too");
		stopWordList.add("twas");
		stopWordList.add("us");
		stopWordList.add("wants");
		stopWordList.add("was");
		stopWordList.add("we");
		stopWordList.add("were");
		stopWordList.add("what");
		stopWordList.add("when");
		stopWordList.add("where");
		stopWordList.add("which");
		stopWordList.add("while");
		stopWordList.add("who");
		stopWordList.add("whom");
		stopWordList.add("why");
		stopWordList.add("will");
		stopWordList.add("with");
		stopWordList.add("would");
		stopWordList.add("yet");
		stopWordList.add("you");
		stopWordList.add("your");
		
	}
}
