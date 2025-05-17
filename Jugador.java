import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;
    private long bote;
    private boolean activo;

    public Jugador(String nombre, long bote) {
        this.nombre = nombre;
        this.bote = bote;
        this.mano = new ArrayList<>();
        this.activo = true;
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
        this.activo = true; // Reactivar jugador para nueva mano
    }

    public long getBote() {
        return bote;
    }

    public void apostar(long cantidad) {
        if (cantidad > bote) {
            throw new IllegalArgumentException("Fondos insuficientes para apostar");
        }
        bote -= cantidad;
    }

    public void ganarFichas(long cantidad) {
        bote += cantidad;
    }

    public void mostrarMano() {
        for (Carta c : mano) {
            System.out.print(c.toString() + " ");
        }
        System.out.println();
    }

    public boolean estaActivo() {
        return activo;
    }

    public void retirar() {
        this.activo = false;
    }
}