package com.hadean777.misc.manager;


public interface RandomManager {

    void startSound();

    byte[] getRandomHash512();

    byte[] getRandomNumbersNative();

    Double countEntropy(byte[] input);

    Double binaryTest(byte[] input);
}
