package edu.buffalo.cse.irf14.query;

public class TermidClosenessPair implements Comparable 
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

	  public int compareTo(Object s) throws ClassCastException 
	  {
	    if (!(s instanceof TermidClosenessPair))
	      throw new ClassCastException("A TermidClosenessPair object expected.");
	    int count = ((TermidClosenessPair)s).getCloseness();  
	    return count - getCloseness();    
	  }
}
