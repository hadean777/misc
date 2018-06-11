package com.hadean777.misc.manager.impl;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.HashManager;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;

@Service(AppConstants.MANAGER_BEAN_HASH_MANAGER)
public class HashManagerImpl implements HashManager {

    public String getSHA512(String input) {
        String result = null;

        if (input != null) {
            byte[] sha512 = getSHA512(input.getBytes());
            result = String.format("%040x", new BigInteger(1, sha512));
        }

        return result;
    }

    public byte[] getSHA512(byte[] input) {

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

    public String proofOfWork512(String input, Integer difficulty) {
        String result = null;
        byte[] byteRes = null;
        long counter = 0;
        if (input == null) {
            result = "";
        } else {
            result = input;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            boolean searchAgain = true;
            while (searchAgain) {
                result = input + counter;
                digest.reset();
                digest.update(result.getBytes("UTF-8"));
                byteRes = digest.digest();
                int numZeros = 0;
                for (int i = 0; i <= difficulty; i++) {
                    if (byteRes[i] != 0) {
                        break;
                    } else {
                        numZeros++;
                    }
                }
                if (numZeros == difficulty) {
                    searchAgain = false;
                }
                counter++;
            }


        } catch (Exception e) {

        }




        return result;
    }

}
