package edu.buffalo.cse.irf14.index;

import java.util.HashMap;
import java.util.LinkedList;

public class kgramindex 
{
	private HashMap<String, LinkedList<Integer>> map = new HashMap<String,LinkedList<Integer>>();
	
	public void kgramindex()
	{
		map = new HashMap<String,LinkedList<Integer>>();
	}
	
	public void addGram(String gram,int termId)
	{
		if(map.containsKey(gram))
		{
			LinkedList<Integer> temp = map.get(gram);
			temp.add(termId);
			map.put(gram, temp);
		}
		else
		{
			LinkedList<Integer> temp = new LinkedList<Integer>();
			temp.add(termId);
			map.put(gram, temp);
		}
	}
	
	public LinkedList<Integer> getPostingsList(String gram)
	{
		if(map.containsKey(gram))
			return map.get(gram);
		return null;
	}
}
