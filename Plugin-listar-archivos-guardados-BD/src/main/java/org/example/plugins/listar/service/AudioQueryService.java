package org.example.plugins.listar.service;

import org.example.plugins.listar.model.AudioMetadata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para consultar la tabla AUDIOS en H2.
 * Configuración: jdbc:h2:~/tabla (user=sa, password="")
 */
public class AudioQueryService {

    private final String h2Url;
    private final String h2User;
    private final String h2Password;

    public AudioQueryService() throws SQLException {
        this.h2Url = "jdbc:h2:file:./data/tabla";
        this.h2User = "sa";
        this.h2Password = "";

        try (Connection conn = getConnection()) {
            createTableIfNotExists(conn);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(h2Url, h2User, h2Password);
    }

    private void createTableIfNotExists(Connection conn) throws SQLException {
        String ddl = "CREATE TABLE IF NOT EXISTS Audios (" +
                "id IDENTITY PRIMARY KEY, " +
                "nombre VARCHAR(255), " +
                "ruta VARCHAR(1024), " +
                "fecha TIMESTAMP)";
        try (Statement st = conn.createStatement()) {
            st.execute(ddl);
        }
    }

    public List<AudioMetadata> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre, ruta, fecha_creacion FROM Audios ORDER BY fecha_creacion DESC";
        List<AudioMetadata> result = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String nombre = rs.getString("nombre");
                String ruta = rs.getString("ruta");
                Timestamp fecha = rs.getTimestamp("fecha_creacion"); // 👈 usar nombre correcto
                result.add(new AudioMetadata(id, nombre, ruta, fecha));
            }
        }
        return result;
    }

}
