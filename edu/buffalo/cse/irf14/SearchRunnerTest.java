package edu.buffalo.cse.irf14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.index.IndexContainer;

public class SearchRunnerTest {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		PrintStream ps1 = null;
		try{
			PrintStream ps = new PrintStream(new File("F:\\IR_pj2_out")); 
			String indexDir = "F:\\IrIndexOutput\\";
			String corpusDir = "F:\\IR-git_pvt_t\\TeamA_newindexer\\trainingFlat\\";
			File queryFile  = new File("F:\\queries.txt");
			SearchRunner sr = new SearchRunner(indexDir, corpusDir, 'E', ps);
			sr.query(queryFile);
			//sr.query("\"Adobe Resources\"", ScoringModel.TFIDF);
			ps1 = new PrintStream(new File("F:\\IR_pj2_out1"));
			ps1.println(IndexContainer.termTermMap.getSortedTerms());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ps1.close();
		}
	}

}
