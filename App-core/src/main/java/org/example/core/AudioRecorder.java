package org.example.core;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    private TargetDataLine line;

    public void startRecording(File outputFile, int durationSeconds) {
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

            Thread thread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(line)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.start();

            // detener después de cierto tiempo
            Thread.sleep(durationSeconds * 1000);
            stopRecording();

            System.out.println("Grabación guardada en: " + outputFile.getAbsolutePath());

        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (line != null) {
            line.stop();
            line.close();
        }
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
