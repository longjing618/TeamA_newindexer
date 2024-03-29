/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	//public static DocumentMap docMap = new DocumentMap();
	public static Document parse(String filename) throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		BufferedReader br = null;
		Document ret = new Document();
		try 
		{
			String current;
			if(filename == null){
				throw new ParserException();
			}
			File f = new File(filename);
			br = new BufferedReader(new FileReader(f));
			
			//Set FILEID with filename
			ret.setField(FieldNames.FILEID, f.getName());
			String DirName = f.getParentFile().getName();
			
			//Set category with directory name
			ret.setField(FieldNames.CATEGORY, DirName);
			
			String pattern = "<AUTHOR>\\s+(By|by|BY)\\s+([^,]+)(,\\s+(.+))?</AUTHOR>";
			Pattern r = Pattern.compile(pattern);
			Matcher m;

			boolean isplace = false;
			
			//not include the rest text of the place line
			//rewrite code first line title, second line author or place
			StringBuilder content = new StringBuilder("");
			int lines = 0;
			while ((current = br.readLine()) != null) 
			{
				current = current.trim();
				if(current.length() > 0)
				{
					lines++;
					if(lines == 1)
					{
						ret.setField(FieldNames.TITLE,current);
						continue;
					}
					if(lines == 2)
					{
						m = r.matcher(current);
						if (m.find())
						{
							ret.setField(FieldNames.AUTHOR,m.group(2));
							if(m.group(4) != null)
								ret.setField(FieldNames.AUTHORORG, m.group(4));
						}
						else
						{
							pattern = "(.*)( \\w{3,}\\s+\\d{1,2}\\s+)(-?.*)";
							r = Pattern.compile(pattern);
							m = r.matcher(current);
							if (m.find())
							{
								String place = m.group(1).trim();
								place = place.endsWith(",") ? place.substring(0, place.length()-1) : place;
								ret.setField(FieldNames.PLACE, place);
								ret.setField(FieldNames.NEWSDATE, m.group(2).trim());
								content.append(m.group(3).trim().replaceFirst("- ", "")).append(" ");
								isplace = true;
							}else{
								content.append(current).append(" ");
							}
						}
						continue;
					}
					if(lines == 3 && isplace == false)
					{
						pattern = "(.*)( \\w{3,}\\s+\\d{1,2}\\s+)(-?.*)";
						r = Pattern.compile(pattern);
						m = r.matcher(current);
						if (m.find())
						{
							String place = m.group(1).trim();
							place = place.endsWith(",") ? place.substring(0, place.length()-1) : place;
							ret.setField(FieldNames.PLACE, place);
							ret.setField(FieldNames.NEWSDATE, m.group(2).trim());
							content.append(m.group(3).trim()).append(" ");
						}else{
							content.append(current).append(" ");
						}
						continue;
					}
					content.append(current.trim()).append(" ");
				}
			}
//			if(content.length() == 0)
//				System.out.println(filename);
			ret.setField(FieldNames.CONTENT, content.toString());

			//ret.setField(FieldNames.DOCID, Integer.toString(docMap.add(filename)));
		} catch(FileNotFoundException e){
			throw new ParserException();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			throw new ParserException();
		} catch(Exception e){
			throw new ParserException();
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
