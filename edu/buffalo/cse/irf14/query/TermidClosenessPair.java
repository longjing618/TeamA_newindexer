package edu.buffalo.cse.irf14.query;

public class TermidClosenessPair implements Comparable<TermidClosenessPair> 
{
	  private int termId;
	  private int closeness;

	  public TermidClosenessPair(int t, int c)
	  {
		  this.termId = t;
		  this.closeness = c;
	  }
	  
	  public int getTermId()
	  {
	    return termId;
	  }

	  public void setTermId(int t) 
	  {
	    this.termId = t;
	  }

	  public int getCloseness() 
	  {
	    return closeness;
	  }

	  public void setCloseness(int i) 
	  {
	    this.closeness = i;
	  }

	  public int compareTo(TermidClosenessPair s) 
	  {
//	    int count = ((TermidClosenessPair)s).getCloseness();  
//	    return count - getCloseness();    
		  Integer i1 = this.closeness;
		  Integer i2 = s.closeness;
		  return i1.compareTo(i2);
	  }
}
