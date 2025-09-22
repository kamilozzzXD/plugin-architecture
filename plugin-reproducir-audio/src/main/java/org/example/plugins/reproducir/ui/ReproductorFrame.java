package org.example.plugins.reproducir.ui;

import org.example.plugins.reproducir.service.ReproductorService;

import javax.swing.*;
import java.io.File;

public class ReproductorFrame extends JFrame {
    private final ReproductorService service;
    private final File archivo;

    public ReproductorFrame(File archivo, ReproductorService service) {
        this.archivo = archivo;
        this.service = service;

        setTitle("Reproductor - " + archivo.getName());
        setSize(380, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnPlay = new JButton("Play");
        JButton btnPause = new JButton("Pause");
        JButton btnResume = new JButton("Resume");
        JButton btnStop = new JButton("Stop");

        btnPlay.addActionListener(e -> {
            // ejecutar en background para capturar errores
            new Thread(() -> {
                try {
                    service.play(archivo);
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "Error al reproducir: " + ex.getMessage())
                    );
                }
            }).start();
        });

        btnPause.addActionListener(e -> service.pause());
        btnResume.addActionListener(e -> service.resume());
        btnStop.addActionListener(e -> service.stop());

        JPanel panel = new JPanel();
        panel.add(btnPlay);
        panel.add(btnPause);
        panel.add(btnResume);
        panel.add(btnStop);

        add(panel);
    }
}
