import java.util.*;

public class EvaluadorManos {

    public static int evaluar(ArrayList<Carta> cartas) {
        // Verificamos si la mano contiene 5 cartas
        if (cartas.size() != 5)
            throw new IllegalArgumentException("La mano debe contener exactamente 5 cartas");

        ArrayList<Integer> valores = new ArrayList<>();
        for (Carta c : cartas) {
            valores.add(c.getValor());
        }

        Map<Integer, Integer> conteo = new HashMap<>();
        for (int v : valores) {
            conteo.put(v, conteo.getOrDefault(v, 0) + 1);
        }

        boolean escalera = esEscalera(valores);
        boolean color = esColor(cartas);

        if (escalera && color) return 9;
        if (conteo.containsValue(4)) return 8;
        if (conteo.containsValue(3) && conteo.containsValue(2)) return 7;
        if (color) return 6;
        if (escalera) return 5;
        if (conteo.containsValue(3)) return 4;

        int pares = 0;
        for (int c : conteo.values()) {
            if (c == 2) pares++;
        }

        if (pares == 2) return 3;
        if (pares == 1) return 2;

        return 1; // Carta alta
    }

    public static int evaluarMejorMano(ArrayList<Carta> cartas) {
        if (cartas.size() < 5)
            throw new IllegalArgumentException("Se necesitan al menos 5 cartas para evaluar la mejor mano");

        List<List<Carta>> combinaciones = combinacionesDeCinco(cartas);
        int mejorValor = -1;

        for (List<Carta> mano : combinaciones) {
            int valor = evaluar(new ArrayList<>(mano));
            if (valor > mejorValor) {
                mejorValor = valor;
            }
        }

        return mejorValor;
    }

    // Verificamos la mejor combinacion de cartas
    public static List<List<Carta>> combinacionesDeCinco(List<Carta> cartas) {
        List<List<Carta>> resultado = new ArrayList<>();
        combinar(cartas, 5, 0, new ArrayList<>(), resultado);
        return resultado;
    }

    // Combinamos las cartas con las comunitarias
    private static void combinar(List<Carta> cartas, int k, int inicio, List<Carta> actual, List<List<Carta>> resultado) {
        if (actual.size() == k) {
            resultado.add(new ArrayList<>(actual));
            return;
        }

        for (int i = inicio; i < cartas.size(); i++) {
            actual.add(cartas.get(i));
            combinar(cartas, k, i + 1, actual, resultado);
            actual.remove(actual.size() - 1);
        }
    }

    // Verificamos si hay proyecto de color
    private static boolean esColor(ArrayList<Carta> cartas) {
        String palo = cartas.get(0).getPalo();
        for (Carta c : cartas) {
            if (!c.getPalo().equals(palo)) return false;
        }
        return true;
    }

    // Verificamos si hay escalera
    private static boolean esEscalera(ArrayList<Integer> valores) {
        Set<Integer> s = new TreeSet<>(valores);
        if (s.size() < 5) return false;

        List<Integer> list = new ArrayList<>(s);
        for (int i = 0; i <= list.size() - 5; i++) {
            if (list.get(i + 4) - list.get(i) == 4) return true;
        }

        // Verifica la escalera especial A-10-11-12-13
        if (s.containsAll(Arrays.asList(1, 10, 11, 12, 13))) return true;

        return false;
    }

    // Imprimimos cual es la mejor jugada del jugador
    public static String descripcionJugada(int valor) {
        return switch (valor) {
            case 9 -> "Escalera de color";
            case 8 -> "Poker";
            case 7 -> "Full House";
            case 6 -> "Color";
            case 5 -> "Escalera";
            case 4 -> "Trío";
            case 3 -> "Doble par";
            case 2 -> "Un par";
            default -> "Carta alta";
        };
    }
}