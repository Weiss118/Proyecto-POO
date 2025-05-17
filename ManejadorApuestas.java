import java.util.*;

public class ManejadorApuestas {
    private long bote;
    private long apuestaActual;

    public ManejadorApuestas() {
        this.bote = 0;
        this.apuestaActual = 0;
    }

    public void procesarApuestas(List<Jugador> jugadores) {
        apuestaActual = 0;
        bote = 0;
        Map<Jugador, Long> aportes = new HashMap<>();
        Set<Jugador> activos = new HashSet<>(jugadores);

        boolean rondaTerminada = false;
        int turno = 0;

        while (!rondaTerminada && activos.size() > 1) {
            Jugador jugador = jugadores.get(turno % jugadores.size());

            if (!activos.contains(jugador)) {
                turno++;
                continue; // jugador retirado
            }

            long aportado = aportes.getOrDefault(jugador, 0L);
            long porIgualar = apuestaActual - aportado;

            String decision = decidirAccion(jugador, porIgualar);

            switch (decision) {
                case "fold":
                    System.out.println(jugador.getNombre() + " se retira.");
                    activos.remove(jugador);
                    break;

                case "call":
                    if (jugador.getBote() >= porIgualar) {
                        jugador.apostar(porIgualar);
                        bote += porIgualar;
                        aportes.put(jugador, aportado + porIgualar);
                        System.out.println(jugador.getNombre() + " iguala con " + porIgualar);
                    } else {
                        // All-in (por simplicidad, lo tratamos como fold)
                        System.out.println(jugador.getNombre() + " no puede igualar y se retira.");
                        activos.remove(jugador);
                    }
                    break;

                case "check":
                    System.out.println(jugador.getNombre() + " pasa.");
                    break;

                case "raise":
                    long subida = 100; // apuesta fija por ahora
                    long total = porIgualar + subida;

                    if (jugador.getBote() >= total) {
                        jugador.apostar(total);
                        bote += total;
                        apuestaActual += subida;
                        aportes.put(jugador, aportado + total);
                        System.out.println(jugador.getNombre() + " sube a " + apuestaActual);
                    } else {
                        System.out.println(jugador.getNombre() + " no puede subir y se retira.");
                        activos.remove(jugador);
                    }
                    break;
            }

            // Verificar si todos han igualado la apuesta actual o se han retirado
            rondaTerminada = true;
            for (Jugador j : activos) {
                if (aportes.getOrDefault(j, 0L) < apuestaActual) {
                    rondaTerminada = false;
                    break;
                }
            }

            turno++;
        }

        System.out.println("Fin de la ronda de apuestas. Bote: " + bote);
    }

    private String decidirAccion(Jugador jugador, long porIgualar) {
        // Simulación simple de decisiones:
        if (porIgualar == 0) {
            return randomDecision("check", "raise");
        } else if (porIgualar > jugador.getBote()) {
            return "fold";
        } else {
            return randomDecision("call", "raise", "fold");
        }
    }

    private String randomDecision(String... opciones) {
        int idx = new Random().nextInt(opciones.length);
        return opciones[idx];
    }

    public long getBote() {
        return bote;
    }
}