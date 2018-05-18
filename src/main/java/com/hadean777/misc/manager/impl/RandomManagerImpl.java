package com.hadean777.misc.manager.impl;

import javax.sound.sampled.*;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.RandomManager;
import org.springframework.stereotype.Service;

@Service(AppConstants.MANAGER_BEAN_RANDOM_MANAGER)
public class RandomManagerImpl implements RandomManager {


    public void startSound() {
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


    public void sampleSound() {
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

            while (true) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                if (numBytesRead == -1)	break;

                sourceLine.write(targetData, 0, numBytesRead);
            }


        }
        catch (Exception e) {
            System.err.println(e);
        }
    }


}
