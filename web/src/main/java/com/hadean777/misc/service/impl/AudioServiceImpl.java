package com.hadean777.misc.service.impl;


import com.hadean777.misc.AppConstants;
import com.hadean777.misc.service.AudioService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sound.sampled.*;

@Service(AppConstants.MANAGER_BEAN_AUDIO_SERVICE)
public class AudioServiceImpl implements AudioService {

    private TargetDataLine targetLine = null;

    @PostConstruct
    public void init() {
        try {
            AudioFormat format = new AudioFormat(44100, 8, 2, true, true);
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public byte[] getSoundData() {
        byte[] targetData = new byte[targetLine.getBufferSize()];

        targetLine.read(targetData, 0, targetData.length);

        return targetData;
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