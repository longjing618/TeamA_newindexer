package edu.buffalo.cse.irf14.analysis;

import java.util.HashSet;

public class TokenFilter_STOPWORD extends TokenFilter{
	int length;
	int count = 0;
	String currentTokenString;
	TokenStream copy;
	Token tempToken;
	HashSet<String> stopWordList = new HashSet<String>();
	public TokenFilter_STOPWORD(TokenStream stream) {
		super(stream);
		copy = stream;
		length = copy.tokenList.size();
	}

	public boolean increment() throws TokenizerException
	{
		if(stopWordList.isEmpty())
			init();
		while(copy.hasNext()){
			String token = copy.next().getTermText();
			if(stopWordList.contains(token)){
				copy.remove();
			}
		}
		
		return true;
	}
	public TokenStream getStream()
	{
		return copy;
	}
	
	private void init(){
		stopWordList.add("a");
		stopWordList.add("about");
		stopWordList.add("above");
		stopWordList.add("after");
		stopWordList.add("again");
		stopWordList.add("against");
		stopWordList.add("all");
		stopWordList.add("am");
		stopWordList.add("an");
		stopWordList.add("and");
		stopWordList.add("any");
		stopWordList.add("are");
		stopWordList.add("aren't");
		stopWordList.add("as");
		stopWordList.add("at");
		stopWordList.add("be");
		stopWordList.add("because");
		stopWordList.add("been");
		stopWordList.add("before");
		stopWordList.add("being");
		stopWordList.add("below");
		stopWordList.add("between");
		stopWordList.add("both");
		stopWordList.add("but");
		stopWordList.add("by");
		stopWordList.add("can't");
		stopWordList.add("cannot");
		stopWordList.add("could");
		stopWordList.add("couldn't");
		stopWordList.add("did");
		stopWordList.add("didn't");
		stopWordList.add("do");
		stopWordList.add("does");
		stopWordList.add("doesn't");
		stopWordList.add("doing");
		stopWordList.add("don't");
		stopWordList.add("down");
		stopWordList.add("during");
		stopWordList.add("each");
		stopWordList.add("few");
		stopWordList.add("for");
		stopWordList.add("from");
		stopWordList.add("further");
		stopWordList.add("had");
		stopWordList.add("hadn't");
		stopWordList.add("has");
		stopWordList.add("hasn't");
		stopWordList.add("have");
		stopWordList.add("haven't");
		stopWordList.add("having");
		stopWordList.add("he");
		stopWordList.add("he'd");
		stopWordList.add("he'll");
		stopWordList.add("he's");
		stopWordList.add("her");
		stopWordList.add("here");
		stopWordList.add("here's");
		stopWordList.add("hers");
		stopWordList.add("herself");
		stopWordList.add("him");
		stopWordList.add("himself");
		stopWordList.add("his");
		stopWordList.add("how");
		stopWordList.add("how's");
		stopWordList.add("i");
		stopWordList.add("i'd");
		stopWordList.add("i'll");
		stopWordList.add("i'm");
		stopWordList.add("i've");
		stopWordList.add("if");
		stopWordList.add("in");
		stopWordList.add("into");
		stopWordList.add("is");
		stopWordList.add("isn't");
		stopWordList.add("it");
		stopWordList.add("it's");
		stopWordList.add("its");
		stopWordList.add("itself");
		stopWordList.add("let's");
		stopWordList.add("me");
		stopWordList.add("more");
		stopWordList.add("most");
		stopWordList.add("mustn't");
		stopWordList.add("my");
		stopWordList.add("myself");
		stopWordList.add("no");
		stopWordList.add("nor");
		stopWordList.add("not");
		stopWordList.add("of");
		stopWordList.add("off");
		stopWordList.add("on");
		stopWordList.add("once");
		stopWordList.add("only");
		stopWordList.add("or");
		stopWordList.add("other");
		stopWordList.add("ought");
		stopWordList.add("our");
		stopWordList.add("ours");
		stopWordList.add("ourselves");
		stopWordList.add("out");
		stopWordList.add("over");
		stopWordList.add("own");
		stopWordList.add("same");
		stopWordList.add("shan't");
		stopWordList.add("she");
		stopWordList.add("she'd");
		stopWordList.add("she'll");
		stopWordList.add("she's");
		stopWordList.add("should");
		stopWordList.add("shouldn't");
		stopWordList.add("so");
		stopWordList.add("some");
		stopWordList.add("such");
		stopWordList.add("than");
		stopWordList.add("that");
		stopWordList.add("that's");
		stopWordList.add("the");
		stopWordList.add("their");
		stopWordList.add("theirs");
		stopWordList.add("them");
		stopWordList.add("themselves");
		stopWordList.add("then");
		stopWordList.add("there");
		stopWordList.add("there's");
		stopWordList.add("these");
		stopWordList.add("they");
		stopWordList.add("they'd");
		stopWordList.add("they'll");
		stopWordList.add("they're");
		stopWordList.add("they've");
		stopWordList.add("this");
		stopWordList.add("those");
		stopWordList.add("through");
		stopWordList.add("to");
		stopWordList.add("too");
		stopWordList.add("under");
		stopWordList.add("until");
		stopWordList.add("up");
		stopWordList.add("very");
		stopWordList.add("was");
		stopWordList.add("wasn't");
		stopWordList.add("we");
		stopWordList.add("we'd");
		stopWordList.add("we'll");
		stopWordList.add("we're");
		stopWordList.add("we've");
		stopWordList.add("were");
		stopWordList.add("weren't");
		stopWordList.add("what");
		stopWordList.add("what's");
		stopWordList.add("when");
		stopWordList.add("when's");
		stopWordList.add("where");
		stopWordList.add("where's");
		stopWordList.add("which");
		stopWordList.add("while");
		stopWordList.add("who");
		stopWordList.add("who's");
		stopWordList.add("whom");
		stopWordList.add("why");
		stopWordList.add("why's");
		stopWordList.add("with");
		stopWordList.add("won't");
		stopWordList.add("would");
		stopWordList.add("wouldn't");
		stopWordList.add("you");
		stopWordList.add("you'd");
		stopWordList.add("you'll");
		stopWordList.add("you're");
		stopWordList.add("you've");
		stopWordList.add("your");
		stopWordList.add("yours");
		stopWordList.add("yourself");
		stopWordList.add("yourselves");
		
	}
}
