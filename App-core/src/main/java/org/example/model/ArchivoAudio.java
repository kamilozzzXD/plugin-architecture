package org.example.model;

import java.io.File;

public class ArchivoAudio {
    private final File archivo;

    public ArchivoAudio(File archivo) {
        this.archivo = archivo;
    }

    public File getArchivo() {
        return archivo;
    }
}
