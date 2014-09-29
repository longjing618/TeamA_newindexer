package edu.buffalo.cse.irf14.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
}

