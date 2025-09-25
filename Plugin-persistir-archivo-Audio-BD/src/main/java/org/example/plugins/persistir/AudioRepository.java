package org.example.plugins.persistir;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class AudioRepository {

    private final String h2Url = "jdbc:h2:file:./data/tabla";
    private final String h2User = "sa";
    private final String h2Password = "";

    public AudioRepository() {
        inicializarTabla();
    }

    private void inicializarTabla() {
        try (Connection conn = DriverManager.getConnection(h2Url, h2User, h2Password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Audios (" +
                            "id IDENTITY PRIMARY KEY, " +
                            "nombre VARCHAR(255), " +
                            "ruta VARCHAR(500), " +
                            "fecha_creacion TIMESTAMP)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long guardarAudio(String nombre, String ruta) throws SQLException {
        String sql = "INSERT INTO Audios (nombre, ruta, fecha_creacion) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(h2Url, h2User, h2Password);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, ruta);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    public Optional<AudioRecord> buscarPorId(long id) {
        String sql = "SELECT id, nombre, ruta, fecha_creacion FROM Audios WHERE id=?";
        try (Connection conn = DriverManager.getConnection(h2Url, h2User, h2Password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new AudioRecord(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("ruta"),
                            rs.getTimestamp("fecha_creacion").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
