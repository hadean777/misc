package pi_count;

import java.math.BigDecimal;

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
	
	
	public final static int PRECISION = 8;
	
	public static void main(String[] args) {
		
		//double fractFromN = 0.00000000000000000000000d;
		
		byte[] log2BinaryFract = getBinaryFract(LOG2);
		System.out.println("L2bin: ");
		for (int i = 0; i < PRECISION; i++) {
			System.out.print(log2BinaryFract[i]);
		}
		System.out.println();
//		long N = 7; //number of binary digit we are searching
//
//		long n = N;
		
		for (int n = 0; n <= LOG2_MODEL_BIN.length; n++) {
			byte nDigit = getLog2Ndigit(n);
			System.out.print(nDigit);
		}
		System.out.println();
//		byte nDigit = getLog2Ndigit(1);
//		System.out.print(nDigit);
		//System.out.println("Fract Result = " + fractFromN);
		System.out.println("Fract Model = " + LOG2_MODEL_STR);
		
	}
	
	private static byte getLog2Ndigit(long digit) {
		byte result = 0;

		double fractFromN = 0.00000000000000000000000d;

		fractFromN = countModSum(1, digit) % 1;
//		System.out.println("Fract Result 1 = " + fractFromN);

		//byte[] binaryFract = getBinaryFract(fractFromN);
		
//		for (int i = 0; i < PRECISION; i++) {
//			System.out.print(binaryFract[i]);
//		}
//		System.out.println();
		
		long inf = digit + 1 + 8;
		
		fractFromN = (fractFromN + countSum(digit+1, digit, inf)) % 1;
//		System.out.println("Fract Result 2 = " + fractFromN);
//		System.out.println();
		
		byte[] binaryFract = getBinaryFract(fractFromN);
		result = binaryFract[0];

		return result;
	}
	
	private static double countSum(long k, long digit, long inf) {
		double result = 0;
		for (long i = k; i <= inf; i++) {
			result = result + (((double) twoPowFloat(digit - i) / (double) i) % 1) ;
			//result = result + ((double) twoPow(n - k) / (double) i);
		}
		result = result % 1;
//		System.out.println("k= " + k);
//		System.out.println("n= " + n);
//		System.out.println("sumRes= " + result);
//		System.out.println();
		return result;
	}
	
	private static double countModSum(long k, long n) {
		double result = 0;
		for (long i = k; i <= n; i++) {
			result = result + ((modNumerator(i, n) / (double) i) % 1) ;
			//result = result + (modNumerator(i, n) / (double) i);
		}
//		System.out.println("k= " + k);
//		System.out.println("n= " + n);
//		System.out.println("modSumRes= " + result);
//		System.out.println();
		//result = result % 1;
		return result;
	}


	private static double modNumerator(long k, long n) {
		double result = 0;
		
		result = (double) twoPow(n - k) % k;
		//result = (double) twoPow(n - k);
		
//		System.out.println("k= " + k);
//		System.out.println("n= " + n);
//		System.out.println("res= " + result);
//		System.out.println();
		
		return result;
	}
	
	private static long twoPow(long n) {
		long result = 1;
		for (long i = 1; i <= n; i++) {
			result = result * 2;
		}
		return result;
	}
	
	private static double twoPowFloat(long n) {
		double result = 1.000000000000000000;
		if (n > 0) {
			for (long i = 1; i <= n; i++) {
				result = result * 2;
			}
		} else if (n < 0) {
			for (long i = -1; i >= n; i--) {
				result = result / 2.000000000;
			}
		}
		return result;
	}
	
	
	
	////////////////////////////////////
	private static byte[] getBinaryFract(Double x) {
		//String result = "";
		
		//final int precision = 8;
		
//		String decimalStr = x.toString();
//		String heximalStr = Double.toHexString(x);
//		System.out.println("DEC: " + decimalStr);
//		System.out.println("HEX: " + heximalStr);
//		
//		String decimalFract = decimalStr.substring(2, 2 + PRECISION);
//		System.out.println("decimalFract: " + decimalFract);
//		System.out.println();
//		
//		//long longFract = Long.parseLong(decimalFract);
//		
//		byte[] decimalBytes = new byte[PRECISION];
//		for (int i = 0; i < decimalFract.length(); i++) {
//			decimalBytes[i] = Byte.parseByte(String.valueOf(decimalFract.charAt(i)));
//			//decimalBytes[i] = (byte) decimalFract.charAt(i);
//			System.out.println(decimalBytes[i]);
//		}
		x = x % 1;
		byte[] binaryBytes = new byte[PRECISION];
		for (int i = 0; i < PRECISION; i++) {
			x = x*2;
			if (x > 1.00000000000000000000) {
				binaryBytes[i] = 1;
			} else {
				binaryBytes[i] = 0;
			}
			x = x % 1;
		}
		
		return binaryBytes;
	}

}
