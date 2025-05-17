import java.util.Scanner;

public class JuegoPokerGUI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido a Poker en Java");
        System.out.println("Elige modo de juego: ");
        System.out.println("1. Texas Hold'em");
        System.out.println("2. Seven Card Stud");

        int opcion = sc.nextInt();
        ManoPoker juego;

        if (opcion == 1) {
            juego = new TexasHoldem();
        } else {
            juego = new SevenCardStud();
        }

        System.out.print("Número de jugadores: ");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print("Nombre del jugador " + (i + 1) + ": ");
            String nombre = sc.next();
            juego.agregarJugador(new Jugador(nombre, 1000));
        }

        juego.repartirCartas();
        juego.realizarApuesta();
        juego.mostrarGanador();
    }
}