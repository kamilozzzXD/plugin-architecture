package org.example.plugins.persistir;

import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;

public class PersistirAudioPlugin implements PluginFiltro {

    private final AudioRepository repository;

    public PersistirAudioPlugin() {
        this.repository = new AudioRepository();
    }

    @Override
    public String getNombre() {
        return "Persistir Audio en BD";
    }

    @Override
    public String getDescripcion() {
        return "Guarda el archivo de audio en la base de datos H2, almacenando ruta relativa y metadata.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        return "audio".equalsIgnoreCase(tipoArchivo);
    }

    @Override
    public String ejecutar(AppContext contexto) {
        File audioFile = contexto.getUltimoArchivoAudio();
        if (audioFile == null || !audioFile.exists()) {
            return "❌ No se encontró un archivo de audio válido en el contexto.";
        }

        try {
            // Directorio raíz del proyecto
            File baseDir = new File(System.getProperty("user.dir"));
            Path basePath = baseDir.toPath().toAbsolutePath().normalize();
            Path audioPath = audioFile.toPath().toAbsolutePath().normalize();

            // Calcular ruta relativa si está dentro del proyecto, de lo contrario usar absoluta
            String relativePath;
            if (audioPath.startsWith(basePath)) {
                relativePath = basePath.relativize(audioPath).toString();
            } else {
                relativePath = audioPath.toString();
            }

            // Guardar en BD
            long id = repository.guardarAudio(audioFile.getName(), relativePath);
            if (id > 0) {
                contexto.setUltimoResultado("Audio guardado con ID=" + id);
                contexto.putServicio("lastSavedAudioId", id);
                return "✅ Audio persistido correctamente con ID=" + id + "\nRuta guardada: " + relativePath;
            } else {
                return "❌ No se pudo guardar el audio en la BD.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error al guardar en BD: " + e.getMessage();
        }
    }
}
