import java.util.*;

public class EvaluadorManos {

    public static int evaluar(ArrayList<Carta> cartas) {
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

    private static boolean esColor(ArrayList<Carta> cartas) {
        String palo = cartas.get(0).getPalo();
        for (Carta c : cartas) {
            if (!c.getPalo().equals(palo)) return false;
        }
        return true;
    }

    private static boolean esEscalera(ArrayList<Integer> valores) {
        Set<Integer> s = new TreeSet<>(valores);
        if (s.size() < 5) return false;

        List<Integer> list = new ArrayList<>(s);
        for (int i = 0; i <= list.size() - 5; i++) {
            if (list.get(i + 4) - list.get(i) == 4) return true;
        }

        if (s.containsAll(Arrays.asList(1, 10, 11, 12, 13))) return true;
        return false;
    }
    
    public static String descripcionJugada(int valor) {
    switch (valor) {
        case 9: return "Escalera de color";
        case 8: return "Poker";
        case 7: return "Full House";
        case 6: return "Color";
        case 5: return "Escalera";
        case 4: return "Trío";
        case 3: return "Doble par";
        case 2: return "Un par";
        default: return "Carta alta";
    }
}

}