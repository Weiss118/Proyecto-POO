import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;
    private long bote;
    private boolean activo;
    private boolean allIn = false;
    private int apuestaRonda;

    public Jugador(String nombre, long bote) {
        this.nombre = nombre;
        this.bote = bote;
        this.mano = new ArrayList<>();
        this.activo = true;
    }

    public int getApuestaRonda() {
        return apuestaRonda;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
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

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean valor) {
        allIn = valor;
    }


    public void apostar(long cantidad) {
        if (cantidad > bote) {
            throw new IllegalArgumentException("Fondos insuficientes para apostar");
        }
        bote -= cantidad;
        apuestaRonda += cantidad;
    }

    public void reiniciarApuestaRonda() {
        apuestaRonda = 0;
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

    // Un jugador se retira de la partida
    public void retirar() {
        this.activo = false;
    }

    // Un jugador vuelve activarse en la partida
    public void activar() {
        this.activo = true;
    }
}
