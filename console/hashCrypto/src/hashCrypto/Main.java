package hashCrypto;

import java.util.zip.CRC32;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Main {
	
	
	public static void main(String[] args) {
		
		byte[] input = getInputArg();
		
		byte[] sha512 = getSHA512(input);
		
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update(sha512);
		
		long result = crc32.getValue();
		
		System.out.println(result);
		System.out.println(Long.toBinaryString(result));
		System.out.println(Long.toHexString(result));
	}

	private static void findInputByCRC(long inputHash, int byteLength) {
		
		int bitLength = byteLength * 8;
		Byte[] result = new Byte[byteLength];
				
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			for (int i = 0; i <= bitLength; i++) {
				
				sha.reset();
            //sha.update(input);
            //result = sha.digest();
			}

        } catch (Exception e) {

        }
		
	}
	
	private static void initByteArray(Byte[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Byte.MIN_VALUE;
		}
	}
	
	private static void incByteArray(Byte[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (incByte(arr[i])) {
				continue;
			} else {
				break;
			}
		}
	}
	
	private static boolean incByte(Byte b) {
		if (b == Byte.MAX_VALUE) {
			b = Byte.MIN_VALUE;
			return true;
		} else {
			b++;
			return false;
		}
	}
	
	private static byte[] getInputArg() {
		String testArg = "12345678";
		
		byte[] inputArg = testArg.getBytes();
		return inputArg;
	}
	
	
	///////////////// SHA-512 /////////////////////////
	public static String getSHA512(String input) {
        String result = null;

        if (input != null) {
            byte[] sha512 = getSHA512(input.getBytes());
            result = String.format("%040x", new BigInteger(1, sha512));
        }

        return result;
    }

    public static byte[] getSHA512(byte[] input) {

        byte[] result = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input);
            result = digest.digest();

        } catch (Exception e) {

        }

        return result;
    }
    
    /////////------------------- CRC -----------------------------
        
}
