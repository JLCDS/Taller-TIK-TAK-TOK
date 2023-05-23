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
	private LocalDateTime endTime = null;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private TicTacToe game;
    private String playerName;
    private String result;
    

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
    }

    private void displayInstructions() {
        JOptionPane.showMessageDialog(null, "Instrucciones:\n" +
                "Haz clic en las celdas para realizar tu movimiento.\n" +
                "El tablero tiene las siguientes coordenadas:\n" +
                " 0 1 2\n" +
                "0 | | \n" +
                "1 | | \n" +
                "2 | | \n" +
                "¡Buena suerte!");
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int row = -1;
        int col = -1;

        // Find the clicked button's position
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // Make the player's move
        boolean validMove = game.makeMove(row, col, TicTacToe.PLAYER_SYMBOL);

        if (validMove) {
            button.setText(String.valueOf(TicTacToe.PLAYER_SYMBOL));

            // Check for a winner or a draw
            if (game.hasWinningCombination(TicTacToe.PLAYER_SYMBOL)) {
                result = "Ganó";
                statusLabel.setText("¡Ganaste!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
            } else if (game.isBoardFull()) {
                result = "Empate";
                statusLabel.setText("¡Empate!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
            } else {
                game.switchPlayer();
                statusLabel.setText("Turno de la computadora (O)");
                computerMove();
            }
        }
    }

    private void computerMove() {
        // Check if the computer can win on the next move
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, TicTacToe.COMPUTER_SYMBOL);
                    if (game.hasWinningCombination(TicTacToe.COMPUTER_SYMBOL)) {
                        buttons[row][col].setText(String.valueOf(TicTacToe.COMPUTER_SYMBOL));
                        result = "Perdió";
                        statusLabel.setText("¡Perdiste!");
                        disableButtons();
                        saveGameResult();
                        showWelcomeWindow();
                        return;
                    }
                    game.clearCell(row, col);
                }
            }
        }

        // Check if the player can win on the next move and block it
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, TicTacToe.PLAYER_SYMBOL);
                    if (game.hasWinningCombination(TicTacToe.PLAYER_SYMBOL)) {
                        game.clearCell(row, col);
                        game.makeMove(row, col, TicTacToe.COMPUTER_SYMBOL);
                        buttons[row][col].setText(String.valueOf(TicTacToe.COMPUTER_SYMBOL));
                        game.switchPlayer();
                        statusLabel.setText("Turno de " + playerName + " (" + game.getCurrentPlayerSymbol() + ")");
                        return;
                    }
                    game.clearCell(row, col);
                }
            }
        }

        // If no winning move is possible, make a random move
        Random random = new Random();
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if (game.isCellEmpty(row, col)) {
                game.makeMove(row, col, TicTacToe.COMPUTER_SYMBOL);
                buttons[row][col].setText(String.valueOf(TicTacToe.COMPUTER_SYMBOL));
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
                writer.write("Jugador: " + playerName + "; " + " Resultado: " + result + "; " + "Simbolo: " + TicTacToe.PLAYER_SYMBOL + "; " + "Tiempo de finalizaci�n: "
                        + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.out.println("Error al guardar el resultado del juego.");
            }
        }

    private void resetGame() {
        game = new TicTacToe();
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setText("");
                button.setEnabled(true);
            }
        }
        statusLabel.setText("Turno de " + playerName + " (X)");
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
