package org.example.model;


import java.io.File;

public class ArchivoTexto {
    private final File archivo;

    public ArchivoTexto(File archivo) {
        this.archivo = archivo;
    }

    public File getArchivo() {
        return archivo;
    }
}

