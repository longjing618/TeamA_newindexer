package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	public void serializeAll(String indexDir)
	{
		String fileName = indexDir+"K-gram_index";
		writeDisk(fileName,map);
	}
	
	public void deSerializeAll(String indexDir)
	{
		String fileName = indexDir+"K-gram_index";
		map = readDisk(fileName);
	}
	
	public HashMap<String, LinkedList<Integer>> readDisk(String fileName)
	{
		HashMap<String, LinkedList<Integer>> indexMap = null;
	    try
	    {
	    	FileInputStream fInput = new FileInputStream(fileName);
	        ObjectInputStream oInput = new ObjectInputStream(fInput);
	        indexMap = (HashMap<String, LinkedList<Integer>>) oInput.readObject();
	        oInput.close();
	        fInput.close();
	        return indexMap;
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	        return null;
	    }
	    catch(ClassNotFoundException c)
	    {
	    	System.out.println("Class not found");
	    	c.printStackTrace();
	    	return null;
	    }
	}
	
	public void writeDisk(String fileName, HashMap<String, LinkedList<Integer>> indexMap)
	{
		try 
		{
			//Open the index file. Create the index file if does not exist. 
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
	 
            FileOutputStream fWriter = new FileOutputStream(file,false);
            ObjectOutputStream oWriter = new ObjectOutputStream(fWriter);
			oWriter.writeObject(indexMap);
			oWriter.close();
			fWriter.close();
 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
