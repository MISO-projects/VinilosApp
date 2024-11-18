# VinilosApp

VinilosApp es una aplicación de Android para gestionar álbumes de música. La aplicación permite a los usuarios ver una lista de álbumes, ver detalles de un álbum específico y navegar entre diferentes fragmentos.

## Características

- Ver una lista de álbumes
- Ver el detalle de un álbum
- Ver una lista de artistas
- Ver el detalle de un artista
- Navegación entre fragmentos

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- `com.miso.vinilosapp`: Contiene la `MainActivity` y otros componentes principales.
- `com.miso.vinilosapp.ui`: Contiene los fragmentos de la interfaz de usuario como `AlbumFragment`, `AlbumDetailFragment` y `HomeFragment`.
- `com.miso.vinilosapp.models`: Contiene las clases de modelo como `Album`.
- `com.miso.vinilosapp.network`: Contiene las clases relacionadas con la red como `ApiService` y `NetworkServiceAdapter`.
- `com.miso.vinilosapp.network.repositories`: Contiene los repositorios como `AlbumRepository`.

## Instalación

1. Clona el repositorio:
    ```sh
    git clone https://github.com/romelapj/vinilosapp.git
    ```
2. Abre el proyecto en Android Studio.
3. Sincroniza el proyecto con Gradle.

## Uso

1. Ejecuta la aplicación en un emulador o dispositivo Android.
2. Navega por la lista de álbumes y selecciona uno para ver los detalles.

## Pruebas

El proyecto incluye pruebas unitarias para verificar la funcionalidad de los componentes principales. Para ejecutar las pruebas:

1. Abre el proyecto en Android Studio.
2. Navega a la ventana de pruebas.
3. Ejecuta las pruebas.

## Contribuciones

Las contribuciones son bienvenidas. Por favor, sigue los siguientes pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-caracteristica`).
3. Realiza los cambios necesarios y haz commit (`git commit -am 'Añadir nueva característica'`).
4. Sube los cambios a tu rama (`git push origin feature/nueva-caracteristica`).
5. Abre un Pull Request.
