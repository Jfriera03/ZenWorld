# ZenWord - Juego de Palabras en Android

## Colaboradores
- [Jfriera03](https://github.com/Jfriera03)
- [Brouse13](https://github.com/Brouse13)

## Introducción
ZenWord es un juego interactivo para Android que consiste en descubrir palabras ocultas a partir de un conjunto limitado de letras organizadas en forma circular. El juego está diseñado para mejorar la agilidad mental y el vocabulario en catalán.

## Características Principales
- **Palabras Aleatorias**: Se generan letras aleatorias basadas en palabras reales del catalán.
- **Palabras Ocultas**: El jugador debe descubrir hasta 5 palabras ocultas con longitud variable (entre 3 y 7 caracteres).
- **Sistema de Bonus**: Encontrar palabras adicionales proporciona bonus que ayudan a revelar letras iniciales de palabras aún no descubiertas.

## Descripción Técnica y Funcionalidades

### Interfaz de Usuario
- Uso de **ConstraintLayout** con `GuideLines` para posicionar dinámicamente elementos en la pantalla.
- **ImageView** para la representación gráfica del círculo de letras.
- Botones con imágenes personalizadas utilizando el atributo `android:background`.
- Uso del atributo `visibility` para mostrar u ocultar elementos según el estado del juego.
- Fondo Zen con imágenes que cubren completamente la pantalla.

### Estructuras de Datos
- Implementación eficiente utilizando **conjuntos (Set)** y **mappings (Map)** para almacenar datos como:
  - Catálogo de palabras válidas (con y sin acentos).
  - Palabras agrupadas por longitud.
  - Palabras ocultas y su posición.
  - Palabras descubiertas.
  - Letras disponibles y número de apariciones para validaciones rápidas.

### Funciones Principales Implementadas
- **Botón Clear**: Limpia la palabra actual e inicialmente activa nuevamente todas las letras disponibles.
- **Botón Send**: Valida palabras introducidas:
  - Si es una palabra oculta, se revela en pantalla.
  - Si es válida pero no oculta, incrementa los bonus.
  - Si es repetida, se muestra en rojo mediante formato HTML (`Html.fromHtml()`).
  - Si es inválida, muestra un mensaje emergente con un `Toast`.

- **Botón Random**: Reordena las letras usando el algoritmo Fisher-Yates para facilitar la creación de nuevas palabras.
- **Botón Bonus**: Muestra un cuadro de diálogo (`AlertDialog`) con las palabras descubiertas y bonus obtenidos.
- **Botón Ayuda**: Revela la primera letra de una palabra oculta cada 5 bonus acumulados.
- **Botón Reiniciar**: Reinicia la partida, generando nuevas palabras y colores aleatorios.

### Manejo Dinámico de Vistas
- Creación dinámica de filas (`TextView`) para palabras ocultas mediante restricciones (`ConstraintSet`) y generación automática de identificadores (`generateViewId()`).
- Restricciones dinámicas para adaptarse a distintos tamaños de pantalla.

### Gestión de Recursos
- Lectura eficiente del archivo externo `paraules.dic`, ubicado en `res/raw/`, utilizando flujos (`InputStream` y `BufferedReader`).

### Validación de Palabras
- Método optimizado (`esParaulaSolucio`) que verifica si una palabra introducida es válida en función del conjunto de letras disponibles.

### Notificaciones y Mensajes
- Uso de `Toast` para mensajes breves.
- `AlertDialog` para información detallada del progreso del usuario y bonus acumulados.

### Uso de HTML en TextView
- Empleo del formato HTML en textos para destacar palabras repetidas en rojo.

### Control del Estado del Juego
- Funciones específicas (`enableViews` y `disableViews`) para habilitar o deshabilitar elementos en pantalla según el progreso del juego.

## Tecnología Utilizada
- Desarrollo en **Android Studio**.
- Lenguaje de programación: **Java**.
- Manejo avanzado de vistas y layouts mediante programación directa en Java.
- Uso avanzado del sistema de restricciones para interfaces flexibles y adaptables.

---

Este proyecto está diseñado con propósitos educativos y como ejemplo práctico del uso eficiente de estructuras de datos y programación interactiva en aplicaciones Android.
