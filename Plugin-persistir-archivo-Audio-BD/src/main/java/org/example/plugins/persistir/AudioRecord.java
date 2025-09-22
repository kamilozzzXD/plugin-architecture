package org.example.plugins.persistir;

import java.time.LocalDateTime;

public class AudioRecord {
    private Long id;
    private String nombre;
    private String ruta;
    private LocalDateTime fechaCreacion;

    public AudioRecord(Long id, String nombre, String ruta, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRuta() { return ruta; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    @Override
    public String toString() {
        return "AudioRecord{id=" + id + ", nombre='" + nombre + "', ruta='" + ruta + "', fecha=" + fechaCreacion + "}";
    }
}
