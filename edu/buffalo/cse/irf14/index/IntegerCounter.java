package edu.buffalo.cse.irf14.index;

import java.util.LinkedList;

public class IntegerCounter {
	private int count = 1;
	private LinkedList<Integer> positionalIndex = new LinkedList<Integer>();
	public int incrementCounter(int position){
		++count;
		return count;
	}
	public int getCount(){
		return count;
	}
	public LinkedList<Integer> getPositionalIndex() {
		return positionalIndex;
	}
	@Override
	public String toString() {
		return "IntegerCounter [count=" + count + ", positionalIndex="
				+ positionalIndex + "]";
	}
	public void setPositionalIndex(LinkedList<Integer> positionalIndex) {
		this.positionalIndex = positionalIndex;
	}
	
	public void addPosition(int position){
		positionalIndex.add(position);
	}
}
