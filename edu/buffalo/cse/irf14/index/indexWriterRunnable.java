package edu.buffalo.cse.irf14.index;

import java.util.HashMap;
import java.util.List;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

public class indexWriterRunnable implements Runnable 
{
	IndexWriter iw;
	FieldNames fname;
	Indexer indexer;
	Document d;
	int docid;
	public indexWriterRunnable(IndexWriter indexwriter, FieldNames fieldname, Document doc, Indexer toindexer, int documentId)
	{
		iw = indexwriter;
		fname = fieldname;
		d = doc;
		indexer = toindexer;
		docid = documentId;
	}
	
    public void run()
    {
		List<HashMap<String, IntegerCounter>> termMapArray;
		try {
			termMapArray = IndexWriterUtil.processDocumet(d, fname);
			iw.addToIndex(docid, termMapArray, indexer);
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
		}
    }
 }
