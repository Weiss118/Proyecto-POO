import java.util.ArrayList;
import java.util.Collections;

public class Mazo {

private ArrayList<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        llenar();
    }

    public void llenar() {
        // Llena la baraja con las cartas estándar
        String[] figuras = {"♣", "♦", "♥", "♠"};
        for (String figura : figuras) {
            for (int i = 1; i <= 13; i++) {
                String color = (figura.equals("♣") || figura.equals("♠")) ? "negro" : "rojo";
                cartas.add(new Carta(figura, color, i));
            }
        }
    }

    // Imprime en consola todas las cartas.
    public void mostrarCartas() {
        for (int i = 0 ; i < cartas.size() ; i++) {
            System.out.println(cartas.get(i).toString());
        }
    }

    // Regresa el mazo.
    public ArrayList<Carta> getMazo() {
        return cartas;
    }

    // Regresa el tamaño del mazo.
    public int getTamanoMazo() {
        return cartas.size();
    }

    // Regresa una carta del ArrayList.
    public Carta darCarta() {
        return cartas.remove(0);
    }

    //Regresa una carta especifica del ArrayList.
    public Carta darCarta(int indice) {
        return cartas.remove(indice);
    }

    // Elimina todas las cartas del mazo.
    public void limpiarMazo() {
        cartas.clear();
    }

    // Revuelve el mazo.
    public void revolverMazo() {
        Collections.shuffle(cartas);
    }
}
