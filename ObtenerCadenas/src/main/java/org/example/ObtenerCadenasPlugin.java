package org.example;

import com.sun.source.util.Plugin;
import org.example.core.AppContext;
import org.example.interfaces.PluginFiltro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ObtenerCadenasPlugin implements PluginFiltro {

    @Override
    public String getNombre() {
        return "Buscar cadena de texto en archivo de texto";
    }

    @Override
    public String getDescripcion() {
        return "Busca y extrae cadenas de texto de un archivo utilizando una cadena de texto proporcionada por el usuario.";
    }

    @Override
    public boolean soportaTipo(String tipoArchivo) {
        return tipoArchivo.equalsIgnoreCase("texto");
    }

    @Override
    public String ejecutar(AppContext contexto) {
        File archivo = contexto.getUltimoArchivoTexto();
        if (archivo == null || !archivo.exists()) {
            return "Error: No se ha seleccionado un archivo de texto.";
        }

        String cadenaBusqueda = (String) contexto.getServicio("cadenaBusqueda");
        if (cadenaBusqueda == null || cadenaBusqueda.trim().isEmpty()) {
            return "Error: No se ha introducido ninguna cadena de texto para buscar.";
        }

        StringBuilder resultado = new StringBuilder("Cadenas de texto encontradas:\n");
        int contador = 0;
        int numeroLinea = 0;

        try (Stream<String> lineas = Files.lines(Path.of(archivo.getAbsolutePath()))) {
            Pattern pattern = Pattern.compile(Pattern.quote(cadenaBusqueda));

            for (String linea : lineas.toList()) {
                numeroLinea++;
                Matcher matcher = pattern.matcher(linea);

                while (matcher.find()) {
                    resultado.append(String.format("Línea %d: %s\n", numeroLinea, matcher.group()));
                    contador++;
                }
            }

        } catch (IOException e) {
            return "Error al leer el archivo de texto: " + e.getMessage();
        }

        if (contador == 0) {
            return "No se encontraron coincidencias en el archivo.";
        }

        return resultado.toString();
    }
}