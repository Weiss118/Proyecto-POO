import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class InterfazPoker extends JFrame {
    private JPanel panelActual;
    private JuegoPoker juegoSeleccionado;
    private int tipoJuegoSeleccionado; // 1 = Texas, 2 = Stud
    private int rondaActual = 0; // 0: Preflop, 1: Flop, 2: Turn, 3: River, 4: Showdown

    public InterfazPoker() {
        setTitle("Simulador de Poker");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mostrarFaseSeleccion();
    }

    private void mostrarFaseSeleccion() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Selecciona el tipo de juego");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        panel.add(titulo, gbc);

        JButton botonTexas = new JButton("Texas Hold'em");
        JButton botonStud = new JButton("Seven Card Stud");

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(botonTexas, gbc);

        gbc.gridx = 1;
        panel.add(botonStud, gbc);

        botonTexas.addActionListener(e -> {
            tipoJuegoSeleccionado = 1;
            mostrarFaseConfiguracion();
        });

        botonStud.addActionListener(e -> {
            tipoJuegoSeleccionado = 2;
            mostrarFaseConfiguracion();
        });

        actualizarPanel(panel);
    }

    private void mostrarFaseConfiguracion() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Configuración de jugadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Cantidad de jugadores:"), gbc);

        int maxJugadores = (tipoJuegoSeleccionado == 1) ? 10 : 8;
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(2, 2, maxJugadores, 1));
        gbc.gridx = 1;
        panel.add(spinner, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Bote inicial:"), gbc);

        JTextField campoBote = new JTextField(10);
        gbc.gridx = 1;
        panel.add(campoBote, gbc);

        JButton continuar = new JButton("Continuar");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(continuar, gbc);

        continuar.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();
            String textoBote = campoBote.getText().trim();

            if (!textoBote.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El bote debe ser un número entero.");
                return;
            }

            int bote = Integer.parseInt(textoBote);
            mostrarFaseNombres(cantidad, bote);
        });

        actualizarPanel(panel);
    }

    private void mostrarFaseNombres(int cantidad, int boteInicial) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        ArrayList<JTextField> campos = new ArrayList<>();

        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Nombres de los jugadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        for (int i = 0; i < cantidad; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            panel.add(new JLabel("Jugador " + (i + 1) + ":"), gbc);

            JTextField campo = new JTextField(12);
            campos.add(campo);
            gbc.gridx = 1;
            panel.add(campo, gbc);
        }

        JButton iniciar = new JButton("Iniciar juego");
        gbc.gridy = cantidad + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(iniciar, gbc);

        iniciar.addActionListener(e -> {
            ArrayList<Jugador> jugadores = new ArrayList<>();
            for (JTextField campo : campos) {
                String nombre = campo.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los jugadores deben tener nombre.");
                    return;
                }
                jugadores.add(new Jugador(nombre, boteInicial));
            }

            if (tipoJuegoSeleccionado == 1) {
                juegoSeleccionado = new TexasHoldEm(jugadores);
            } else {
                juegoSeleccionado = new SevenCardStud(jugadores);
            }

            mostrarFaseJuego();
        });

        actualizarPanel(panel);
    }

    private void mostrarFaseJuego() {
    JPanel panel = new JPanel(new BorderLayout());

    JTextArea areaTexto = new JTextArea();
    areaTexto.setEditable(false);
    areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
    JScrollPane scroll = new JScrollPane(areaTexto);
    panel.add(scroll, BorderLayout.CENTER);

    // Panel inferior con botones de acción
    JPanel panelInferior = new JPanel(new BorderLayout());

    JButton botonSiguienteRonda = new JButton("Siguiente ronda");
    panelInferior.add(botonSiguienteRonda, BorderLayout.NORTH);

    JPanel panelAcciones = new JPanel();
    JButton botonCheckCall = new JButton("Check / Call");
    JButton botonRaise = new JButton("Raise");
    JButton botonFold = new JButton("Fold");

    panelAcciones.add(botonCheckCall);
    panelAcciones.add(botonRaise);
    panelAcciones.add(botonFold);
    panelInferior.add(panelAcciones, BorderLayout.SOUTH);

    panel.add(panelInferior, BorderLayout.SOUTH);

    actualizarPanel(panel);

    // Repartir cartas iniciales al comenzar
    juegoSeleccionado.repartirCartasIniciales();
    areaTexto.setText(juegoSeleccionado.obtenerEstadoJuego());

    botonSiguienteRonda.addActionListener(e -> {
        if (rondaActual < juegoSeleccionado.getNumeroRondasTotales()) {
            juegoSeleccionado.siguienteFase();
            rondaActual++;
            areaTexto.setText(juegoSeleccionado.obtenerEstadoJuego());

            if (rondaActual == juegoSeleccionado.getNumeroRondasTotales()) {
                botonSiguienteRonda.setText("Mostrar ganador");
            }
        } else {
            String ganador = juegoSeleccionado.obtenerGanador();
            areaTexto.append("\n--- SHOWDOWN ---\n" + ganador);
            botonSiguienteRonda.setEnabled(false);
        }
    });

    // Lógica botón Check/Call
    botonCheckCall.addActionListener(e -> {
        Jugador jugador = juegoSeleccionado.getJugadorActual();
        int diferencia = juegoSeleccionado.getBote() - jugador.getApuestaRonda(); // este método puede que necesites implementarlo
        if (diferencia > 0 && jugador.getBote() >= diferencia) {
            jugador.apostar(diferencia);
            ((TexasHoldEm) juegoSeleccionado).incrementarBote(diferencia);
            areaTexto.append("\n" + jugador.getNombre() + " hace Call de " + diferencia);
        } else {
            areaTexto.append("\n" + jugador.getNombre() + " hace Check");
        }
        juegoSeleccionado.pasarTurno();
        areaTexto.append("\n\n" + juegoSeleccionado.obtenerEstadoJuego());
    });

    // Lógica botón Raise
    botonRaise.addActionListener(e -> {
        Jugador jugador = juegoSeleccionado.getJugadorActual();
        String input = JOptionPane.showInputDialog(this, "¿Cuánto deseas subir?");
        if (input != null && input.matches("\\d+")) {
            int cantidad = Integer.parseInt(input);
            if (jugador.getBote() >= cantidad) {
                jugador.apostar(cantidad);
                ((TexasHoldEm) juegoSeleccionado).setBote(juegoSeleccionado.getBote() + cantidad);
                ((TexasHoldEm) juegoSeleccionado).incrementarBote(cantidad);
                areaTexto.append("\n" + jugador.getNombre() + " hace Raise de " + cantidad);
                juegoSeleccionado.pasarTurno();
                areaTexto.append("\n\n" + juegoSeleccionado.obtenerEstadoJuego());
            } else {
                JOptionPane.showMessageDialog(this, "No tienes suficiente dinero.");
            }
        }
    });

    // Lógica botón Fold
    botonFold.addActionListener(e -> {
        Jugador jugador = juegoSeleccionado.getJugadorActual();
        jugador.retirar();
        areaTexto.append("\n" + jugador.getNombre() + " hace Fold");
        juegoSeleccionado.pasarTurno();
        areaTexto.append("\n\n" + juegoSeleccionado.obtenerEstadoJuego());
    });
}

    private void actualizarPanel(JPanel nuevo) {
        setContentPane(nuevo);
        panelActual = nuevo;
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazPoker().setVisible(true));
    }
}

