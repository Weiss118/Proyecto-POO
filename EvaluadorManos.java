import java.util.*;

public class EvaluadorManos {

    public enum TipoMano {
        CARTA_ALTA, PAR, DOBLE_PAR, TRIO, ESCALERA,
        COLOR, FULL_HOUSE, POKER, ESCALERA_COLOR, ESCALERA_REAL
    }

    public static class ResultadoMano implements Comparable<ResultadoMano> {
        private TipoMano tipo;
        private List<Integer> cartasOrdenadas; // Para desempates

        public ResultadoMano(TipoMano tipo, List<Integer> cartasOrdenadas) {
            this.tipo = tipo;
            this.cartasOrdenadas = cartasOrdenadas;
        }

        public TipoMano getTipo() {
            return tipo;
        }

        public List<Integer> getCartasOrdenadas() {
            return cartasOrdenadas;
        }

        @Override
        public int compareTo(ResultadoMano otra) {
            if (this.tipo.ordinal() != otra.tipo.ordinal()) {
                return this.tipo.ordinal() - otra.tipo.ordinal();
            }
            for (int i = 0; i < cartasOrdenadas.size(); i++) {
                if (i >= otra.cartasOrdenadas.size()) break;
                int cmp = cartasOrdenadas.get(i) - otra.cartasOrdenadas.get(i);
                if (cmp != 0) return cmp;
            }
            return 0;
        }
    }

    // Clase ManoPoker actualizada con comparación
    public static class ManoPoker implements Comparable<ManoPoker> {
        private TipoMano tipo;
        private List<Carta> cartas;

        public ManoPoker(TipoMano tipo, List<Carta> cartas) {
            this.tipo = tipo;
            this.cartas = new ArrayList<>(cartas);
            this.cartas.sort(Comparator.comparingInt(Carta::getValor).reversed());
        }

        public TipoMano getTipo() {
            return tipo;
        }

        public List<Carta> getCartas() {
            return cartas;
        }

        // Para comparación, usamos el ordinal de tipo y luego valores ordenados
        @Override
        public int compareTo(ManoPoker mejorMano) {
            if (this.tipo.ordinal() != mejorMano.tipo.ordinal()) {
                return this.tipo.ordinal() - mejorMano.tipo.ordinal();
            }
            for (int i = 0; i < this.cartas.size(); i++) {
                int cmp = this.cartas.get(i).getValor() - mejorMano.cartas.get(i).getValor();
                if (cmp != 0) return cmp;
            }
            return 0;
        }

        @Override
        public String toString() {
            return tipo + " " + cartas;
        }
    }

    // Método principal que evalúa la mejor mano posible
    public static ManoPoker evaluarMejorMano(List<Carta> cartas) {
        if (cartas.size() < 5) {
            throw new IllegalArgumentException("Se requieren al menos 5 cartas para evaluar una mano de poker");
        }

        // Generar todas las combinaciones posibles de 5 cartas
        List<List<Carta>> combinaciones = generarCombinaciones(cartas, 5);
        ManoPoker mejor = null;

        for (List<Carta> combinacion : combinaciones) {
            ManoPoker mano = evaluarCombinacion(combinacion);
            if (mejor == null || mano.compareTo(mejor) > 0) {
                mejor = mano;
            }
        }

        return mejor;
    }

    private static ManoPoker evaluarCombinacion(List<Carta> cartas) {
        cartas.sort(Comparator.comparingInt(Carta::getValor).reversed());
        boolean esColor = esColor(cartas);
        boolean esEscalera = esEscalera(cartas);

        if (esColor && esEscalera && contieneValor(cartas, 14) && contieneValor(cartas, 10)) {
            // Escalera Real: A-K-Q-J-10 mismo palo
            return new ManoPoker(TipoMano.ESCALERA_REAL, cartas);
        }
        if (esColor && esEscalera) {
            return new ManoPoker(TipoMano.ESCALERA_COLOR, cartas);
        }
        if (esPoker(cartas)) {
            return new ManoPoker(TipoMano.POKER, cartas);
        }
        if (esFullHouse(cartas)) {
            return new ManoPoker(TipoMano.FULL_HOUSE, cartas);
        }
        if (esColor) {
            return new ManoPoker(TipoMano.COLOR, cartas);
        }
        if (esEscalera) {
            return new ManoPoker(TipoMano.ESCALERA, cartas);
        }
        if (esTrio(cartas)) {
            return new ManoPoker(TipoMano.TRIO, cartas);
        }
        if (esDoblePar(cartas)) {
            return new ManoPoker(TipoMano.DOBLE_PAR, cartas);
        }
        if (esPar(cartas)) {
            return new ManoPoker(TipoMano.PAR, cartas);
        }
        return new ManoPoker(TipoMano.CARTA_ALTA, cartas);
    }

    private static List<List<Carta>> generarCombinaciones(List<Carta> cartas, int k) {
        List<List<Carta>> resultado = new ArrayList<>();
        combinar(cartas, new ArrayList<>(), 0, k, resultado);
        return resultado;
    }

    private static void combinar(List<Carta> cartas, List<Carta> actual, int index, int k, List<List<Carta>> resultado) {
        if (actual.size() == k) {
            resultado.add(new ArrayList<>(actual));
            return;
        }
        for (int i = index; i < cartas.size(); i++) {
            actual.add(cartas.get(i));
            combinar(cartas, actual, i + 1, k, resultado);
            actual.remove(actual.size() - 1);
        }
    }

    private static boolean esColor(List<Carta> cartas) {
        String palo = cartas.get(0).getPalo();
        for (Carta c : cartas) {
            if (!c.getPalo().equals(palo)) {
                return false;
            }
        }
        return true;
    }

    private static boolean esEscalera(List<Carta> cartas) {
        List<Integer> valores = new ArrayList<>();
        for (Carta c : cartas) {
            if (!valores.contains(c.getValor()))
                valores.add(c.getValor());
        }
        Collections.sort(valores);

        // Escalera con As bajo
        if (valores.equals(Arrays.asList(2, 3, 4, 5, 14))) return true;

        for (int i = 1; i < valores.size(); i++) {
            if (valores.get(i) != valores.get(i - 1) + 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean contieneValor(List<Carta> cartas, int valor) {
        for (Carta c : cartas) {
            if (c.getValor() == valor) return true;
        }
        return false;
    }

    private static boolean esPar(List<Carta> cartas) {
        Map<Integer, Integer> contador = contarValores(cartas);
        return contador.values().stream().anyMatch(c -> c == 2);
    }

    private static boolean esDoblePar(List<Carta> cartas) {
        Map<Integer, Integer> contador = contarValores(cartas);
        long pares = contador.values().stream().filter(c -> c == 2).count();
        return pares >= 2;
    }

    private static boolean esTrio(List<Carta> cartas) {
        Map<Integer, Integer> contador = contarValores(cartas);
        return contador.values().stream().anyMatch(c -> c == 3);
    }

    private static boolean esFullHouse(List<Carta> cartas) {
        Map<Integer, Integer> contador = contarValores(cartas);
        boolean tieneTrio = contador.values().stream().anyMatch(c -> c == 3);
        boolean tienePar = contador.values().stream().anyMatch(c -> c == 2);
        return tieneTrio && tienePar;
    }

    private static boolean esPoker(List<Carta> cartas) {
        Map<Integer, Integer> contador = contarValores(cartas);
        return contador.values().stream().anyMatch(c -> c == 4);
    }

    private static Map<Integer, Integer> contarValores(List<Carta> cartas) {
        Map<Integer, Integer> contador = new HashMap<>();
        for (Carta c : cartas) {
            contador.put(c.getValor(), contador.getOrDefault(c.getValor(), 0) + 1);
        }
        return contador;
    }
}