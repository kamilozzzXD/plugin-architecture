package org.example;

import org.example.api.AudioToTextService;
import org.example.impl.VoskAudioToText;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Asegúrate de que el archivo de audio exista en esta ruta
        // y que sea un archivo .wav con la configuración adecuada para Vosk (16 kHz, mono)
        File audioFile = new File("prueba3.wav");

        AudioToTextService sttService = new VoskAudioToText();

        try {
            String transcript = sttService.transcribe(audioFile);
            System.out.println("Transcripción completada: " + transcript);
        } catch (Exception e) {
            System.err.println("Error al transcribir el audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}