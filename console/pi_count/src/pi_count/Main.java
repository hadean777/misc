package pi_count;

public class Main {
	
	public static void main(String[] args) {
		
		double e = 2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427427466391932003059921817413596629043572900334295260;
		
//		long n = 8;
//		long sum = 0;
//		
//		for (long k = 0; k <= n; k++) {
//			sum = sum + countIteration(k, n);
//		}
		
//		System.out.println(sum);
		
		double loge = Math.log(e);
		Double log2 = Math.log(2);
		
		String log2str = log2.toString();
		String fractStr = log2str.substring(2,8);
		Long fract = Long.parseLong(fractStr);
		
		
		
		System.out.println("Log e = " + loge);
		System.out.println("Log 2 = " + log2 + " Also: " + Double.doubleToLongBits(0.01d));
		
	}

	
	private static long countIteration(long k, long n) {
		long result = 0;
		
		long h1 = hexPow(n-k);
		long h2 = 8*k+1;
		
		result = (h1%h2)/h2;
		
		return result;
	}
	
	private static long hexPow(long n) {
		long result = 1;
		
		for (long i = 0; i < n; i++) {
			result = result * 16;
		}
		
		return result;
	}
}
