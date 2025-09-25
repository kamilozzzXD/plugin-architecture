package org.example.plugins.reproducir;

import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;
import org.example.plugins.reproducir.utils.AudioValidator;
import java.io.File;

public class ReproducirAudioPlugin implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Reproducir archivo de audio";
    }

    @Override
    public String getDescripcion() {
        return "Prepara un archivo de audio para su reproducción.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        return "audio".equalsIgnoreCase(tipoArchivo);
    }

    @Override
    public String ejecutar(AppContext contexto) {
        File archivo = contexto.getUltimoArchivo();
        if (archivo == null) {
            return "ERROR: No hay archivo en el contexto.";
        }

        String validacion = AudioValidator.validar(archivo);
        if (!"OK".equals(validacion)) {
            return validacion;
        }

        // The key change is here:
        // Do NOT create a new JFrame or other UI component.
        // Instead, just return a status message.
        return "OK: Archivo de audio listo para reproducir.";
    }
}