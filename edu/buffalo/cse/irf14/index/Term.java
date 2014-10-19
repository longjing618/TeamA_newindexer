package edu.buffalo.cse.irf14.index;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Term implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8930369869009602270L;
	private int termId;
	private int numberOfDocuments;
	private int totalCount;
	private byte [] docOffsets;
	private byte [] frequency;
	private byte [] positions;
	private int arraySize;
	private int frequencySize;
	private int arrayCount;
	private int frequencyCount;
	private int maxDocIdInPoting;
	private int positionArraySize;
	private int positionArrayCount;
	
	public Term() {
		super();
		numberOfDocuments = 0;
		totalCount = 0;
		docOffsets = new byte[10];
		frequency = new byte[10];
		positions = new byte[100];
		arraySize = 10;
		frequencySize = 10;
		arrayCount = 0;
		frequencyCount = 0;
		positionArraySize = 10;
		maxDocIdInPoting = 0;
		
	}
	//private List<Posting> postingList = new LinkedList<Posting>();
	//Not maintaining pointer to posting list here
	//There will be one posting list for all words starting with a single character.
	//For example, apple, Amsterdam will go to the same posting list. Need to discuss this.
	//We may use ArrayList<ArrayyList<Posting>> to do this. Or will a single ArrayList do? Also should 
	//we use a linked list instead of ArrayList?
	public int getTermId() {
		return termId;
	}
	public void setTermId(int termId) {
		this.termId = termId;
	}
	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}
	public void setNumberOfDocuments(int numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getGlobalFrequency(){
		return totalCount/numberOfDocuments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + termId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (termId != other.termId)
			return false;
		return true;
	}
	public List<Posting> getPostingList() {
		List<Posting> postingList = new LinkedList<Posting>();
		List<Integer> docIdOffsetList = decodeVB(docOffsets);
		List<Integer> frequencyList = decodeVB(frequency);
		int currentDocId = 0;
		int length = docIdOffsetList.size();
		List<LinkedList<Integer>> positionalIndex = getPositionalIndex();
		for(int i = 0; i < length; i++){
			Posting posting = new Posting();
			int docId = currentDocId + docIdOffsetList.get(i);
			int termFreq = frequencyList.get(i);
			posting.setDocId(docId);
			posting.setTermCountInDoc(termFreq);
			postingList.add(posting);
			currentDocId = docId;
			posting.setPositionLsit(positionalIndex.get(i));
		}
		
		return postingList;
	}
	
	public void addPosting(Posting posting){
		numberOfDocuments++;
		totalCount += posting.getTermCountInDoc();
		int currentDocOffset = posting.getDocId() - maxDocIdInPoting;
		maxDocIdInPoting = posting.getDocId();
		byte [] currentDocOffsetVB = convertToVB(currentDocOffset);
		if(arrayCount + currentDocOffsetVB.length >= arraySize){
			expandPostingArray();
		}
		for(int i = 0; i < currentDocOffsetVB.length; i++){
			docOffsets[arrayCount] = currentDocOffsetVB[i];
			arrayCount++;
		}
		
		int currentTermFreq = posting.getTermCountInDoc();
		byte [] currentTermFreqVB = convertToVB(currentTermFreq);
		if(frequencyCount + currentTermFreqVB.length >= frequencySize){
			expandFrequenyArray();
		}
		for(int i = 0; i < currentTermFreqVB.length; i++){
			frequency[frequencyCount] = currentTermFreqVB[i];
			frequencyCount++;
		}
		addPositions(posting.getPositionLsit());
	}
	
	
	private void expandPostingArray(){
		byte [] newDocOffsets = new byte[arraySize * 2];
		arraySize *= 2;
		for(int index = 0; index < arrayCount; index++){
			newDocOffsets[index] = docOffsets[index];
		}
		this.docOffsets = newDocOffsets;
	}
	
	private void expandFrequenyArray(){
		byte [] newFrequency = new byte[frequencySize * 2];
		frequencySize *= 2;
		for(int index = 0; index < frequencyCount; index++){
			newFrequency[index] = frequency[index];
		}
		this.frequency = newFrequency;
	}
	
	private void expandPositionsArray(){
		byte [] newPositions = new byte[positionArraySize * 2];
		positionArraySize *= 2;
		for(int index = 0; index < positionArrayCount; index++){
			newPositions[index] = positions[index];
		}
		this.positions = newPositions;
	}
	
	private byte[] convertToVB(int intForConversion){
		byte firstBit1 = (byte)(Integer.parseInt("10000000",2));
		byte firstBit0 = Byte.parseByte("01111111",2);
		//byte lastBit1 = Byte.parseByte("00000001",2);
		byte [] byteArray = BigInteger.valueOf(intForConversion).toByteArray();
		
		int [] carryBitArray = new int[1]; 
		carryBitArray[0] = byteArray[byteArray.length - 1] > 0 ? 0 : 1; 
		
		byteArray[byteArray.length - 1] = (byte) (byteArray[byteArray.length - 1] | firstBit1);
		int j = 1;
		for(int n = byteArray.length - 2; n >= 0; n--,j++){
			byte currentByte = byteArray[n];
			int [] tempCarryBitArray = new int[j + 1];
			tempCarryBitArray[0] = (currentByte > 0 ? 0 : 1);
			for(int k = 0; k < j; k++){
				currentByte = (byte) (currentByte << 1);
				tempCarryBitArray[k+1] =  (currentByte > 0 ? 0 : 1);
			}
			
			
			currentByte = (byte) (currentByte & firstBit0);
//			if(carryBit == 1){
//				currentByte = (byte) (currentByte | lastBit1);
//			}
			String carry = "";
			for(int b : carryBitArray){
				carry = carry + b;
			}
			byte carryByte = Byte.parseByte(carry, 2);
			currentByte = (byte)(currentByte | carryByte);
			byteArray[n] = currentByte;
			carryBitArray = tempCarryBitArray;
		}
		
		return byteArray;
	}
	
	private LinkedList<Integer> decodeVB(byte [] byteArray){
		LinkedList<Integer> list = new LinkedList<Integer>();
		StringBuffer sb = new StringBuffer("");
		boolean isStopByte = false;
		for(byte b : byteArray){
			if(b < 0){
				isStopByte = true;
			}else{
				b = (byte)( b|-128);  //ugly hack to make Integer.toBinaryString return minimum 8 bits.
			}
			String str = Integer.toBinaryString(b);
			
			str = str.substring(str.length() - 7, str.length());
			
			
			sb.append(str);
			if(isStopByte){
				isStopByte = false;
				//System.out.println(sb.toString());
				int i = Integer.parseInt(sb.toString(), 2);
				list.add(i);
				sb = new StringBuffer("");
			}
		}
		
		return list;
	}
	
	public void cleanup(){
		byte [] newDocOffsets = new byte[arrayCount];
		byte [] newFrequency = new byte[frequencyCount];
		for(int index = 0; index < arrayCount; index++){
			newDocOffsets[index] = docOffsets[index];
		}
		for(int index = 0; index < frequencyCount; index++){
			newFrequency[index] = frequency[index];
		}
		arraySize = arrayCount;
		frequencySize = frequencyCount;
	}
	
	private void addPositions(List<Integer> positionsList){
		int currentHigh = 0;
		//byte [] temp = new byte [1000];
		//int positionCount = 0;
		for(int currentPosition : positionsList){
			int currentOffset = currentPosition - currentHigh;
			currentHigh = currentPosition;
			byte [] tempVB = convertToVB(currentOffset);
			if(positionArrayCount + tempVB.length + 1 >= positionArraySize){
				expandPositionsArray();
			}
			for(int i = 0; i < tempVB.length; i++){
				positions[positionArrayCount] = tempVB[i];
				positionArrayCount++;
			}
		}
		positions[positionArrayCount] = (byte)0;
		positionArrayCount++;
	}
	
	private List<LinkedList<Integer>> getPositionalIndex(){
		List<LinkedList<Integer>> returnList = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> positionList = new LinkedList<Integer>();
		int currentPosition = 0;
		for(int index = 0; index <= positionArrayCount; index++){
			int currentOffset = positions[index];
			if(currentOffset == 0){
				returnList.add(positionList);
				positionList = new LinkedList<Integer>();
				currentPosition = 0;
			}
			currentPosition += currentOffset;
			positionList.add(currentPosition);
		}
		return returnList;
	}
}
