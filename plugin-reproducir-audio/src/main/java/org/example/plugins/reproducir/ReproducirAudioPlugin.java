package org.example.plugins.reproducir;

import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;
import org.example.plugins.reproducir.service.ReproductorService;
import org.example.plugins.reproducir.ui.ReproductorFrame;
import org.example.plugins.reproducir.utils.AudioValidator;

import javax.swing.*;
import java.io.File;

public class ReproducirAudioPlugin implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Reproducir archivo de audio";
    }

    @Override
    public String getDescripcion() {
        return "Permite reproducir un archivo WAV desde la aplicación con controles Play/Pause/Resume/Stop.";
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

        // Abrir ventana de control en EDT
        SwingUtilities.invokeLater(() -> {
            ReproductorService service = new ReproductorService();
            ReproductorFrame frame = new ReproductorFrame(archivo, service);
            frame.setVisible(true);
        });

        return "OK: Ventana de control abierta para " + archivo.getName();
    }
}
