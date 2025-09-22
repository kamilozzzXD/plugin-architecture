package org.example.plugins.listar;

import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;
import org.example.plugins.listar.model.AudioMetadata;
import org.example.plugins.listar.service.AudioQueryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Plugin que lista los audios guardados en BD y permite seleccionar uno
 * para usarlo como ultimoArchivoAudio en AppContext.
 */
public class ListarAudiosPlugin implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Listar audios en BD";
    }

    @Override
    public String getDescripcion() {
        return "Consulta la base de datos H2 y muestra la lista de audios almacenados.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        // No requiere archivo: soporta "desconocido" y "audio"
        return "desconocido".equalsIgnoreCase(tipoArchivo) || "audio".equalsIgnoreCase(tipoArchivo);
    }
    @Override
    public boolean requiereArchivoInicial() {
        return false;
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

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay audios guardados en la base de datos.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return "ℹ️ BD vacía, sin audios.";
        }

        // Preparar tabla
        String[] columnNames = {"ID", "Nombre", "Ruta", "Fecha", "Existe"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (AudioMetadata m : lista) {
            File f = m.getRuta() == null ? null : new File(m.getRuta());
            boolean exists = f != null && f.exists();
            model.addRow(new Object[]{
                    m.getId(),
                    m.getNombre(),
                    m.getRuta(),
                    m.getFecha(),
                    exists ? "Sí" : "No"
            });
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCerrar);

        JDialog dialog = new JDialog((Frame) null, "Audios en BD", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        dialog.setSize(800, 300);
        dialog.setLocationRelativeTo(null);

        // Acción seleccionar
        btnSeleccionar.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Selecciona primero un registro.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String ruta = (String) model.getValueAt(sel, 2);
            File f = new File(ruta);
            contexto.setUltimoArchivoAudio(f);

            JOptionPane.showMessageDialog(dialog,
                    "Archivo seleccionado: " + ruta,
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción cerrar
        btnCerrar.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);

        return "OK: " + lista.size() + " audios listados.";
    }
}
