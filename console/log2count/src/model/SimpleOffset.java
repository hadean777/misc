package model;

public class SimpleOffset {
	
	private long offset;
	private int bitLength;
	
	//temporary param
	private String initSeq;
	
	public SimpleOffset(long offset, int bitLength) {
		this.offset = offset;
		this.bitLength = bitLength;
	}
	
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public int getBitLength() {
		return bitLength;
	}
	public void setBitLength(int bitLength) {
		this.bitLength = bitLength;
	}

	
	////////////////
	public String getInitSeq() {
		return initSeq;
	}
	public void setInitSeq(String initSeq) {
		this.initSeq = initSeq;
	}
}
