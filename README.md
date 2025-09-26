# 🔌 Plugin Architecture Java Project

¡Bienvenido/a! 🚀 Este proyecto demuestra una arquitectura **Plug and Play** moderna en Java, permitiendo la integración sencilla de nuevos módulos y funcionalidades sin tocar el núcleo del sistema.

## 🧑‍💻 Características Principales

- **Lenguaje principal:** Java ☕
- **Arquitectura:** Plug and Play (enchufa y usa)
- **Extensible:** Añade plugins fácilmente para ampliar capacidades
- **Modularidad:** Separación clara entre núcleo y extensiones

## 🗣️ Integración de Reconocimiento de Voz con Vosk

Este proyecto permite incorporar reconocimiento de voz usando [Vosk](https://alphacephei.com/vosk/).  
> **Nota importante:**  
> Para usar Vosk en español, **debes descargar el modelo correspondiente** desde la siguiente página:  
> 👉 [Modelos Vosk en español](https://alphacephei.com/vosk/models)

Coloca el modelo descargado en la carpeta original del modelo o en la siguiente ruta dentro del proyecto:
- `vosk-model-es-0.42`

Asegúrate de que el modelo esté accesible antes de ejecutar el reconocimiento de voz.

## 📦 Instalación

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/kamilozzzXD/plugin-architecture.git
   ```
2. **Descarga el modelo Vosk en español** y colócalo en la carpeta original del modelo o en `vosk-model-es-0.42`.

3. **Compila el proyecto:**
   ```bash
   ./gradlew build
   ```
   _O usa tu herramienta Java favorita._

## 🧩 Cómo agregar nuevos plugins

1. Crea una clase que implemente la interfaz de plugin proporcionada.
2. Coloca tu clase en la carpeta de plugins.
3. El sistema la detectará automáticamente al iniciar.

## 📚 Ejemplo de uso

```java
PluginManager manager = new PluginManager();
manager.loadPlugins();
```

## 🛠️ Requisitos

- Java 8 o superior
- Modelo Vosk en español
- Maven

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Puedes crear nuevos plugins para ampliar la funcionalidad.  
Por favor, abre un **Pull Request** o **Issue** para sugerencias, problemas, o mejoras.

## 👨‍💻 Autores

- Camilo Herrera
- Javic Rojas



---

<div align="center">
  Hecho con ❤️ por la comunidad.
</div>
