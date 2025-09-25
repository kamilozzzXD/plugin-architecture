package org.example.plugins.listar;

import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;
import org.example.model.ArchivoAudioMetadata;
import org.example.plugins.listar.model.AudioMetadata;
import org.example.plugins.listar.service.AudioQueryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin que consulta los audios en la BD y los expone en el AppContext.
 * La interfaz gráfica la maneja VentanaPrincipal.
 */
public class ListarAudiosPlugin implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Listar archivos guardados en base de datos";
    }

    @Override
    public String getDescripcion() {
        return "Consulta la tabla AUDIOS en H2 y expone la lista en el contexto.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        // Este plugin no depende de un archivo específico
        return "desconocido".equalsIgnoreCase(tipoArchivo) || "audio".equalsIgnoreCase(tipoArchivo);
    }

    @Override
    public String ejecutar(AppContext contexto) {
        AudioQueryService service;
        try {
            service = new AudioQueryService();
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error al inicializar servicio BD: " + e.getMessage();
        }

        List<AudioMetadata> lista;
        try {
            lista = service.listarTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error al consultar BD: " + e.getMessage();
        }

        if (lista == null || lista.isEmpty()) {
            return "ℹ️ BD vacía, sin audios.";
        }

        // Convertir a POJO conocido por el core
        List<ArchivoAudioMetadata> coreList = new ArrayList<>();
        for (AudioMetadata m : lista) {
            coreList.add(new ArchivoAudioMetadata(
                    m.getId(),
                    m.getNombre(),
                    m.getRuta(),
                    m.getFecha()
            ));
        }

        // Guardar la lista en el AppContext para que VentanaPrincipal la use
        contexto.putServicio("audiosBD", coreList);

        return "OK: " + coreList.size() + " audios listados.";
    }
}
