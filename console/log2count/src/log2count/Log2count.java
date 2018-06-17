package log2count;

public class Log2count {
	
	public final static int PRECISION = 8;
	
	public static boolean getBoolLog2Ndigit(long digit) {
		boolean result = false;

		double fractFromN = 0.00000000000000000000000d;

		fractFromN = countModSum(1, digit) % 1;
		
		long inf = digit + 1 + PRECISION;
		
		fractFromN = (fractFromN + countSum(digit+1, digit, inf)) % 1;
		
		byte[] binaryFract = getBinaryFract(fractFromN);
		if (binaryFract[0] == 1) {
			result = true;
		}

		return result;
	}
	
	public static byte getLog2Ndigit(long digit) {
		byte result = 0;

		double fractFromN = 0.00000000000000000000000d;

		fractFromN = countModSum(1, digit) % 1;
		
		long inf = digit + 1 + PRECISION;
		
		fractFromN = (fractFromN + countSum(digit+1, digit, inf)) % 1;
		
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
		//result = result % 1;

		return result;
	}
	
	private static double countModSum(long k, long n) {
		double result = 0;
		for (long i = k; i <= n; i++) {
			result = result + ((modNumerator(i, n) / (double) i) % 1) ;
			//result = result + (modNumerator(i, n) / (double) i);
		}

		return result;
	}


	private static double modNumerator(long k, long n) {
		double result = 0;
		
		result = (double) twoPow(n - k) % k;
		//result = (double) twoPow(n - k);
		
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
	
	
	
	private static byte[] getBinaryFract(Double x) {
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
