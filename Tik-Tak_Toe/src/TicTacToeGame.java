import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGame extends JFrame {
    private JButton[][] buttons;
    private char currentPlayer;

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));

        buttons = new JButton[3][3];
        currentPlayer = 'X';

        initializeButtons();
    }

    private void initializeButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));

                buttons[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource();
                        clickedButton.setText(Character.toString(currentPlayer));
                        clickedButton.setEnabled(false);

                        if (checkForWin()) {
                            JOptionPane.showMessageDialog(null, "¡" + currentPlayer + " ha ganado!");
                            resetGame();
                        } else if (checkForDraw()) {
                            JOptionPane.showMessageDialog(null, "Empate");
                            resetGame();
                        } else {
                            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                        }
                    }
                });

                add(buttons[row][col]);
            }
        }
    }

    private boolean checkForWin() {
        // Comprobar filas
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals(buttons[row][1].getText()) &&
                    buttons[row][1].getText().equals(buttons[row][2].getText()) &&
                    !buttons[row][0].getText().equals("")) {
                return true;
            }
        }

        // Comprobar columnas
        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals(buttons[1][col].getText()) &&
                    buttons[1][col].getText().equals(buttons[2][col].getText()) &&
                    !buttons[0][col].getText().equals("")) {
                return true;
            }
        }

        // Comprobar diagonales
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().equals("")) {
            return true;
        }

        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().equals("")) {
            return true;
        }

        return false;
    }

    private boolean checkForDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
        currentPlayer = 'X';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TicTacToeGame().setVisible(true);
            }
        });
    }
}
