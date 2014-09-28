package edu.buffalo.cse.irf14.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TermMap {
	private int arrayIndex = 0;
	private int termId = 0;
	//private HashMap<String, Integer> termMap;
	//private HashMap<Integer, String> reverseMap;
	private ArrayList<String> termMap;
	public TermMap() {
		termMap = new ArrayList<String>();	
	}

	public int getStart(String term)
	{
		for(int i=0;i<termMap.size();i++)
		{
			int ret = termMap.get(i).indexOf(term+',');
			if(ret > -1)
				return ret;
		}
		return -1;
	}
	
	public int getArrayIndex(String term)
	{
		for(int i=0;i<termMap.size();i++)
		{
			int ret = termMap.get(i).indexOf(term+',');
			if(ret > -1)
				return i;
		}
		return -1;
	}
	
	public int add(String term) {
		termId++;
		if(termMap.size() == 0)
		{
			termMap.add("");
		}
		if(Integer.MAX_VALUE - termMap.get(arrayIndex).length() < term.length())
		{
			termMap.add(term+',');
			arrayIndex++;
		}
		else
		{
			String temp = termMap.get(arrayIndex);
			temp += term +',';
			termMap.set(arrayIndex, temp);
		}
		return termId;
	}
	
	public int getTermIdFromTermMap(String term)
	{
		int ret = -1;
		for(int i=0;i<termMap.size();i++)
		{
			String temp = termMap.get(i);
			int index = temp.indexOf(term+',');
			if(index > -1)
			{
				for(int j=0; j<index; j++) 
				{
				    if(temp.charAt(j) == ',')
				        ret++;
				}
				return ret + 1;
			}
			for(int j=0;j<temp.length();j++)
				if(temp.charAt(j) == ',')
					ret++;
		}
		return ret;
	}
	
	public int getTermId(String term){
		int ret = getTermIdFromTermMap(term);
		if(ret == -1)
			return add(term);
		return ret;
	}
	
	public int getTermIdWithoutAdding(String term){
		return getTermIdFromTermMap(term);
	}

	public int getSize(){
		return termId;
	}
	
	public String getTermText(int index, int start, int length)
	{
		if(index >= termMap.size())
			return null;
		
		String temp = termMap.get(index);
		if(start < 0 || start > temp.length())
			return null;
		else
		{
			String ret = temp.substring(start, start+length);
			return ret;
		}
	}
	
	public List<String> getSortedTerms(){
		List<String> terms = new ArrayList<String>();
		for(int i=0;i<termMap.size();i++)
			terms.addAll(Arrays.asList(termMap.get(i).split(",")));
		Collections.sort(terms);
		return terms;
	}
}
