public class SevenCardStud extends ManoPoker {

    @Override
    public void repartirCartas() {
        for (Jugador j : jugadores) {
            // 2 down cards
            j.recibirCarta(mazo.darCarta());
            j.recibirCarta(mazo.darCarta());
            // 1 up card
            j.recibirCarta(mazo.darCarta());
        }

        // 3 up cards
        for (int ronda = 0; ronda < 3; ronda++) {
            for (Jugador j : jugadores) {
                j.recibirCarta(mazo.darCarta());
            }
        }

        // última carta oculta
        for (Jugador j : jugadores) {
            j.recibirCarta(mazo.darCarta());
        }
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

            int puntaje = EvaluadorManos.evaluar(j.getMano());
            System.out.println("Puntaje: " + puntaje + " (" + EvaluadorManos.descripcionJugada(puntaje) + ")");

            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorJugador = j;
            }
        }

        System.out.println("El ganador es: " + mejorJugador.getNombre());
    }
}
