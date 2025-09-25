package org.example;

import org.example.impl.ESpeakTTSPlugin;
import org.example.interfaces.TTSPlugin;

public class Main {
    public static void main(String[] args)  {
        try {
        TTSPlugin ttsPlugin = new ESpeakTTSPlugin();
        ttsPlugin.configurarVoz(1.2, 0.1); // Velocidad 1.2x, tono ligeramente alto
        ttsPlugin.establecerVoz("es");

        // Reproducir texto directamente
        String texto = "Hola, este es un ejemplo de conversión de texto a voz en Java";
        System.out.println("Reproduciendo: " + texto);
        boolean exito = ttsPlugin.reproducirTexto(texto);

        if (exito) {
            System.out.println("Reproducción exitosa");
        } else {
            System.out.println("Error en la reproducción");
        }

        // Guardar como archivo de audio
        String rutaArchivo = "salida_audio.wav";
        boolean guardado = ttsPlugin.convertirTextoAudio(texto, rutaArchivo);

        if (guardado) {
            System.out.println("Audio guardado en: " + rutaArchivo);
        } else {
            System.out.println("Error guardando el audio");
        }

    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
    }
}