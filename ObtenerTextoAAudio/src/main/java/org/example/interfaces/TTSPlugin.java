package org.example.interfaces;

public interface TTSPlugin {
    /**
     * Convierte texto a audio
     * @param texto El texto a convertir
     * @param outputPath Ruta donde guardar el archivo de audio
     * @return true si la conversión fue exitosa
     */
    boolean convertirTextoAudio(String texto, String outputPath);

    /**
     * Reproduce el audio directamente
     * @param texto El texto a convertir y reproducir
     * @return true si la reproducción fue exitosa
     */
    boolean reproducirTexto(String texto);

    /**
     * Configura parámetros del TTS
     * @param velocidad Velocidad de habla (0.5 - 2.0)
     * @param tono Tono de voz (-1.0 a 1.0)
     */
    void configurarVoz(double velocidad, double tono);

    /**
     * Obtiene las voces disponibles
     * @return Array de nombres de voces disponibles
     */
    String[] obtenerVocesDisponibles();

    /**
     * Establece la voz a usar
     * @param nombreVoz Nombre de la voz
     */
    void establecerVoz(String nombreVoz);
}
