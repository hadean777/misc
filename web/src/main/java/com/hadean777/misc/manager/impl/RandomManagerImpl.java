package com.hadean777.misc.manager.impl;

import javax.sound.sampled.*;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.HashManager;
import com.hadean777.misc.manager.RandomManager;
import com.hadean777.misc.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Random;

@Service(AppConstants.MANAGER_BEAN_RANDOM_MANAGER)
public class RandomManagerImpl implements RandomManager {

    @Autowired
    private HashManager hashManager;

    @Autowired
    @Qualifier(AppConstants.MANAGER_BEAN_AUDIO_SERVICE)
    private AudioService audioService;

    public void startSound() {
        //sampleSound();
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);

        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];

//            numBytesRead = targetLine.read(targetData, 0, targetData.length);
//            if (numBytesRead > 0) {
//
//            }
            //while (true) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                //if (numBytesRead == -1)	break;

                for (int i = 0; i < numBytesRead; i++) {
                    System.out.print(targetData[i]);
                }

                System.out.println();
                //sourceLine.write(targetData, 0, numBytesRead);
                //System.out.print(targetData);

            //}


        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public byte[] getAudioBytes() {
        byte[] targetData = audioService.getSoundData();

        byte[] byteData = convertBitsToBytes(targetData);

        return byteData;
    }

    public byte[] getRandomHash512() {

//        AudioFormat format = new AudioFormat(44100, 8, 2, true, true);
//
//        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
//        //DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        byte[] hash = hashManager.getSHA512(getAudioBytes());

//        try {
//            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
//            targetLine.open(format);
//            targetLine.start();
//
////            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
////            sourceLine.open(format);
////            sourceLine.start();
//
//            int numBytesRead;
//            byte[] targetData = new byte[targetLine.getBufferSize() / 5];
//
//            numBytesRead = targetLine.read(targetData, 0, targetData.length);
//
//            hash = hashManager.getSHA512(targetData);
//
//            System.out.print(String.format("%040x", new BigInteger(1, hash)));
//
//            System.out.println();
//
//
//
//        }
//        catch (Exception e) {
//            System.err.println(e);
//        }

        return hash;
    }

    public byte[] getRandomNumbersNative() {
        //final int RANDOM_SIZE = 64;
        final int RANDOM_SIZE = 5512;
        byte[] array = new byte[RANDOM_SIZE];

        Random rand = new Random();

        for (int i = 0; i < RANDOM_SIZE; i++) {
            Integer randInt = rand.nextInt(255);
            array[i] = randInt.byteValue();
        }

        return array;
    }



    ///////////// --------------------------- Test methods --------------------------------

    public Double countEntropy(byte[] input) {
        double entropy = 0.0;
        int inputSize = input.length - 1;
        int[] frequency_array = new int[256];

        for (int i = 0; i < inputSize; i++) {
            byte item = input[i];
            frequency_array[item + 128]++;
        }

        for (int i = 0; i < frequency_array.length; i++) {
            if (frequency_array[i] != 0) {
                double probabilityOfByte = (double) frequency_array[i] / (double) inputSize;

                double value = probabilityOfByte * (Math.log(probabilityOfByte) / Math.log(2));
                entropy = entropy + value;
            }
        }

        return -1 * entropy;
    }



    public Double binaryTest(byte[] input) {
        double result = 0.0;
        double destinationValue = 0.500000000000000000000000000000000000000000000000000;

        long countOf1 = 0l;
        long totalCount = 0;

        for (int i = 0; i < input.length; i++) {
            byte sym = input[i];
            String binary = String.format("%8s", Integer.toBinaryString(sym & 0xFF)).replace(' ', '0');
            //System.out.println("Bin: " + binary);
            for (int j = 0; j < 8; j++) {
                char c = binary.charAt(j);
                if (c == '1') {
                    countOf1++;
                }
                totalCount++;
            }
        }

        double actualValue = (double) countOf1 / (double) totalCount;

        result = actualValue - destinationValue;

        return result;
    }

    public byte[] convertBitsToBytes(byte[] input) {

        byte[] result = null;

        if (input != null) {

            int inputBufferSize = input.length;
            int outputBufferSize = inputBufferSize / 8;
            if (outputBufferSize > 0) {
                result = new byte[outputBufferSize];
                int currentOffset = 0;
                for (int i = 0; i < outputBufferSize; i++){
                    byte resultByte = 0;
                    for (int j = 0; j < 8; j++) {
                        if (input[currentOffset] != 0) {
                            if (j == 0) {
                                resultByte += 128;
                            } else if (j == 1) {
                                resultByte += 64;
                            } else if (j == 2) {
                                resultByte += 32;
                            } else if (j == 3) {
                                resultByte += 16;
                            } else if (j == 4) {
                                resultByte += 8;
                            } else if (j == 5) {
                                resultByte += 4;
                            } else if (j == 6) {
                                resultByte += 2;
                            } else if (j == 7) {
                                resultByte += 1;
                            }
                        }
                        currentOffset++;
                    }
                    result[i] = resultByte;
                }
            }
        }

        return result;
    }

}
