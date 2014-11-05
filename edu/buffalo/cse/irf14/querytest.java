package edu.buffalo.cse.irf14;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;

public class querytest {
	public static void main(String[] args) throws FileNotFoundException
	{
		SearchRunner sr = new SearchRunner("./index/","./flatcorpus",'E',new PrintStream("./test"));
		//sr.query("Adobe", ScoringModel.OKAPI);
		sr.query("hello world", ScoringModel.OKAPI);
		//sr.query("hello world", ScoringModel.OKAPI);
	}
}
