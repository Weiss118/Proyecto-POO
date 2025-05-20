import java.util.List;

public class ManoPoker implements Comparable<ManoPoker> {

    public enum TipoMano {
        ESCALERA_REAL,
        ESCALERA_COLOR,
        POKER,
        FULL_HOUSE,
        COLOR,
        ESCALERA,
        TRIO,
        DOBLE_PAR,
        PAR,
        CARTA_ALTA
    }

    private String nombre;
    private int fuerza;
    private List<Carta> cartas;

    public ManoPoker(String nombre, int fuerza, List<Carta> cartas) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.cartas = cartas;
    }

    public int getFuerza() {
        return fuerza;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    @Override
    public int compareTo(ManoPoker otra) {
        if (this.fuerza != otra.fuerza) {
            return Integer.compare(this.fuerza, otra.fuerza);
        }

        // Si tienen la misma fuerza, comparar por valores de cartas
        for (int i = 0; i < this.cartas.size(); i++) {
            int cmp = Integer.compare(this.cartas.get(i).getValor(), otra.cartas.get(i).getValor());
            if (cmp != 0) return cmp;
        }

        return 0; // Empate
    }

    @Override
    public String toString() {
        return nombre + " " + cartas;
    }
}