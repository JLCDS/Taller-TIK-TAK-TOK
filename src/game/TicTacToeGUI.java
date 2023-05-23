package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;



public class TicTacToeGUI extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private JLabel statusLabel;
    private TicTacToe game;
    private String playerName;
    private String result;
    private LocalDateTime endTime = null;
    private char playerSymbol;
    private char computerSymbol;

    public TicTacToeGUI(String playerName) {
        super("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLayout(new GridLayout(4, 3));

        buttons = new JButton[3][3];
        game = new TicTacToe();
        this.playerName = playerName;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 50));
                buttons[row][col].addActionListener(this);
                add(buttons[row][col]);
            }
        }

        statusLabel = new JLabel("Turno de " + playerName + " (X)");
        add(statusLabel);

        setVisible(true);
        displayInstructions();
        chooseSymbol();
    }

    private void displayInstructions() {
        JOptionPane.showMessageDialog(null, "Instrucciones:\n" +
                "Haz clic en las celdas para realizar tu movimiento.\n" +
                "El tablero tiene las siguientes coordenadas:\n" +
                " 0 1 2\n" +
                "0 | | \n" +
                "1 | | \n" +
                "2 | | \n" +
                "�Buena suerte!");
    }

    private void chooseSymbol() {
        String[] options = { "X", "O" };
        int choice = JOptionPane.showOptionDialog(null, "Elige tu s�mbolo:", "Selecci�n de s�mbolo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            playerSymbol = 'X';
            computerSymbol = 'O';
        } else if (choice == 1) {
            playerSymbol = 'O';
            computerSymbol = 'X';
        } else {
            // Por defecto, utilizar X como s�mbolo del jugador
            playerSymbol = 'X';
            computerSymbol = 'O';
        }

        statusLabel.setText("Turno de " + playerName + " (" + playerSymbol + ")");
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int row = -1;
        int col = -1;

        // Encontrar la posici�n del bot�n clickeado
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // Realizar el movimiento del jugador
        boolean validMove = game.makeMove(row, col, playerSymbol);

        if (validMove) {
            button.setText(String.valueOf(playerSymbol));

            // Verificar si hay un ganador o un empate
            if (game.hasWinningCombination(playerSymbol)) {
                result = "Gan�";
                statusLabel.setText("�Ganaste!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
            } else if (game.isBoardFull()) {
                result = "Empate";
                statusLabel.setText("�Empate!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
            } else {
                game.switchPlayer();
                statusLabel.setText("Turno de la computadora (" + computerSymbol + ")");
                computerMove();
            }
        }
    }

    private void computerMove() {
        // Verificar si la computadora puede ganar en el siguiente movimiento
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, computerSymbol);
                    if (game.hasWinningCombination(computerSymbol)) {
                        buttons[row][col].setText(String.valueOf(computerSymbol));
                        result = "Perdi�";
                        statusLabel.setText("�Perdiste!");
                        disableButtons();
                        saveGameResult();
                        showWelcomeWindow();
                        return;
                    }
                    game.clearCell(row, col);
                }
            }
        }

        // Verificar si el jugador puede ganar en el siguiente movimiento y bloquearlo
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, playerSymbol);
                    if (game.hasWinningCombination(playerSymbol)) {
                        game.clearCell(row, col);
                        game.makeMove(row, col, computerSymbol);
                        buttons[row][col].setText(String.valueOf(computerSymbol));
                        game.switchPlayer();
                        statusLabel.setText("Turno de " + playerName + " (" + game.getCurrentPlayerSymbol() + ")");
                        return;
                    }
                    game.clearCell(row, col);
                }
            }
        }

        // Si no es posible hacer un movimiento ganador, hacer un movimiento aleatorio
        Random random = new Random();
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if (game.isCellEmpty(row, col)) {
                game.makeMove(row, col, computerSymbol);
                buttons[row][col].setText(String.valueOf(computerSymbol));
                game.switchPlayer();
                statusLabel.setText("Turno de " + playerName + " (" + game.getCurrentPlayerSymbol() + ")");
                return;
            }
        }
    }

    private void disableButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    private void saveGameResult() {
        String fileName = "history_game.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            endTime = LocalDateTime.now(); // Asignar valor a endTime
            writer.write("Jugador: " + playerName + "; " + "Resultado: " + result + "; " + "S�mbolo: " + playerSymbol + "; " + "Tiempo de finalizaci�n: "
                    + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar el resultado del juego.");
        }
    }

    private void showWelcomeWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WelcomeWindow welcomeWindow = new WelcomeWindow();
                welcomeWindow.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WelcomeWindow welcomeWindow = new WelcomeWindow();
                welcomeWindow.setVisible(true);
            }
        });
    }
}
