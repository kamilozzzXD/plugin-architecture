package org.example.plugins.reproducir.service;

import javax.sound.sampled.*;
import java.io.File;

public class ReproductorService {
    private Clip clip;
    private AudioInputStream audioStream;
    private boolean paused = false;
    private long pauseMicroseconds = 0;

    /**
     * Abre y reproduce el archivo en un hilo separado.
     */
    public void play(File archivo) throws Exception {
        // stop previous if playing
        stop();

        audioStream = AudioSystem.getAudioInputStream(archivo);
        clip = AudioSystem.getClip();
        // abrir y reproducir en un hilo para no bloquear la UI
        Thread t = new Thread(() -> {
            try {
                clip.open(audioStream);
                clip.setFramePosition(0);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Reproductor-Thread");
        t.setDaemon(true);
        t.start();
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pauseMicroseconds = clip.getMicrosecondPosition();
            clip.stop();
            paused = true;
        }
    }

    public void resume() {
        if (clip != null && paused) {
            clip.setMicrosecondPosition(pauseMicroseconds);
            clip.start();
            paused = false;
        }
    }

    public void stop() {
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
                clip = null;
            }
            if (audioStream != null) {
                audioStream.close();
                audioStream = null;
            }
        } catch (Exception ignored) {}
        paused = false;
        pauseMicroseconds = 0;
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
