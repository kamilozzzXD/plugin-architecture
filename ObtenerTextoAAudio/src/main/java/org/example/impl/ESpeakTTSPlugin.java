package org.example.impl;

import org.example.interfaces.TTSPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ESpeakTTSPlugin implements TTSPlugin {

    private double velocidad = 1.0;
    private double tono = 0.0;
    private String voz = "es"; // Voz en español por defecto
    private String rutaEspeak = "eSpeak/command_line/espeak.exe"; // Ajustar según instalación

    public ESpeakTTSPlugin() {
        verificarESpeakInstalado();
    }

    private void verificarESpeakInstalado() {
        try {
            ProcessBuilder pb = new ProcessBuilder(rutaEspeak, "--version");
            Process proceso = pb.start();
            int resultado = proceso.waitFor();
            if (resultado != 0) {
                throw new RuntimeException("eSpeak no está instalado o no se encuentra en PATH");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verificando eSpeak: " + e.getMessage());
        }
    }

    @Override
    public boolean convertirTextoAudio(String texto, String outputPath) {
        try {
            List<String> comando = new ArrayList<>();
            comando.add(rutaEspeak);
            comando.add("--path=eSpeak");
            comando.add("-v");
            comando.add(voz); // Utilizar la voz establecida
            comando.add("-s");
            comando.add(String.valueOf((int) (velocidad * 175)));
            comando.add("-p");
            comando.add(String.valueOf((int) (tono * 50 + 50)));
            comando.add("-w"); // Flag para escribir la salida en un archivo
            comando.add(outputPath); // Ruta del archivo de salida
            comando.add(texto); // El texto a convertir

            ProcessBuilder pb = new ProcessBuilder(comando);
            Process proceso = pb.start();
            int resultado = proceso.waitFor();

            return resultado == 0;
        } catch (Exception e) {
            System.err.println("Error convirtiendo texto a audio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reproducirTexto(String texto) {
        try {
            List<String> comando = new ArrayList<>();
            comando.add(rutaEspeak);
            comando.add("--path=eSpeak"); // Corrección del path
            comando.add("-v");
            comando.add(voz);
            comando.add("-s");
            comando.add(String.valueOf((int) (velocidad * 175)));
            comando.add("-p");
            comando.add(String.valueOf((int) (tono * 50 + 50)));
            comando.add("--stdin"); // 🔑 leer desde entrada estándar

            ProcessBuilder pb = new ProcessBuilder(comando);
            Process proceso = pb.start();

            // escribir el texto en la entrada del proceso
            try (var writer = new java.io.OutputStreamWriter(proceso.getOutputStream())) {
                writer.write(texto);
                writer.flush();
            }

            int resultado = proceso.waitFor();
            return resultado == 0;
        } catch (Exception e) {
            System.err.println("Error reproduciendo texto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void configurarVoz(double velocidad, double tono) {
        this.velocidad = Math.max(0.5, Math.min(2.0, velocidad));
        this.tono = Math.max(-1.0, Math.min(1.0, tono));
    }

    @Override
    public String[] obtenerVocesDisponibles() {
        try {
            ProcessBuilder pb = new ProcessBuilder(rutaEspeak, "--voices");
            Process proceso = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            List<String> voces = new ArrayList<>();
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (!linea.startsWith("Pty") && linea.trim().length() > 0) {
                    String[] partes = linea.trim().split("\\s+");
                    if (partes.length > 1) {
                        voces.add(partes[1]); // Código de voz
                    }
                }
            }

            return voces.toArray(new String[0]);
        } catch (Exception e) {
            return new String[]{"es", "en", "fr", "de"}; // Voces por defecto
        }
    }

    @Override
    public void establecerVoz(String nombreVoz) {
        this.voz = nombreVoz;
    }
}