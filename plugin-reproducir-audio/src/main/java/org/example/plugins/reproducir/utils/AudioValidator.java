package org.example.plugins.reproducir.utils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class AudioValidator {

    public static boolean esValido(File archivo) {
        if (archivo == null) {
            return false;
        }
        if (!archivo.exists() || !archivo.isFile() || !archivo.canRead()) {
            return false;
        }
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".wav"); // por ahora solo WAV
    }

    public static boolean esFormatoSoportado(File archivo) {
        try {
            AudioFileFormat formato = AudioSystem.getAudioFileFormat(archivo);
            return formato != null;
        } catch (IOException | javax.sound.sampled.UnsupportedAudioFileException e) {
            return false;
        }
    }

    public static String validar(File archivo) {
        if (archivo == null) {
            return "ERROR: Archivo nulo.";
        }
        if (!archivo.exists()) {
            return "ERROR: El archivo no existe.";
        }
        if (!archivo.canRead()) {
            return "ERROR: No se puede leer el archivo.";
        }
        if (!esValido(archivo)) {
            return "ERROR: Solo se soportan archivos WAV en esta versión.";
        }
        if (!esFormatoSoportado(archivo)) {
            return "ERROR: El formato del archivo no es soportado por AudioSystem.";
        }
        return "OK";
    }
}
