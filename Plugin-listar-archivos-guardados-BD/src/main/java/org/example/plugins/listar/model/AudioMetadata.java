package org.example.plugins.listar.model;

import java.sql.Timestamp;

public class AudioMetadata {
    private final long id;
    private final String nombre;
    private final String ruta;
    private final Timestamp fecha;

    public AudioMetadata(long id, String nombre, String ruta, Timestamp fecha) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "AudioMetadata{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ruta='" + ruta + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
