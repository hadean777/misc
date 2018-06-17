package log2count;

import java.util.Date;

public class Main {
	
	//Euler number:
	public static final double e = 2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427427466391932003059921817413596629043572900334295260;
	
	//log(2)
	public static final double LOG2 = 0.693147180559945;
	
	//Fractional part of log(2) from MATLAB bin(fi) //fixed
	public final static String LOG2_MODEL_STR = "1011000101110010"; 
	public final static byte[] LOG2_MODEL_BIN = {1,0,1,1,0,0,0,1,0,1,1,1,0,0,1,0};
	
	//Log2 from online calc
	public final static String L2 = "0.1011000101110010";
	
	
	public final static int PRECISION = 2;
	
	public static void main(String[] args) {
		
		
		byte[] initialSequence = getInitialSequence();
		
		int sequenceBitLength = initialSequence.length * 8;
		
//		for (long n = 0; n <= sequenceBitLength; n++) {
//			byte nDigit = Log2count.getLog2Ndigit(n);
//			System.out.print(nDigit);
//		}
		System.out.println();
		Date startTime = new Date();
		long longStart = startTime.getTime() / 1000;
		System.out.println("Starting...");
		System.out.println("Start Time: " + startTime + "Long(sec): " + longStart);
		System.out.println("sequenceBitLength: " + sequenceBitLength);
		System.out.println();
		boolean[] initBoolSeq = convertToBoolArray(initialSequence);
		
		boolean searching = true;
		int localCount = 0;
		long globalCount = 0;
		int maxResult = 0;
		while (searching) {
			boolean nDigit = Log2count.getBoolLog2Ndigit(globalCount);
			//if bits are equals
			if (!(nDigit^initBoolSeq[localCount])) {
				localCount++;
			} else {
				if (localCount > maxResult) {
					maxResult = localCount;
					System.out.println("Global: " + globalCount + " Max: " + maxResult);
					localCount = 0;
				}
			}
			if (localCount >= sequenceBitLength) {
				searching = false;
			}
			globalCount++;
		}
		
		long offset = globalCount - sequenceBitLength;
		System.out.println("Global: " + globalCount + " Offset: " + offset);
		System.out.println();
		Date finishTime = new Date();
		long longFinish = finishTime.getTime() / 1000;
		System.out.println("Completed");
		System.out.println("Finish Time: " + finishTime + "Long(sec): " + longFinish);
		long totalTime = longFinish - longStart;
		System.out.println("Total time (sec): " + totalTime);
		
	}
	
	//TODO: should be implemented load from file
	private static byte[] getInitialSequence() {
		
		//For sequence 6guRczu8eUwBKtWq offset=9097

		String sampleSequence = "6guRczu8eUwBKtWq!";
		
		System.out.println("Sequence: " + sampleSequence);
		byte[] result = sampleSequence.getBytes();
		return result;
	}
	
	private static boolean[] convertToBoolArray(byte[] seq) {
		int sequenceBitLength = seq.length * 8;
		boolean[] result = new boolean[sequenceBitLength];
		int bitCounter = 0;
		for (int byteNum = 0; byteNum < seq.length; byteNum++) {
			byte currentByte = seq[byteNum];
			String strByte = String.format("%8s", Integer.toBinaryString(currentByte & 0xFF)).replace(' ', '0');
			for (int bitNum = 0; bitNum < 8; bitNum++) {
				byte currentBit = Byte.parseByte(String.valueOf(strByte.charAt(bitNum)));
				if (currentBit == 1) {
					result[bitCounter] = true;
				} else {
					result[bitCounter] = false;
				}
				bitCounter++;
			}
		}
		
		return result;
	}
	

}
