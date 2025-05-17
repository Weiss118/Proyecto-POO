import java.util.ArrayList;

public abstract class ManoPoker implements ReglasJuego {
    protected ArrayList<Jugador> jugadores;
    protected Mazo mazo;

    public ManoPoker() {
        jugadores = new ArrayList<>();
        mazo = new Mazo();
        mazo.revolverMazo();
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

    public abstract void repartirCartas();
    public abstract void realizarApuesta();
    public abstract void mostrarGanador();
}
