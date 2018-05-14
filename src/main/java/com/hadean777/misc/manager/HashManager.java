package com.hadean777.misc.manager;


public interface HashManager {


    String getSHA512(String input);

    String proofOfWork512(String input, Integer difficulty);

}