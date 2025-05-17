import java.util.ArrayList;

public abstract class JuegoPoker {
    protected ArrayList<Jugador> jugadores;
    protected Mazo mazo;

    public JuegoPoker(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.mazo = new Mazo();
        mazo.revolverMazo();
    }

    public abstract void repartirCartas();
    public abstract void realizarRondasApuesta();
    public abstract Jugador determinarGanador();

    public void jugar() {
        repartirCartas();
        realizarRondasApuesta();
        Jugador ganador = determinarGanador();
        System.out.println("Ganador: " + ganador.getNombre());
    }
}
