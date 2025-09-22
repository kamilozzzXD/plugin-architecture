package org.example.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AppContext: objeto compartido por el núcleo y los plugins.
 * Guarda referencias al último archivo, lista de audios, resultados, y servicios compartidos.
 */
public class AppContext {

    private volatile File ultimoArchivo;          // archivo "seleccionado" o producido
    private volatile File ultimoArchivoAudio;     // si aplica
    private volatile File ultimoArchivoTexto;     // si aplica
    private volatile String ultimoResultado;      // texto resultado (p. ej. transcripción)
    private final List<File> listaAudios = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Object> servicios = new ConcurrentHashMap<>();

    // getters / setters

    public File getUltimoArchivo() {
        return ultimoArchivo;
    }

    public synchronized void setUltimoArchivo(File ultimoArchivo) {
        this.ultimoArchivo = ultimoArchivo;
    }

    public File getUltimoArchivoAudio() {
        return ultimoArchivoAudio;
    }

    public synchronized void setUltimoArchivoAudio(File ultimoArchivoAudio) {
        this.ultimoArchivoAudio = ultimoArchivoAudio;
        if (ultimoArchivoAudio != null) {
            this.ultimoArchivo = ultimoArchivoAudio;
            addAudio(ultimoArchivoAudio);
        }
    }

    public File getUltimoArchivoTexto() {
        return ultimoArchivoTexto;
    }

    public synchronized void setUltimoArchivoTexto(File ultimoArchivoTexto) {
        this.ultimoArchivoTexto = ultimoArchivoTexto;
        if (ultimoArchivoTexto != null) {
            this.ultimoArchivo = ultimoArchivoTexto;
        }
    }

    public String getUltimoResultado() {
        return ultimoResultado;
    }

    public synchronized void setUltimoResultado(String ultimoResultado) {
        this.ultimoResultado = ultimoResultado;
    }

    public List<File> getListaAudios() {
        synchronized (listaAudios) {
            return new ArrayList<>(listaAudios);
        }
    }

    public void addAudio(File audio) {
        if (audio != null) {
            listaAudios.add(audio);
        }
    }

    public void putServicio(String clave, Object servicio) {
        servicios.put(clave, servicio);
    }

    public Object getServicio(String clave) {
        return servicios.get(clave);
    }
}
