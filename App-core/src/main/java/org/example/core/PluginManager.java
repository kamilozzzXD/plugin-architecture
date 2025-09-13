package org.example.core;


import org.example.interfaces.PluginFiltro;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {

    public List<PluginFiltro> cargarPlugins() {
        List<PluginFiltro> plugins = new ArrayList<>();
        ServiceLoader<PluginFiltro> loader = ServiceLoader.load(PluginFiltro.class);

        for (PluginFiltro filtro : loader) {
            plugins.add(filtro);
            System.out.println("Plugin cargado: " + filtro.getNombre());
        }

        return plugins;
    }
}

