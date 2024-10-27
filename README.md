# VinilosApp

VinilosApp es una aplicación de Android desarrollada en Kotlin y Java que permite a los usuarios explorar una lista de álbumes y ver los detalles de cada uno. La aplicación utiliza Gradle como sistema de construcción.

## Características

- Lista de álbumes
- Detalles del álbum
- Navegación entre fragmentos

## Tecnologías

- **Lenguajes**: Kotlin, Java
- **Frameworks**: Android Jetpack (Navigation, LiveData, ViewModel)
- **Sistema de construcción**: Gradle

## Estructura del Proyecto

- `app/src/main/java/com/miso/vinilosapp/`: Contiene el código fuente de la aplicación.
- `app/src/main/res/layout/`: Contiene los archivos de diseño XML.
- `app/src/main/res/navigation/`: Contiene los gráficos de navegación.
- `app/src/main/res/values/`: Contiene los recursos de valores como strings, colors, etc.

## Instalación

1. Clona el repositorio:
    ```sh
    git clone https://github.com/romelapj/vinilosapp.git
    ```
2. Abre el proyecto en Android Studio.
3. Sincroniza el proyecto con Gradle.
4. Ejecuta la aplicación en un emulador o dispositivo físico.

## Uso

1. Al abrir la aplicación, se muestra una lista de álbumes.
2. Al seleccionar un álbum, se navega a la pantalla de detalles del álbum.

## Contribución

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza los cambios necesarios y haz commit (`git commit -am 'Añadir nueva funcionalidad'`).
4. Sube los cambios a tu rama (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.


```mermaid

classDiagram
    class AlbumFragment {
        +onCreateView()
        +onViewCreated()
    }

    class AlbumDetailFragment {
        +onCreateView()
        +onViewCreated()
    }

    class NavGraph {
        +startDestination: String
    }

    AlbumFragment --> NavGraph : "Navigates to"
    AlbumDetailFragment --> NavGraph : "Navigates to"
