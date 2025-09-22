package org.example.interfaces;

import org.example.core.AppContext;

/**
 * Contrato de plugins: ahora recibe AppContext para mejor interoperabilidad.
 */
public interface PluginFiltro {
    String getNombre();
    String getDescripcion();
    boolean soportaTipo(String tipoArchivo);
    String ejecutar(AppContext contexto);
}
