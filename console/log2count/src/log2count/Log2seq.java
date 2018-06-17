package log2count;

import java.util.Date;

import model.SimpleOffset;

public class Log2seq {
	
	public static SimpleOffset findSequenceInLog2(byte[] initialSequence) {
		
		int sequenceBitLength = initialSequence.length * 8;
		System.out.println();
		Date startTime = new Date();
		long longStart = startTime.getTime() / 1000;
		System.out.println("Search for sequence started");
		System.out.println("Start Time: " + startTime + "Long(sec): " + longStart);
		System.out.println("sequenceBitLength: " + sequenceBitLength);
		System.out.println();
		boolean[] initBoolSeq = convertToBoolArray(initialSequence);
		String initSeq = "";
		for (int i = 0; i < initBoolSeq.length; i++) {
			if (initBoolSeq[i]) {
				initSeq += "1";
			} else {
				initSeq += "0";
			}
		}
		
		boolean searching = true;
		int localCount = 0;
		long globalCount = 0;
		int maxResult = 0;
		while (searching) {
			boolean nDigit = Log2count.getBoolLog2Ndigit(globalCount);
			//if bits are equals
			if (!(nDigit^initBoolSeq[localCount])) {
				localCount++;
				//System.out.println("Correct=" + nDigit);
			} else {
				if (localCount > maxResult) {
					maxResult = localCount;
					System.out.println("Global: " + globalCount + " Max: " + maxResult);
				}
				if (localCount > 0) {
					globalCount--;
				}
				localCount = 0;
				
			}
			if (localCount >= sequenceBitLength) {
				searching = false;
			}
			globalCount++;
		}
		
		long offset = globalCount - sequenceBitLength;
		System.out.println("Global: " + globalCount + " Offset: " + offset);
		System.out.println();
		System.out.println();
		Date finishTime = new Date();
		long longFinish = finishTime.getTime() / 1000;
		System.out.println("Search for sequence completed");
		System.out.println("Finish Time: " + finishTime + "Long(sec): " + longFinish);
		long totalTime = longFinish - longStart;
		System.out.println("Total time (sec): " + totalTime);
		
		SimpleOffset result = new SimpleOffset(offset, sequenceBitLength);
		result.setInitSeq(initSeq);
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
