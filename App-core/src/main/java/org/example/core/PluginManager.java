package org.example.core;

import org.example.interfaces.PluginFiltro;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginManager {

    public List<PluginFiltro> cargarPlugins() {
        List<PluginFiltro> plugins = new ArrayList<>();
        try {
            // Carpeta Plugins
            File pluginsDir = new File("Plugins");
            if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
                System.out.println("No se encontró la carpeta Plugins.");
                return plugins;
            }

            // Juntar todos los JARs
            File[] jars = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jars == null || jars.length == 0) {
                System.out.println("No hay plugins disponibles en /Plugins.");
                return plugins;
            }

            // Crear classloader con todos los jars
            URL[] urls = new URL[jars.length];
            for (int i = 0; i < jars.length; i++) {
                urls[i] = jars[i].toURI().toURL();
            }
            URLClassLoader pluginClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

            // Cargar implementaciones de PluginFiltro
            ServiceLoader<PluginFiltro> loader = ServiceLoader.load(PluginFiltro.class, pluginClassLoader);
            for (PluginFiltro filtro : loader) {
                plugins.add(filtro);
                System.out.println("Plugin cargado: " + filtro.getNombre());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return plugins;
    }
}
