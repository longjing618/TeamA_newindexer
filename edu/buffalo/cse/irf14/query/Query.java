package edu.buffalo.cse.irf14.query;

<<<<<<< HEAD
=======
import java.util.HashSet;
>>>>>>> a8964f1269cfb04183863cbb8db47767c001b432

/**
 * Class that represents a parsed query
 * @author nikhillo
 *
 */
public class Query {
	private String querypostfix;
	public Query(String query)
	{
		querypostfix = new String(query);
	}
	
	/**
	 * Method to convert given parsed query into string
	 */
	public String toString() {
		//TODO: YOU MUST IMPLEMENT THIS
		return null;
	}
	
	//Bikram doing this
	public HashSet<Integer> getQueryDocIdSet(){
		
		return null;
	}
}
