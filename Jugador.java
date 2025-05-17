import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;
    private long bote;

    public Jugador(String nombre, long bote) {
        this.nombre = nombre;
        this.bote = bote;
        this.mano = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void recibirCarta(Carta carta) {
        mano.add(carta);
    }

    public void limpiarMano() {
        mano.clear();
    }

    public long getBote() {
        return bote;
    }

    public void apostar(long cantidad) {
        bote -= cantidad;
    }

    public void ganar(long cantidad) {
        bote += cantidad;
    }

    public void mostrarMano() {
        for (Carta c : mano) {
            System.out.print(c.toString() + " ");
        }
        System.out.println();
    }
}
