package pi_count;

public class Main {
	
	//Euler number:
	public static final double e = 2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427427466391932003059921817413596629043572900334295260;
	
	//log(2)
	public static final double LOG2 = 0.693147180559945;
	
	//Fractional part of log(2) from MATLAB bin(fi)
	public final static String LOG2_MODEL_STR = "0101100010111001"; 
	public final static byte[] LOG2_MODEL_BIN = {0,1,0,1,1,0,0,0,1,0,1,1,1,0,0,1};
	
	public static void main(String[] args) {
		
		//double fractFromN = 0.00000000000000000000000d;
		
		long n = 8; //number of binary digit we are searching
		
		long inf = 10; // precision (instead of infinity)
		
		byte nDigit = getLog2Ndigit(n, inf);

		//System.out.println("Fract Result = " + fractFromN);
		System.out.println("Fract Model = " + LOG2_MODEL_STR);
		
	}
	
	private static byte getLog2Ndigit(long n, long inf) {
		byte result = 0;
		
		double fractFromN = 0.00000000000000000000000d;
		
		fractFromN = countModSum(1, n);
		System.out.println("Fract Result 1 = " + fractFromN);
		System.out.println();
		fractFromN = (fractFromN + countSum(n, inf)) % 1;
		System.out.println("Fract Result 2 = " + fractFromN);
		System.out.println();
		return result;
	}
	
	private static double countSum(long k, long n) {
		double result = 0;
		for (long i = k; i <= n; i++) {
			result = result + (((double) twoPow(n - k) / (double) i) % 1) ;
		}
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
		}
		System.out.println("k= " + k);
		System.out.println("n= " + n);
		System.out.println("modSumRes= " + result);
		System.out.println();
		return result;
	}

	//TODO: should be optimized with "binary algorithm for exponentiation"
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
	
}
