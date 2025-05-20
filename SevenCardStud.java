import java.util.*;

public class SevenCardStud extends JuegoPoker {

    private Mazo mazo;
    private int fase; // 0 a 4 para cada "street"
    private int bote;
    private Map<Jugador, Integer> apuestas;

    public SevenCardStud(ArrayList<Jugador> jugadores) {
        super(jugadores);
        this.mazo = new Mazo();
        this.fase = 0;
        this.bote = 0;
        this.apuestas = new HashMap<>();
        for (Jugador j : jugadores) apuestas.put(j, 0);
    }

    @Override
    public int getNumeroRondasTotales() {
        return 4; // Fourth, Fifth, Sixth, Seventh Streets
    }

    @Override
    public String obtenerEstadoJuego() {
        StringBuilder sb = new StringBuilder();
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
        Jugador ganador = jugadores.get(new Random().nextInt(jugadores.size()));
        ganador.ganarFichas(bote);
        return "Ganador: " + ganador.getNombre() + " con " + bote + " puntos.\n";
    }

    @Override
    public void repartirCartasIniciales() {
        mazo.revolverMazo();
        for (Jugador j : jugadores) {
            j.setMano(new ArrayList<>());
            // Dos ocultas
            j.getMano().add(mazo.darCarta());
            j.getMano().add(mazo.darCarta());
            // Una descubierta
            j.getMano().add(mazo.darCarta());
        }
        fase = 0;
    }

    @Override
    public void siguienteFase() {
        switch (fase) {
            case 0: // Fourth Street
                System.out.println("=== FASE: FOURTH STREET ===");
                repartirCartaVisible();
                ejecutarApuesta();
                break;
            case 1: // Fifth Street
                System.out.println("=== FASE: FIFTH STREET ===");
                repartirCartaVisible();
                ejecutarApuesta();
                break;
            case 2: // Sixth Street
                System.out.println("=== FASE: SIXTH STREET ===");
                repartirCartaVisible();
                ejecutarApuesta();
                break;
            case 3: // Seventh Street
                System.out.println("=== FASE: SEVENTH STREET ===");
                repartirCartaOculta();
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

    private void repartirCartaVisible() {
        for (Jugador j : jugadores) {
            if (j.estaActivo()) {
                Carta c = mazo.darCarta();
                j.getMano().add(c);
                System.out.println(j.getNombre() + " recibe carta visible: " + c);
            }
        }
    }

    private void repartirCartaOculta() {
        for (Jugador j : jugadores) {
            if (j.estaActivo()) {
                Carta c = mazo.darCarta();
                j.getMano().add(c);
                System.out.println(j.getNombre() + " recibe carta oculta.");
            }
        }
    }

    @Override
    public void mostrarCartas() {
        for (Jugador j : jugadores) {
            System.out.print(j.getNombre() + ": ");
            j.mostrarMano(); // Este método ya imprime la mano completa, tú decides si ocultas algunas cartas
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
            if (!j.estaActivo()) continue;

            ArrayList<Carta> combinadas = j.getMano();
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

    public void rondaDeApuestas() {
        Scanner sc = new Scanner(System.in);
        int inicio = obtenerIndicePrimerJugadorActivo();

        int apuestaActual = 0;
        Map<Jugador, Integer> apuestasActuales = new HashMap<>();
        Set<Jugador> activos = new HashSet<>();

        for (Jugador j : jugadores) {
            if (j.estaActivo()) {
                apuestasActuales.put(j, 0);
                activos.add(j);
            }
        }

        boolean hayApuesta = false;
        boolean rondaTerminada = false;

        while (!rondaTerminada && activos.size() > 1) {
            rondaTerminada = true;

            for (int i = 0; i < jugadores.size(); i++) {
                int idx = (inicio + i) % jugadores.size();
                Jugador jugador = jugadores.get(idx);

                if (!activos.contains(jugador)) continue;

                int apuestaJugador = apuestasActuales.get(jugador);
                System.out.println("\nTurno de " + jugador.getNombre() + " (Fichas: " + jugador.getBote() + ")");
                System.out.println("Apuesta actual: " + apuestaActual + ", tú has apostado: " + apuestaJugador);

                if (!hayApuesta) {
                    System.out.print("¿[1] Check, [2] Apuesta, [3] Retirarse? ");
                    int eleccion = sc.nextInt();

                    switch (eleccion) {
                        case 1: // Check
                            // No se hace nada
                            break;
                        case 2: // Apuesta
                            System.out.print("¿Cuánto deseas apostar? ");
                            int cantidad = sc.nextInt();
                            if (jugador.getBote() < cantidad) {
                                System.out.println("No tienes suficientes fichas.");
                                i--; // repetir turno
                                continue;
                            }
                            jugador.apostar(cantidad);
                            bote += cantidad;
                            apuestaActual = cantidad;
                            apuestasActuales.put(jugador, cantidad);
                            hayApuesta = true;
                            rondaTerminada = false;
                            break;
                        case 3: // Retirarse
                            jugador.retirar();
                            activos.remove(jugador);
                            break;
                        default:
                            System.out.println("Opción inválida.");
                            i--; // repetir turno
                            break;
                    }
                } else {
                    System.out.print("¿[1] Igualar, [2] Subir, [3] Retirarse? ");
                    int eleccion = sc.nextInt();

                    switch (eleccion) {
                        case 1: // Igualar
                            int diferencia = apuestaActual - apuestaJugador;
                            if (jugador.getBote() < diferencia) {
                            System.out.println("No tienes suficientes fichas para igualar.");
                            i--; // repetir turno
                            continue;
                        }
                            jugador.apostar(diferencia);
                            bote += diferencia;
                            apuestasActuales.put(jugador, apuestaActual);
                            break;
                        case 2: // Subir
                            System.out.print("¿Cuánto deseas subir (total)? ");
                            int nuevaApuesta = sc.nextInt();
                            if (nuevaApuesta <= apuestaActual) {
                                System.out.println("La subida debe ser mayor que la apuesta actual.");
                                i--; // repetir turno
                                continue;
                            }
                            int total = nuevaApuesta - apuestaJugador;
                            if (jugador.getBote() < total) {
                                System.out.println("No tienes suficientes fichas para subir.");
                                i--; // repetir turno
                                continue;
                            }
                            jugador.apostar(total);
                            bote += total;
                            apuestaActual = nuevaApuesta;
                            apuestasActuales.put(jugador, nuevaApuesta);
                            rondaTerminada = false;
                            break;
                        case 3: // Retirarse
                            jugador.retirar();
                            activos.remove(jugador);
                            break;
                        default:
                            System.out.println("Opción inválida.");
                            i--; // repetir turno
                            break;
                    }
                }
            }

            // Verificar si todos los jugadores activos han igualado la apuesta actual
            for (Jugador j : activos) {
                if (apuestasActuales.get(j) != apuestaActual) {
                    rondaTerminada = false;
                    break;
                }
            }
        }

        System.out.println("\nFin de la ronda de apuestas. Bote: " + bote + " fichas.");
    }

    private int obtenerIndicePrimerJugadorActivo() {    
        int mejorIndice = -1;
        Carta mejorCarta = null;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            if (!j.estaActivo()) continue;

            // Buscar la carta visible más alta (la última es visible en fases 1 a 3)
            ArrayList<Carta> mano = j.getMano();
            Carta visible = mano.get(mano.size() - 1);

            if (mejorCarta == null || visible.compareTo(mejorCarta) > 0) {
                mejorCarta = visible;
                mejorIndice = i;
            }
        }

        return mejorIndice;
    }

    public int getFase() {
        return fase;
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
