package org.example;

import org.example.gui.VentanaPrincipal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Núcleo de la aplicación iniciado ===");

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}