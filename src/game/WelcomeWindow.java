package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WelcomeWindow extends JFrame {
    public WelcomeWindow() {
        super("Bienvenido a Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new FlowLayout());

        JLabel titleLabel = new JLabel("Bienvenido a Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel);

        JButton startButton = new JButton("Iniciar Juego");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String playerName = JOptionPane.showInputDialog("Ingresa tu nombre:");
                if (playerName != null && !playerName.isEmpty()) {
                    TicTacToeGUI game = new TicTacToeGUI(playerName);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre válido.");
                }
            }
        });
        add(startButton);

        JButton historyButton = new JButton("Mostrar Historial");
        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHistory();
            }
        });
        add(historyButton);
    }

    private void showHistory() {
        StringBuilder history = new StringBuilder();
        String fileName = "history_game.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el historial de juegos.");
        }

        if (history.length() > 0) {
            JOptionPane.showMessageDialog(null, "Historial de juegos:\n\n" + history.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No hay juegos registrados en el historial.");
        }
    }
}
