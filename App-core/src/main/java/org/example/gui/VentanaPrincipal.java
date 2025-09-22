package org.example.gui;


import org.example.core.*;
import org.example.interfaces.PluginFiltro;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final AudioRecorder audioRecorder;
    private final PluginManager pluginManager;
    private final ButtonGroup grupoPlugins;

    public VentanaPrincipal() {
        this.audioRecorder = new AudioRecorder();
        this.pluginManager = new PluginManager();
        this.grupoPlugins = new ButtonGroup();

        setTitle("App Núcleo - Arquitectura Plugin");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



// --- Barra de menú ---
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlugins = new JMenu("Plugins");

// cargar plugins dinámicamente
        List<PluginFiltro> plugins = pluginManager.cargarPlugins();
        if (plugins.isEmpty()) {
            JMenuItem noPlugins = new JMenuItem("No hay plugins cargados");
            noPlugins.setEnabled(false);
            menuPlugins.add(noPlugins);
        } else {
            for (PluginFiltro plugin : plugins) {
                JRadioButtonMenuItem item = new JRadioButtonMenuItem(plugin.getNombre());
                item.addActionListener(e -> {
                    AppContext contexto = AppContextSingleton.get(); // usamos una única instancia compartida

                    // si no hay archivo cargado todavía, pedir al usuario
                    // Solo si el plugin requiere archivo inicial
                    if (plugin.requiereArchivoInicial() && contexto.getUltimoArchivoAudio() == null) {
                        JFileChooser chooser = new JFileChooser();
                        int ret = chooser.showOpenDialog(this);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            contexto.setUltimoArchivoAudio(chooser.getSelectedFile());
                        }
                    }


                    FiltroExecutor executor = new FiltroExecutor();
                    String resultado = executor.ejecutarFiltro(plugin, contexto);

                    JOptionPane.showMessageDialog(this,
                            "Descripción: " + plugin.getDescripcion() + "\n\nResultado: " + resultado,
                            "Plugin ejecutado",
                            JOptionPane.INFORMATION_MESSAGE);
                });
                grupoPlugins.add(item);
                menuPlugins.add(item);
            }
        }

        menuBar.add(menuPlugins);
        setJMenuBar(menuBar);

// --- Panel principal ---
        JLabel label = new JLabel("Bienvenido al núcleo de la aplicación", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        JButton grabarBtn = new JButton("Grabar 5sg");
        grabarBtn.addActionListener(e -> {
            // Generar nombre único con timestamp
            String nombreArchivo = "grabacion_" + System.currentTimeMillis() + ".wav";
            File output = new File(nombreArchivo);

            audioRecorder.startRecording(output, 5);

            // actualizar AppContext con el nuevo archivo
            AppContext contexto = AppContextSingleton.get();
            contexto.setUltimoArchivoAudio(output);

            JOptionPane.showMessageDialog(this,
                    "Grabación finalizada.\nArchivo: " + output.getAbsolutePath(),
                    "Grabación", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel panelCentro = new JPanel();
        panelCentro.add(grabarBtn);
        add(panelCentro, BorderLayout.CENTER);

    }
}
