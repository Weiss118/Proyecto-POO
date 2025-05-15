
import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private ArrayList<Carta> mano;
    public long bote;

    public Jugador(String nombre, long bote) {
        this.nombre = nombre;
        this.bote = bote;
        mano = new ArrayList<>();
    }


}