import java.util.ArrayList;

public abstract class JuegoPoker {
    protected ArrayList<Jugador> jugadores;
    protected Mazo mazo;
    protected int turnoActual;
    protected int bote;
    protected int apuestaActual;

    public JuegoPoker(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.mazo = new Mazo();
        this.mazo.revolverMazo();
        this.turnoActual = 0;
        this.bote = 0;
        this.apuestaActual = 0;
    }

    // Reparte las cartas iniciales según la modalidad
    public abstract void repartirCartasIniciales();

    // Avanza a la siguiente fase (flop, turn, river / streets)
    public abstract void siguienteFase();

    // Lógica para mostrar cartas (opcional, según la modalidad)
    public abstract void mostrarCartas();

    // Ejecuta una ronda de apuestas (puede variar por modalidad)
    public abstract void ejecutarApuesta();

    // Determina el ganador de la mano actual
    public abstract Jugador determinarGanador();

    // Regresa el estado del juego
    public abstract String obtenerEstadoJuego();

    public abstract String obtenerGanador();

    // Obtenemos el numero de rondas totales a jugar
    public abstract int getNumeroRondasTotales();

    public abstract String realizarCheckCall(Jugador jugador);
    public abstract String realizarRaise(Jugador jugador, int cantidad);
    public abstract String realizarFold(Jugador jugador);

    // Reinicia el juego para una nueva ronda
    public void reiniciarRonda() {
        mazo = new Mazo();
        mazo.revolverMazo();
        bote = 0;
        apuestaActual = 0;
        for (Jugador jugador : jugadores) {
            jugador.limpiarMano();
            jugador.estaActivo();
        }
    }

    public void pasarTurno() {
    int totalJugadores = jugadores.size();
    for (int i = 1; i <= totalJugadores; i++) {
        int siguiente = (turnoActual + i) % totalJugadores;
        Jugador candidato = jugadores.get(siguiente);
        if (candidato.estaActivo()) {
            turnoActual = siguiente;
            return;
        }
    }
}

    // Avanza al siguiente jugador activo
    public void avanzarTurno() {
        do {
            turnoActual = (turnoActual + 1) % jugadores.size();
        } while (!jugadores.get(turnoActual).estaActivo());
    }

    // Getter del jugador actual
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    // Métodos de acceso
    public int getBote() {
        return bote;
    }

    public void agregarAlBote(int cantidad) {
        bote += cantidad;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    

}