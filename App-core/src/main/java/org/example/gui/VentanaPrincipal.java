package org.example.gui;

import org.example.core.*;
import org.example.interfaces.PluginFiltro;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class VentanaPrincipal extends JFrame {

    private final AudioRecorder audioRecorder;
    private final PluginManager pluginManager;
    private final ButtonGroup grupoPlugins;
    private final JTextArea salidaMensajesArea;
    private final JTextArea listadoArchivosArea;
    private final JTextArea reproducirAudioArea;
    private final JTextArea textoAudioArea;
    private final JTextArea cadenaEncontradaArea;

    private JButton grabarBtn;
    private JButton pararBtn;
    private JLabel statusLabel;

    // Controles de Reproducción
    private JButton playBtn;
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton stopBtn;
    private Clip audioClip;

    private static final Logger logger = Logger.getLogger(VentanaPrincipal.class.getName());

    public VentanaPrincipal() {
        this.audioRecorder = new AudioRecorder();
        this.pluginManager = new PluginManager();
        this.grupoPlugins = new ButtonGroup();
        this.salidaMensajesArea = new JTextArea(5, 20);
        this.listadoArchivosArea = new JTextArea(5, 20);
        this.reproducirAudioArea = new JTextArea(5, 20);
        this.textoAudioArea = new JTextArea(5, 20);
        this.cadenaEncontradaArea = new JTextArea(5, 20);

        setTitle("Aplicación de procesamiento de texto con Arquitectura Plugin");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Principal ---
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        // --- Panel Superior (Botones de Carga y Grabación) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        grabarBtn = new JButton("Grabar");
        pararBtn = new JButton("Parar");
        statusLabel = new JLabel("Listo para grabar");
        pararBtn.setEnabled(false);

        panelSuperior.add(grabarBtn);
        panelSuperior.add(pararBtn);
        panelSuperior.add(statusLabel);

        // --- Panel Izquierdo (Componentes/Filtros) ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        JLabel componentesLabel = new JLabel("Componentes (filtros)");
        componentesLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        List<PluginFiltro> plugins = pluginManager.cargarPlugins();
        for (PluginFiltro plugin : plugins) {
            JRadioButton radioBtn = new JRadioButton(plugin.getNombre());
            radioBtn.addActionListener(e -> ejecutarFiltro(plugin));
            grupoPlugins.add(radioBtn);
            radioButtonsPanel.add(radioBtn);
        }

        JButton ejecutarBtn = new JButton("Ejecutar filtro seleccionado");

        panelIzquierdo.add(componentesLabel, BorderLayout.NORTH);
        panelIzquierdo.add(radioButtonsPanel, BorderLayout.CENTER);
        panelIzquierdo.add(ejecutarBtn, BorderLayout.SOUTH);

        // --- Panel Central (Outputs) ---
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.add(createOutputPanel("Archivos de audio", new JTextArea(5, 20)));
        panelCentral.add(createOutputPanel("Listado de archivos de audio", new JScrollPane(listadoArchivosArea)));

        // Panel de Reproducción integrado
        JPanel panelReproductor = createPlayerPanel();
        panelCentral.add(panelReproductor);

        panelCentral.add(createOutputPanel("Obtener texto del archivo", new JScrollPane(textoAudioArea)));

        // --- Panel Inferior (Salida de Mensajes) ---
        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
        panelInferior.add(createOutputPanel("Salida de mensajes", new JScrollPane(salidaMensajesArea)));
        panelInferior.add(createOutputPanel("Cadena de texto encontrada en los archivos", new JScrollPane(cadenaEncontradaArea)));

        // Agregando paneles al frame principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos de Grabación
        grabarBtn.addActionListener(e -> {
            grabarBtn.setEnabled(false);
            pararBtn.setEnabled(true);
            statusLabel.setText("Grabando...");
            audioRecorder.startRecording(new File("grabacion_" + System.currentTimeMillis() + ".wav"));
        });

        pararBtn.addActionListener(e -> {
            grabarBtn.setEnabled(true);
            pararBtn.setEnabled(false);
            statusLabel.setText("Grabación finalizada");
            File recordedFile = audioRecorder.stopRecording();
            AppContextSingleton.get().setUltimoArchivoAudio(recordedFile);
            salidaMensajesArea.setText("Grabación finalizada. Archivo: " + recordedFile.getAbsolutePath());
        });
    }

    private JPanel createOutputPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Reproducir audio", SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        playBtn = new JButton("Play");
        pauseBtn = new JButton("Pause");
        resumeBtn = new JButton("Resume");
        stopBtn = new JButton("Stop");

        playBtn.addActionListener(e -> playAudio());
        pauseBtn.addActionListener(e -> pauseAudio());
        resumeBtn.addActionListener(e -> resumeAudio());
        stopBtn.addActionListener(e -> stopAudio());

        buttonPanel.add(playBtn);
        buttonPanel.add(pauseBtn);
        buttonPanel.add(resumeBtn);
        buttonPanel.add(stopBtn);

        JScrollPane scrollPane = new JScrollPane(reproducirAudioArea);
        reproducirAudioArea.setEditable(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return panel;
    }

    private void playAudio() {
        File audioFile = AppContextSingleton.get().getUltimoArchivoAudio();
        if (audioFile == null || !audioFile.exists()) {
            reproducirAudioArea.setText("No hay archivo de audio para reproducir.");
            return;
        }

        try {
            if (audioClip != null) {
                audioClip.stop();
                audioClip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            reproducirAudioArea.setText("Reproduciendo: " + audioFile.getName());

        } catch (Exception ex) {
            reproducirAudioArea.setText("Error al reproducir el archivo de audio: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void pauseAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            reproducirAudioArea.append("\nAudio en pausa.");
        }
    }

    private void resumeAudio() {
        if (audioClip != null && !audioClip.isRunning()) {
            audioClip.start();
            reproducirAudioArea.append("\nReproducción reanudada.");
        }
    }

    private void stopAudio() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            reproducirAudioArea.append("\nReproducción detenida.");
        }
    }

    private void ejecutarFiltro(PluginFiltro plugin) {
        AppContext contexto = AppContextSingleton.get();
        FiltroExecutor executor = new FiltroExecutor();

        // Check if the plugin is the "Reproducir archivo de audio" plugin
        if ("Reproducir archivo de audio".equals(plugin.getNombre())) {
            File audioFile = contexto.getUltimoArchivoAudio();
            if (audioFile == null || !audioFile.exists()) {
                reproducirAudioArea.setText("No hay archivo de audio en el contexto.");
            } else {
                // Simply update the text area, do not execute the plugin's 'ejecutar' method
                reproducirAudioArea.setText("Listo para reproducir el archivo: " + audioFile.getName());
            }
            return; // Exit the method here
        }

        // For all other plugins, execute them as normal
        String resultado = executor.ejecutarFiltro(plugin, contexto);

        switch (plugin.getNombre()) {
            case "Persistir archivo en base de datos":
                salidaMensajesArea.setText(resultado);
                break;
            case "Listar archivos guardados en base de datos":
                listadoArchivosArea.setText(resultado);
                break;
            case "Obtener texto del archivo de audio":
                textoAudioArea.setText(resultado);
                break;
            case "Leer archivo de texto - Convertir texto a archivo de audio":
                salidaMensajesArea.setText("Ruta del audio generado: " + resultado);
                reproducirAudioArea.setText("Listo para reproducir el audio generado del texto.");
                break;
            case "Buscar cadena de texto en archivo de texto":
                cadenaEncontradaArea.setText(resultado);
                break;
            default:
                salidaMensajesArea.setText("Resultado del plugin " + plugin.getNombre() + ":\n" + resultado);
                break;
        }
    }
}