/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		BufferedReader br = null;
		Document ret = new Document();
		try 
		{
			String current;
			File f = new File(filename);
			br = new BufferedReader(new FileReader(f));
			
			//Set FILEID with filename
			ret.setField(FieldNames.FILEID, filename);
			String DirName = f.getParent();
			
			//Set category with directory name
			ret.setField(FieldNames.CATEGORY, DirName);
			
			while ((current = br.readLine()) != null) 
			{
				System.out.println(current);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (br != null)
					br.close();
			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		return ret;
	}

}
