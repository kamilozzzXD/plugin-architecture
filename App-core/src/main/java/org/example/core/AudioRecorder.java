package org.example.core;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    private TargetDataLine line;
    private File outputFile;
    private Thread recordThread;

    public void startRecording(File outputFile) {
        this.outputFile = outputFile;
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Micrófono no soportado.");
                return;
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            recordThread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(line)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            recordThread.start();
            System.out.println("Grabación iniciada.");

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public File stopRecording() {
        if (line != null) {
            line.stop();
            line.close();
            System.out.println("Grabación guardada en: " + outputFile.getAbsolutePath());
            return outputFile;
        }
        return null;
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}