public class Carta implements Comparable<Carta> {
    
    private String palo;
    private String color;
    private int valor;

    public Carta(String palo, String color, int valor) {
        this.palo = palo;
        this.color = color;
        this.valor = valor;
    }

    @Override
    public int compareTo(Carta otra) {
        if (this.valor != otra.valor) {
            return Integer.compare(this.valor, otra.valor);
        } else {
            return this.palo.compareTo(otra.palo);
        }
    }

    @Override
    public String toString() {
        //Este metodo le da el formato que debe tener al mostrarse la carta en consola.
        String laCarta;
        switch(valor) {
            case 1:
                laCarta = "|A "+palo+"|";
                break;
            case 11:
                laCarta = "|J "+palo+"|";
                break;
            case 12: 
                laCarta = "|Q "+palo+"|";
                break;
            case 13:
                laCarta = "|K "+palo+"|";
                break;
            default: 
                laCarta = "|" +valor+ " " +palo+"|";
        }
        return laCarta;
    }

    // Bloque de setters y getters
    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}