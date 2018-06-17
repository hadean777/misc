package log2count;

import java.util.Date;

import model.SimpleOffset;

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
		
		SimpleOffset seqOffset = Log2seq.findSequenceInLog2(initialSequence);
		
		String restoredSequence = restoreSequenceFromLog2(seqOffset);
		
		
		////// Output
		System.out.println("Initial:  " + seqOffset.getInitSeq());
//		for (int i = 0; i < initialSequence.length; i++) {
//			System.out.print(initialSequence[i]);
//		}
		//System.out.println();
		System.out.println("Restored: " + restoredSequence);
//		for (int i = 0; i < restoredSequence.length; i++) {
//			System.out.print(restoredSequence[i]);
//		}
	}
	
	//TODO: should be implemented load from file
	private static byte[] getInitialSequence() {
		
		//For sequence 6guRczu8eUwBKtWq offset=9097

		String sampleSequence = "63";
		
		System.out.println("Sequence: " + sampleSequence);
		byte[] result = sampleSequence.getBytes();
		return result;
	}
	
	public static String restoreSequenceFromLog2(SimpleOffset seqOffset) {
		
		System.out.println();
		Date startTime = new Date();
		long longStart = startTime.getTime() / 1000;
		System.out.println("Restoring sequence started");
		System.out.println("Start Time: " + startTime + "Long(sec): " + longStart);
		System.out.println();
		
		long initOffset = seqOffset.getOffset();
		int bitLength = seqOffset.getBitLength();
		long endOffset = initOffset + bitLength;
		
		System.out.println("initOffset = " + initOffset);
		System.out.println("bitLength = " + bitLength);
		System.out.println("endOffset = " + endOffset);
		byte[] bits = new byte[bitLength];
		int localCount = 0;
		String result = "";
		for(long globalCount = initOffset; globalCount < endOffset; globalCount++) {
			//bits[localCount] = Log2count.getLog2Ndigit(globalCount);
			boolean bit = Log2count.getBoolLog2Ndigit(globalCount);
			if (bit) {
				result += "1";
			} else {
				result += "0";
			}
			localCount++;
		}
		//byte[] result = encodeToByteArray(bits);
		System.out.println();
		Date finishTime = new Date();
		long longFinish = finishTime.getTime() / 1000;
		System.out.println("Restoring sequence completed");
		System.out.println("Finish Time: " + finishTime + "Long(sec): " + longFinish);
		long totalTime = longFinish - longStart;
		System.out.println("Total time (sec): " + totalTime);
		return result; 
	}
	
	private static byte[] encodeToByteArray(byte[] bits) {
        byte[] results = new byte[(bits.length + 7) / 8];
        int byteValue = 0;
        int index;
        for (index = 0; index < bits.length; index++) {

            byteValue = (byteValue << 1) | bits[index];

            if (index %8 == 7) {
                results[index / 8] = (byte) byteValue;
            }
        }

        if (index % 8 != 0) {
        	Integer bVal = byteValue << (8 - (index % 8));
            results[index / 8] = bVal.byteValue();
        }

        return results;
    }

}
