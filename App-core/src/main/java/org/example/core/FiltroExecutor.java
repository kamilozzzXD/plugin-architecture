package org.example.core;


import org.example.interfaces.PluginFiltro;

import java.io.File;

public class FiltroExecutor {

    public String ejecutarFiltro(PluginFiltro filtro, File archivo) {
        if (!filtro.soportaTipo(obtenerTipoArchivo(archivo))) {
            return "El plugin no soporta este tipo de archivo.";
        }
        return filtro.ejecutar(archivo);
    }

    private String obtenerTipoArchivo(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        if (nombre.endsWith(".mp3") || nombre.endsWith(".wav")) {
            return "audio";
        } else if (nombre.endsWith(".txt")) {
            return "texto";
        }
        return "desconocido";
    }
}
