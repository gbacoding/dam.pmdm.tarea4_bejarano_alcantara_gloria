# Spyro the Dragon - Guía de Personajes y Mundos 🐉✨

## Introducción
Esta aplicación es una enciclopedia interactiva para fans de la saga Spyro the Dragon. 
Su propósito es ofrecer una guía visual de los personajes y mundos del juego, incorporando elementos multimedia y secretos ocultos ("Easter Eggs") 
para mejorar la experiencia del usuario.

## Características principales
- **Guía de Bienvenida:** Un tutorial interactivo que utiliza vídeos y bocadillos de texto animados para enseñar a usar la app.
- **Lista de Personajes:** Un catálogo detallado que utiliza un `RecyclerView` para mostrar a los héroes y villanos.
- **Sección de Mundos:** Información sobre los niveles del juego con un secreto desbloqueable.
- **Easter Eggs:**
  - **Magia de Ripto:** Al realizar una pulsación larga sobre el villano Ripto, su cetro lanza un hechizo sobre el cetro.
  - **Vídeo Secreto:** Al pulsar tres veces rápidamente en la pestaña de mundos, se activa un vídeo especial.

## Tecnologías utilizadas
- **Kotlin:** Lenguaje principal de desarrollo.
- **Navigation Component:** Para gestionar el flujo entre fragmentos.
- **Canvas API:** Utilizada para dibujar el efecto de magia de Ripto en tiempo real.
- **Multimedia:** Uso de `MediaPlayer`, `VideoView` y `SurfaceView` para sonidos y vídeos.
- **Animaciones:** Interpoladores (Bounce, Overshoot) y `AnimationDrawable`.

## Instrucciones de uso
1. **Clonar el repositorio:** `git clone https://github.com/tu_usuario/tu_repositorio.git`
2. **Abrir en Android Studio:** Importar el proyecto y sincronizar con Gradle.
3. **Ejecutar:** Compilar en un dispositivo con API 24 o superior.

## Conclusiones del desarrollador
El desarrollo de este proyecto ha permitido integrar conceptos avanzados de la interfaz de usuario de Android. 
El mayor desafío fueros los Easter Eggs:
Conseguir que el video se mostrara y no interfiriera el RecyclerView.
la gestión de la pulsacion prolongada (`GestureDetector`) para que no interfirieran con el scroll de la lista de personajes. 
Además, el uso de la API de Canvas para crear efectos visuales dinámicos ha sido un aprendizaje clave para mejorar la interactividad de la aplicación.
En concreto he tenido dificultades para colocarlo espacialmente sobre el cetro.

