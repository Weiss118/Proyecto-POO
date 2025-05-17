import java.util.ArrayList;

public class TexasHoldem extends ManoPoker {
    private ArrayList<Carta> cartasComunitarias;

    public TexasHoldem() {
        super();
        cartasComunitarias = new ArrayList<>();
    }

    @Override
    public void repartirCartas() {
        for (Jugador j : jugadores) {
            j.recibirCarta(mazo.darCarta());
            j.recibirCarta(mazo.darCarta());
        }

        // Quemar y repartir el flop
        mazo.darCarta(); // quemar
        for (int i = 0; i < 3; i++) {
            cartasComunitarias.add(mazo.darCarta());
        }

        // Turn
        mazo.darCarta(); // quemar
        cartasComunitarias.add(mazo.darCarta());

        // River
        mazo.darCarta(); // quemar
        cartasComunitarias.add(mazo.darCarta());
    }

    @Override
    public void realizarApuesta() {
        long apuestaFija = 100;
        for (Jugador j : jugadores) {
            j.apostar(apuestaFija);
        }
        System.out.println("Cada jugador ha apostado 100 fichas.");
    }

    @Override
    public void mostrarGanador() {
        Jugador mejorJugador = null;
        int mejorPuntaje = -1;

        for (Jugador j : jugadores) {
            System.out.print(j.getNombre() + " tiene: ");
            j.mostrarMano();

            ArrayList<Carta> totalCartas = new ArrayList<>(j.getMano());
            totalCartas.addAll(cartasComunitarias);

            int puntaje = EvaluadorManos.evaluar(totalCartas);
            System.out.println("Puntaje: " + puntaje + " (" + EvaluadorManos.descripcionJugada(puntaje) + ")");

            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorJugador = j;
            }
        }

        System.out.println("Cartas comunitarias: " + cartasComunitarias);
        System.out.println("El ganador es: " + mejorJugador.getNombre());
    }
}
