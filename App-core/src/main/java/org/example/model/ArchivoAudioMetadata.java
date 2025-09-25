package org.example.model;

import java.sql.Timestamp;

public class ArchivoAudioMetadata {
    private final long id;
    private final String nombre;
    private final String ruta;
    private final Timestamp fecha;

    public ArchivoAudioMetadata(long id, String nombre, String ruta, Timestamp fecha) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.fecha = fecha;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRuta() { return ruta; }
    public Timestamp getFecha() { return fecha; }

    @Override
    public String toString() {
        return "ArchivoAudioMetadata{id=" + id + ", nombre='" + nombre + "', ruta='" + ruta + "', fecha=" + fecha + "}";
    }
}
