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
            //toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {

        }

        return result;
    }
}
