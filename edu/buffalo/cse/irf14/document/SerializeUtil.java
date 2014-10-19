package edu.buffalo.cse.irf14.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.buffalo.cse.irf14.index.TermMap;

public class SerializeUtil {
	public void serializeDocMap(String indexDir, DocumentMap docMap){
		String fileName = indexDir + "docMap";
		writeDiskDocMap(fileName, docMap);
	}
	public DocumentMap deSerializeDocMap(String indexDir){
		String fileName = indexDir + "docMap";
		
		return readDiskDocMap(fileName);
	}
	public void writeDiskDocMap(String fileName, DocumentMap docMap)
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
			oWriter.writeObject(docMap);
			oWriter.close();
			fWriter.close();
 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public DocumentMap readDiskDocMap(String fileName)
	{
		DocumentMap docMap = null;
	    try
	    {
	    	FileInputStream fInput = new FileInputStream(fileName);
	        ObjectInputStream oInput = new ObjectInputStream(fInput);
	        docMap = (DocumentMap) oInput.readObject();
	        oInput.close();
	        fInput.close();
	        return docMap;
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
	public void serializeTermMap(String indexDir, TermMap termMap){
		String fileName = indexDir + "docMap";
		writeDiskTermMap(fileName, termMap);
	}
	public TermMap deSerializeTermMap(String indexDir){
		String fileName = indexDir + "docMap";
		
		return readDiskTermMap(fileName);
	}
	public void writeDiskTermMap(String fileName, TermMap termMap)
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
			oWriter.writeObject(termMap);
			oWriter.close();
			fWriter.close();
 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public TermMap readDiskTermMap(String fileName)
	{
		TermMap termMap = null;
	    try
	    {
	    	FileInputStream fInput = new FileInputStream(fileName);
	        ObjectInputStream oInput = new ObjectInputStream(fInput);
	        termMap = (TermMap) oInput.readObject();
	        oInput.close();
	        fInput.close();
	        return termMap;
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

}

