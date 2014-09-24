package edu.buffalo.cse.irf14.index;

public class IntegerCounter {
	private int count = 1;
	public int incrementCounter(){
		++count;
		return count;
	}
	public int getCount(){
		return count;
	}
}
