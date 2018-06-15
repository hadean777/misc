package pi_count;

import java.math.BigDecimal;

public class Main {
	
	//Euler number:
	public static final double e = 2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427427466391932003059921817413596629043572900334295260;
	
	//log(2)
	public static final double LOG2 = 0.693147180559945;
	
	//Fractional part of log(2) from MATLAB bin(fi)
	public final static String LOG2_MODEL_STR = "0101100010111001"; 
	public final static byte[] LOG2_MODEL_BIN = {0,1,0,1,1,0,0,0,1,0,1,1,1,0,0,1};
	
	
	public final static int PRECISION = 8;
	
	public static void main(String[] args) {
		
		//double fractFromN = 0.00000000000000000000000d;
		
		long N = 7; //number of binary digit we are searching

		long n = N;
		
		long inf = 5000; // precision (instead of infinity)
		
		byte nDigit = getLog2Ndigit(n, inf);

		//System.out.println("Fract Result = " + fractFromN);
		System.out.println("Fract Model = " + LOG2_MODEL_STR);
		
	}
	
	private static byte getLog2Ndigit(long n, long inf) {
		byte result = 0;

		double fractFromN = 0.00000000000000000000000d;

		fractFromN = countModSum(1, n) % 1;
		System.out.println("Fract Result 1 = " + fractFromN);

		getBinaryFract(fractFromN);

		//String binStr = Long.toBinaryString(Double.doubleToLongBits(fractFromN));
//		System.out.println(binStr);
//		System.out.println(binStr.substring(11));
		fractFromN = (fractFromN + countSum(n+1, inf)) % 1;
		System.out.println("Fract Result 2 = " + fractFromN);
		System.out.println();
		return result;
	}
	
	private static double countSum(long k, long n) {
		double result = 0;
		for (long i = k; i <= n; i++) {
			result = result + (((double) twoPow(n - k) / (double) i) % 1) ;
			//result = result + ((double) twoPow(n - k) / (double) i);
		}
		result = result % 1;
		System.out.println("k= " + k);
		System.out.println("n= " + n);
		System.out.println("sumRes= " + result);
		System.out.println();
		return result;
	}
	
	private static double countModSum(long k, long n) {
		double result = 0;
		for (long i = k; i <= n; i++) {
			result = result + ((modNumerator(i, n) / (double) i) % 1) ;
			//result = result + (modNumerator(i, n) / (double) i);
		}
		System.out.println("k= " + k);
		System.out.println("n= " + n);
		System.out.println("modSumRes= " + result);
		System.out.println();
		//result = result % 1;
		return result;
	}


	private static double modNumerator(long k, long n) {
		double result = 0;
		
		result = (double) twoPow(n - k) % k;
		
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
	
	
	
	////////////////////////////////////
	private static String getBinaryFract(Double x) {
		String result = "";
		
		//final int precision = 8;
		
		String decimalStr = x.toString();
		String heximalStr = Double.toHexString(x);
		System.out.println("DEC: " + decimalStr);
		System.out.println("HEX: " + heximalStr);
		
		String decimalFract = decimalStr.substring(2, 2 + PRECISION);
		System.out.println("decimalFract: " + decimalFract);
		System.out.println();
		
		byte[] decimalBytes = new byte[PRECISION];
		for (int i = 0; i < decimalFract.length(); i++) {
			decimalBytes[i] = Byte.parseByte(String.valueOf(decimalFract.charAt(i)));
			//decimalBytes[i] = (byte) decimalFract.charAt(i);
			System.out.println(decimalBytes[i]);
		}
		
		return result;
	}

}
