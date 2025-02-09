# Russian Roulette
- Aplicación hecha con mucho !amor:)

## Clases Principales
---
### RoundManager
Clase encargada de gestionar las rondas del juego, los turnos y el flujo general del mismo.

- **Atributos**:
  - `round`: Número de la ronda actual.
  - `isRoundStarted`: Indica si una ronda está en curso.
  - `currentPlayer`: Índice del jugador actual.
  - `playerCount`: Cantidad de jugadores en la partida.
  - `deadPlayer`: Nombre del jugador que ha muerto, si aplica.
  - `gameMessenger`: Instancia de `GameMessenger` para manejar mensajes hacia los jugadores.
  - `players`: Lista de jugadores en la partida.
  - `isGameOver`: Indica si el juego ha terminado.
  - `playerShooted`: Nombre del jugador objetivo del disparo.

- **Métodos**:
  - **Gestión de la ronda**:
    - `roundStart(Shotgun shootgun)`: Prepara, anuncia y comienza una nueva ronda.
    - `roundFinish()`: Finaliza la ronda actual y verifica si algún jugador ha muerto.
    - `prepareRound(Shotgun shootgun)`: Prepara la ronda inicializando los parámetros y llenando la escopeta.
    - `announceRound(Shotgun shootgun)`: Envía mensajes sobre el estado actual de la ronda, incluyendo balas y vidas de los jugadores.
    - `startTurn()`: Selecciona un jugador inicial de manera aleatoria y anuncia su turno.

  - **Turnos y flujo del juego**:
    - `generateRandomStarterPlayerTurn()`: Selecciona de forma aleatoria al jugador que inicia el turno.
    - `calcualteNextPlayer()`: Calcula el índice del siguiente jugador en turno.
    - `messageOptions()`: Envía al jugador actual las opciones de acción disponibles.

  - **Anuncios y mensajes**:
    - `announcePlayersCurrentLives()`: Anuncia las vidas actuales de los jugadores.
    - `announceCurrentBullets(Shotgun shootgun)`: Anuncia el estado actual del cargador de la escopeta.
    - `announceDeathPlayer()`: Anuncia a los jugadores que un participante ha muerto.
    - `announceWinner()`: Anuncia al ganador del juego cuando este ha terminado.

  - **Control de estado**:
    - `checkIfPlayerIsDeath()`: Verifica si algún jugador ha muerto y actualiza el estado del juego.
    - `isRoundStarted()`: Retorna si la ronda actual está en curso.
    - `isGameOver()`: Retorna si el juego ha finalizado.
---

### PlayerApp
Clase principal del cliente que conecta a los jugadores al servidor.

- **Flujo principal**:
  - Solicita el nombre del jugador al inicio.
  - Se conecta al servidor utilizando el puerto definido en `Constants`.
  - Inicia un hilo de escucha (`ServerListener`) para recibir mensajes del servidor.
  - Envía comandos escritos por el jugador al servidor.

---

### Player
Clase que representa a un jugador.

- **Atributos**:
  - `name`: Nombre del jugador.
  - `socket`: Conexión del jugador al servidor.
  - `input`: Flujo de datos para recibir mensajes del servidor.
  - `message`: Flujo de datos para enviar mensajes al servidor.
  - `isAlive`: Indica si el jugador está vivo.
  - `lives`: Número de vidas del jugador.

- **Métodos**:
  - `getShot()`: Reduce una vida al jugador y lo marca como muerto si llega a 0 vidas.

---

### PlayerActionHandler
Clase encargada de manejar las acciones de los jugadores durante el juego.

- **Atributos**:
  - `roundManager`: Instancia de `RoundManager` que controla el flujo del juego.

- **Responsabilidades**:
  - Valida las entradas del jugador (opciones válidas: `"player"` y `"myself"`).
  - Gestiona la acción de disparar con la escopeta (`Shotgun`).
  - Envía mensajes a los jugadores sobre las acciones realizadas.

---

### Shotgun
Clase que representa la escopeta del juego.

- **Atributos**:
  - `shootgunFakeBullets`: Número de balas falsas.
  - `shootgunRealBullets`: Número de balas reales.
  - `shootgunBullets`: Lista de balas actuales en el cargador.
  - `MAX_SLOTS`: Capacidad máxima del cargador (8 balas).

- **Métodos**:
  - `generateBullets()`: Genera un número aleatorio de balas reales y falsas.
  - `fillShootgun()`: Llena el cargador con balas reales, falsas y vacías, y las mezcla aleatoriamente.
  - `calculateCurrentRealBullets()`: Reduce el contador de balas reales.
  - `calculateCurrentFakeBullets()`: Reduce el contador de balas falsas.
  - `isShotgunEmpty()`: Verifica si el cargador está vacío.

---

### GameMessenger
Clase responsable de manejar y enviar mensajes a los jugadores sobre el estado del juego y las acciones que deben realizar.

- **Atributos**:
  - `roundManager`: Instancia de `RoundManager` para interactuar con el estado de la ronda.

- **Métodos**:
  - `showTimeToStartARound(int miliseconds, RoundManager roundManager)`: Muestra un mensaje de cuenta regresiva antes de comenzar una ronda.
  - `rulesExplication()`: Explica las reglas del juego a los jugadores.
  - `finishRoundMessage(ArrayList<Player> players)`: Informa a los jugadores que la ronda ha terminado y los prepara para la próxima.
  - `starterPlayerMessage()`: Anuncia al jugador inicial de la ronda.
  - `playerNextTurnMessage()`: Indica a los jugadores quién tiene el turno actual.
  - `showActualRoundMessage()`: Muestra el número de la ronda actual a los jugadores.
---

### Rules
Clase que contiene las reglas del juego en forma de constantes de texto.

- **Atributos**:
  - `RULES`: Array de `String` que almacena todas las reglas del juego.

- **Métodos**:
  - `getRule(int ruleNumber)`: Retorna una regla específica en base a su índice.

- Las reglas definidas en `RULES` son:
  - Solo habrá una escopeta para todos los jugadores.
  - En cada ronda, la escopeta tendrá una cantidad aleatoria de balas falsas o reales.
  - La escopeta no siempre estará llena; puede contener entre 2 y 10 balas.
  - El primer turno de cada ronda se asigna aleatoriamente a un jugador.
  - Si un jugador dispara a otro (con bala real o falsa), el turno termina.
  - Si un jugador se dispara con una bala real, el turno termina.
  - Si un jugador se dispara con una bala falsa, puede volver a disparar.
  - La ronda termina cuando la escopeta se vacía o un jugador muere.
  - Cada jugador tiene 3 vidas.
  - El juego termina cuando un jugador muere.

---

### GameHandler
Clase encargada de manejar el flujo del juego en un hilo separado.

- **Atributos**:
  - `shootgun`: Instancia de `Shotgun` para manejar la mecánica de disparo.
  - `roundManager`: Instancia de `RoundManager` para controlar las rondas.
  - `playerActionHandler`: Instancia de `PlayerActionHandler` para gestionar las acciones de los jugadores.
  - `gameMessenger`: Instancia de `GameMessenger` para enviar mensajes a los jugadores.

- **Métodos**:
  - `run()`: Método principal del hilo que ejecuta el flujo del juego:
    - Explica las reglas a los jugadores.
    - Inicia el temporizador de inicio.
    - Mientras el juego no termine:
       - Inicia y gestiona cada ronda.
       - Procesa las acciones de los jugadores.
       - Muestra mensajes de estado (vidas, balas restantes).
       - Alterna turnos hasta que se vacíe la escopeta o alguien muera.
    - Si el juego termina, anuncia al jugador muerto y al ganador.

---


### ServerListener
Hilo dedicado a escuchar mensajes del servidor en tiempo real.

- **Atributos**:
  - `inputStream`: Flujo de datos para recibir mensajes del servidor.

- **Métodos**:
  - `run()`: Escucha mensajes del servidor y los imprime en la consola del cliente. Si se pierde la conexión, muestra un mensaje de error.

---

### ConsoleHandler
Clase auxiliar para manejar interacciones de la consola.

- **Métodos**:
  - `waitConsoleTime(int milliseconds)`: Pausa la ejecución por el número de milisegundos especificado.
  - `clearConsole(ArrayList<Player> players)`: Envía comandos para limpiar la consola de cada jugador conectado.

---

### ConsoleColors
Clase para personalizar el color y formato de los mensajes en consola.

- **Atributos**:
  - Colores predefinidos como `ANSI_RED`, `ANSI_BLUE`, `ANSI_YELLOW`, entre otros.
  - `BOLD`: Aplica negritas a un mensaje.

- **Métodos**:
  - `changeColor(String message, String color)`: Aplica un color al texto.
  - `changeBoldColor(String message, String color)`: Aplica negritas y un color al texto.

---

### Constants
Clase que contiene constantes del proyecto.

- **SERVER_PORT**: Puerto del servidor utilizado por la aplicación.

---
