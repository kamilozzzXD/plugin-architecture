package org.example.plugins.textoaaudio;

import org.example.core.AppContext;
import org.example.plugins.textoaaudio.impl.VoskAudioToText;
import org.example.interfaces.PluginFiltro;

import java.io.File;

public class PluginObtenerTextoAAudio implements PluginFiltro {
    @Override
    public String getNombre() {
        return "Obtener texto del archivo de audio";
    }

    @Override
    public String getDescripcion() {
        return "Convierte el audio de un archivo WAV a texto utilizando el modelo de Vosk.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        // Asume que el tipo es el mismo que el del archivo
        return "audio".equalsIgnoreCase(tipoArchivo);
    }

    @Override
    public String ejecutar(AppContext contexto) {
        File archivo = contexto.getUltimoArchivoAudio(); // Usa el método correcto para obtener el archivo de audio
        if (archivo == null || !archivo.exists()) {
            return "ERROR: No hay archivo de audio cargado en la aplicación.";
        }

        VoskAudioToText sttService = new VoskAudioToText();
        try {
            // El servicio de Vosk se encarga de las conversiones de formato
            String transcript = sttService.transcribe(archivo);

            // Vosk devuelve el texto en formato JSON, debemos extraerlo.
            // Una implementación simple para extraer el texto
            int startIndex = transcript.indexOf("\"text\" : \"");
            int endIndex = transcript.lastIndexOf("\"");

            if (startIndex != -1 && endIndex != -1) {
                String finalResult = transcript.substring(startIndex + 10, endIndex);
                // Si el resultado es una cadena vacía, indica que no se reconoció nada.
                if(finalResult.trim().isEmpty()){
                    return "No se ha podido transcribir el audio, no se encontró voz.";
                }
                return finalResult;
            } else {
                return "Error al procesar la transcripción del audio.";
            }

        } catch (Exception e) {
            System.err.println("Error al transcribir el audio: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: No se pudo transcribir el audio. " + e.getMessage();
        }
    }

    @Override
    public boolean requiereArchivoInicial() {
        // Este plugin siempre requiere que haya un archivo cargado.
        return true;
    }
}