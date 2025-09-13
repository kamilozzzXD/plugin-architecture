package org.example.interfaces;


import java.io.File;

public interface PluginFiltro {
    String getNombre();
    String getDescripcion();
    boolean soportaTipo(String tipoArchivo);
    String ejecutar(File archivo);
}
