package org.example.core;

import org.example.interfaces.PluginFiltro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FiltroExecutor {

    private static final Logger logger = Logger.getLogger(FiltroExecutor.class.getName());

    public String ejecutarFiltro(PluginFiltro filtro, AppContext contexto) {
        String tipo = obtenerTipoArchivo(contexto);
        if (!filtro.soportaTipo(tipo)) {
            return "El plugin no soporta este tipo de archivo.";
        }
        return filtro.ejecutar(contexto);
    }

    private String obtenerTipoArchivo(AppContext contexto) {
        File archivo = contexto.getUltimoArchivo();
        if (archivo == null) {
            return "desconocido";
        }
        String nombre = archivo.getName().toLowerCase();
        if (nombre.endsWith(".mp3") || nombre.endsWith(".wav")) {
            return "audio";
        } else if (nombre.endsWith(".txt")) {
            return "texto";
        }
        return "desconocido";
    }
}