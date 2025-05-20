import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class TexasHoldEm extends JuegoPoker {

    private ArrayList<Carta> cartasComunitarias;
    private Mazo mazo;
    private int fase;
    private int bote;
    private Map<Jugador, Integer> apuestas;
    private final int smallBlind = 10;
    private final int bigBlind = 20;

    public TexasHoldEm(ArrayList<Jugador> jugadores) {
        super(jugadores);
        this.mazo = new Mazo();
        this.cartasComunitarias = new ArrayList<>();
        this.fase = 0;
        this.bote = 0;
        this.apuestas = new HashMap<>();
        for (Jugador j : jugadores) apuestas.put(j, 0);
    }

    @Override
    public int getNumeroRondasTotales() {
        return 4; // Preflop, Flop, Turn, River
    }

    @Override
    public String obtenerEstadoJuego() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartas comunitarias:\n");
        for (Carta c : cartasComunitarias) {
            sb.append(c).append(" ");
        }
        sb.append("\n\n");

        for (Jugador j : jugadores) {
            sb.append(j.getNombre()).append(" - Bote: ").append(j.getBote()).append(" - Mano: ");
            for (Carta c : j.getMano()) {
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String obtenerGanador() {
        // Ganador aleatorio por ahora
        Jugador ganador = jugadores.get(new Random().nextInt(jugadores.size()));
        ganador.ganarFichas(bote);
        return "Ganador: " + ganador.getNombre() + " con " + bote + " puntos.\n";
    }

    @Override
    public void repartirCartasIniciales() {
        mazo.revolverMazo();

        for (Jugador jugador : jugadores) {
            jugador.setMano(new ArrayList<>());
            jugador.getMano().add(mazo.darCarta());
            jugador.getMano().add(mazo.darCarta());
        }

        aplicarBlinds();

        fase = 0;
    }

    private void aplicarBlinds() {
        if (jugadores.size() < 2) return;

        Jugador small = jugadores.get(0);
        Jugador big = jugadores.get(1);

        long apuestaSB = Math.min(smallBlind, small.getBote());
        small.apostar(apuestaSB);
        apuestas.put(small, (int) apuestaSB);
        bote += apuestaSB;

        long apuestaBB = Math.min(bigBlind, big.getBote());
        big.apostar(apuestaBB);
        apuestas.put(big, (int) apuestaBB);
        bote += apuestaBB;

        System.out.println(small.getNombre() + " pone small blind de " + apuestaSB);
        System.out.println(big.getNombre() + " pone big blind de " + apuestaBB);
    }

    @Override
    public void siguienteFase() {
        switch (fase) {
            case 0: // Pre-Flop ya se repartió en repartirCartasIniciales()
                System.out.println("=== FASE: PRE-FLOP ===");
                ejecutarApuesta(); // Ronda de apuestas post-reparto inicial
                break;

            case 1: // Flop
                System.out.println("=== FASE: FLOP ===");
                mazo.darCarta(); // Quemar carta
                for (int i = 0; i < 3; i++) {
                    cartasComunitarias.add(mazo.darCarta());
                }
                mostrarCartas();
                ejecutarApuesta();
                break;

            case 2: // Turn
                System.out.println("=== FASE: TURN ===");
                mazo.darCarta(); // Quemar carta
                cartasComunitarias.add(mazo.darCarta());
                mostrarCartas();
                ejecutarApuesta();
                break;

            case 3: // River
                System.out.println("=== FASE: RIVER ===");
                mazo.darCarta(); // Quemar carta
                cartasComunitarias.add(mazo.darCarta());
                mostrarCartas();
                ejecutarApuesta();
                break;

            case 4: // Showdown
                System.out.println("=== FASE: SHOWDOWN ===");
                mostrarCartas();
                determinarGanador();
                break;

            default:
                System.out.println("Todas las fases han terminado.");
                return;
        }

        fase++;
    }

    @Override
    public void mostrarCartas() {
        System.out.println("\nCartas comunitarias:");
        for (Carta c : cartasComunitarias) System.out.print(c + " ");
        System.out.println("\n");

        for (Jugador j : jugadores) {
            if (j.estaActivo() || j.isAllIn()) {
                System.out.print(j.getNombre() + ": ");
                j.mostrarMano();
            }
        }
    }

    @Override
    public void ejecutarApuesta() {
        rondaDeApuestas();
    }

    @Override
    public Jugador determinarGanador() {
        Map<Jugador, EvaluadorManos.ManoPoker> manosEvaluadas = new HashMap<>();
        for (Jugador j : jugadores) {
            if (!j.estaActivo() && !j.isAllIn()) continue;

            ArrayList<Carta> combinadas = new ArrayList<>(j.getMano());
            combinadas.addAll(cartasComunitarias);
            manosEvaluadas.put(j, EvaluadorManos.evaluarMejorMano(combinadas));
        }

        EvaluadorManos.ManoPoker mejorMano = null;
        ArrayList<Jugador> ganadores = new ArrayList<>();

        for (Map.Entry<Jugador, EvaluadorManos.ManoPoker> entry : manosEvaluadas.entrySet()) {
            if (mejorMano == null || entry.getValue().compareTo(mejorMano) > 0) {
                mejorMano = entry.getValue();
                ganadores.clear();
                ganadores.add(entry.getKey());
            } else if (entry.getValue().compareTo(mejorMano) == 0) {
                ganadores.add(entry.getKey());
            }
        }

        if (ganadores.size() == 1) {
            Jugador ganador = ganadores.get(0);
            ganador.ganarFichas(bote);
            System.out.println("🏆 Ganador: " + ganador.getNombre() + " gana " + bote + " fichas.");
            bote = 0;
            return ganador;
        } else {
            int porJugador = bote / ganadores.size();
            for (Jugador j : ganadores) {
                j.ganarFichas(porJugador);
                System.out.println("🤝 Empate: " + j.getNombre() + " gana " + porJugador + " fichas.");
            }
            bote = 0;
            return null;
        }
    }

    public void jugarFase(int faseActual) {
        switch (faseActual) {
            case 0:
                System.out.println("=== FASE: PRE-FLOP ===");
                ejecutarApuesta();
                break;

            case 1:
                System.out.println("=== FASE: FLOP ===");
                mazo.darCarta(); // Quemar
                for (int i = 0; i < 3; i++) cartasComunitarias.add(mazo.darCarta());
                ejecutarApuesta();
                break;

            case 2:
                System.out.println("=== FASE: TURN ===");
                mazo.darCarta(); // Quemar
                cartasComunitarias.add(mazo.darCarta());
                ejecutarApuesta();
                break;

            case 3:
                System.out.println("=== FASE: RIVER ===");
                mazo.darCarta(); // Quemar
                cartasComunitarias.add(mazo.darCarta());
                ejecutarApuesta();
                break;

            case 4:
                System.out.println("=== FASE: SHOWDOWN ===");
                mostrarCartas();
                determinarGanador();
                break;
        }
    }

    public void rondaDeApuestas() {
        int apuestaMaxima = apuestas.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        boolean rondaActiva = true;
        Scanner scanner = new Scanner(System.in);

        while (rondaActiva) {
            rondaActiva = false;

            for (Jugador jugador : jugadores) {
                if (!jugador.estaActivo()) continue;

                int apuestaActual = apuestas.getOrDefault(jugador, 0);
                int diferencia = apuestaMaxima - apuestaActual;

                if (jugador.getBote() <= 0) {
                    jugador.setAllIn(true);
                    System.out.println(jugador.getNombre() + " está en ALL-IN.");
                    continue;
                }

                System.out.println(jugador.getNombre() + ", tu turno. Bote actual: " + bote);
                if (diferencia == 0) {
                    System.out.println("Opciones: 1) Check  2) Raise  3) Fold");
                } else {
                    System.out.println("Opciones: 1) Call " + diferencia + "  2) Raise  3) Fold");
                }

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1: // Call / Check
                        if (diferencia > 0) {
                            long fichasDisponibles = jugador.getBote();
                            int aPagar = (int) Math.min(diferencia, fichasDisponibles);
                            jugador.apostar(aPagar);
                            apuestas.put(jugador, apuestaActual + aPagar);
                            bote += aPagar;

                            if (aPagar < diferencia) {
                                jugador.setAllIn(true);
                                System.out.println(jugador.getNombre() + " se va ALL-IN con " + aPagar);
                            }
                        }
                        break;

                    case 2: // Raise
                        System.out.println("¿Cuánto deseas subir?");
                        int subida = scanner.nextInt();
                        int total = apuestaMaxima + subida;

                        if (jugador.getBote() + apuestaActual < total) {
                            System.out.println("No tienes suficientes fichas. Se considera ALL-IN.");
                            long allInTotal = jugador.getBote();
                            jugador.apostar(allInTotal);
                            apuestas.put(jugador, (int) (apuestaActual + allInTotal));
                            bote += allInTotal;
                            jugador.setAllIn(true);
                        } else {
                            jugador.apostar(total - apuestaActual);
                            apuestas.put(jugador, total);
                            bote += total - apuestaActual;
                            apuestaMaxima = total;
                            rondaActiva = true;
                        }
                        break;

                    case 3: // Fold
                        jugador.retirar();
                        break;

                    default:
                        System.out.println("Opción inválida. Se considera Fold.");
                        jugador.retirar();
                        break;
                }

                if (jugadores.stream().filter(j -> j.estaActivo() || j.isAllIn()).count() <= 1) {
                    return;
                }
            }
        }
    }

    public void resetearRonda() {
        // Reiniciar mazo y cartas comunitarias
        this.mazo = new Mazo();
        this.mazo.revolverMazo();
        this.cartasComunitarias.clear();

        // Resetear apuestas y bote
        this.bote = 0;
        this.apuestas.clear();

        // Reiniciar estado de los jugadores
        for (Jugador j : jugadores) {
            j.activar();
            j.setAllIn(false);
            j.setMano(new ArrayList<>());
            apuestas.put(j, 0);
        }

        // Reiniciar fase
        this.fase = 0;
    }

    public int getFase() {
        return fase;
    }

    public ArrayList<Carta> getCartasComunitarias() {
        return cartasComunitarias;
    }

@Override
public String realizarCheckCall(Jugador jugador) {
    int apuestaActual = apuestas.getOrDefault(jugador, 0);
    int maxApuesta = apuestas.values().stream().max(Integer::compareTo).orElse(0);
    int diferencia = maxApuesta - apuestaActual;

    if (diferencia > 0) {
        int toCall = (int) Math.min(diferencia, jugador.getBote());
        jugador.apostar(toCall);
        bote += toCall;
        apuestas.put(jugador, apuestaActual + toCall);
        if (toCall < diferencia) {
            jugador.setAllIn(true);
            return jugador.getNombre() + " se va ALL-IN con " + toCall;
        }
        return jugador.getNombre() + " iguala la apuesta con " + toCall;
    } else {
        return jugador.getNombre() + " hace Check.";
    }
}

@Override
public String realizarRaise(Jugador jugador, int cantidad) {
    int apuestaActual = apuestas.getOrDefault(jugador, 0);
    int maxApuesta = apuestas.values().stream().max(Integer::compareTo).orElse(0);
    int nuevaApuesta = maxApuesta + cantidad;

    if (jugador.getBote() + apuestaActual < nuevaApuesta) {
        long allInTotal = jugador.getBote();
        jugador.apostar(allInTotal);
        apuestas.put(jugador, (int) (apuestaActual + allInTotal));
        bote += allInTotal;
        jugador.setAllIn(true);
        return jugador.getNombre() + " se va ALL-IN con " + allInTotal;
    } else {
        int aPagar = nuevaApuesta - apuestaActual;
        jugador.apostar(aPagar);
        apuestas.put(jugador, nuevaApuesta);
        bote += aPagar;
        return jugador.getNombre() + " sube la apuesta a " + nuevaApuesta;
    }
}

@Override
public String realizarFold(Jugador jugador) {
    jugador.retirar();
    return jugador.getNombre() + " se retira.";
}

public void incrementarBote(long bote) {
    this.bote += bote;
}

    public void setBote(int bote) {
        this.bote = bote;
    }
}