package org.example;

import org.example.core.AppContext;
import org.example.impl.ESpeakTTSPlugin;
import org.example.interfaces.PluginFiltro;
import org.example.interfaces.TTSPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextoAAudio implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Leer archivo de texto - Convertir texto a archivo de audio";
    }

    @Override
    public String getDescripcion() {
        return "Convierte el texto de un archivo a un archivo de audio WAV.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        return "texto".equalsIgnoreCase(tipoArchivo);
    }

    @Override
    public String ejecutar(AppContext contexto) {
        File archivoTexto = contexto.getUltimoArchivoTexto();
        if (archivoTexto == null || !archivoTexto.exists()) {
            return "ERROR: No hay archivo de texto seleccionado en el contexto.";
        }

        try {
            String texto = new String(Files.readAllBytes(Paths.get(archivoTexto.getAbsolutePath())));
            if (texto.trim().isEmpty()) {
                return "ERROR: El archivo de texto está vacío.";
            }

            TTSPlugin ttsPlugin = new ESpeakTTSPlugin();
            ttsPlugin.configurarVoz(1.2, 0.1);
            ttsPlugin.establecerVoz("es");

            // Generar un nombre de archivo único para el audio de salida
            String rutaAudioSalida = "audio_generado_" + System.currentTimeMillis() + ".wav";

            boolean guardado = ttsPlugin.convertirTextoAudio(texto, rutaAudioSalida);

            if (guardado) {
                // Actualizar el contexto con el nuevo archivo de audio para que se pueda reproducir
                File archivoAudioGenerado = new File(rutaAudioSalida);
                contexto.setUltimoArchivoAudio(archivoAudioGenerado);
                return "Audio guardado en: " + archivoAudioGenerado.getAbsolutePath();
            } else {
                return "ERROR: No se pudo guardar el audio.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: Ocurrió un error al procesar el archivo: " + e.getMessage();
        }
    }

    @Override
    public boolean requiereArchivoInicial() {
        // Este plugin necesita un archivo de texto para funcionar
        return true;
    }
}