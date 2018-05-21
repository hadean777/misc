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


    public byte[] getRandomHash512() {

//        AudioFormat format = new AudioFormat(44100, 8, 2, true, true);
//
//        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
//        //DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
        byte[] targetData = audioService.getSoundData();

        byte[] hash = hashManager.getSHA512(targetData);

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
        byte[] array = new byte[64];

        Random rand = new Random();

        for (int i = 0; i < 64; i++) {
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



}
