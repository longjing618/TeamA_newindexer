package edu.buffalo.cse.irf14.query;

public class SentenceMatchCountPair implements Comparable 
{
	  private String sentence;
	  private int matchcount;

	  public SentenceMatchCountPair(String s, int i)
	  {
		  this.sentence = s;
		  this.matchcount = i;
	  }
	  
	  public String getsentence() 
	  {
	    return sentence;
	  }

	  public void setsentence(String str) 
	  {
	    this.sentence = str;
	  }

	  public int getMatchCount() 
	  {
	    return matchcount;
	  }

	  public void setMatchCount(int i) 
	  {
	    this.matchcount = i;
	  }

	  public int compareTo(Object s) throws ClassCastException 
	  {
	    if (!(s instanceof SentenceMatchCountPair))
	      throw new ClassCastException("A SentenceMatchCountPair object expected.");
	    int count = ((SentenceMatchCountPair)s).getMatchCount();  
	    return count - getMatchCount();    
	  }
}

