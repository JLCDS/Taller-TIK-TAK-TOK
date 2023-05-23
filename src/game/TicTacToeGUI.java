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
    private char playerSymbol;
    private char computerSymbol;
    private String result;
    private LocalDateTime endTime = null;
    private JTabbedPane tabbedPane;
    private boolean symbolSelectionShown = false;
    
    
    public TicTacToeGUI(String playerName) {
        super("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);

        // Crea las pestañas
        tabbedPane = new JTabbedPane();

        JPanel gamePanel = new JPanel(new GridLayout(4, 3));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JPanel studentInfoPanel = createStudentInfoPanel();
        
        tabbedPane.addTab("Juego", gamePanel);
        tabbedPane.addTab("Información", infoPanel);
        tabbedPane.addTab("Integrantes", studentInfoPanel);
        
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        
        

        buttons = new JButton[3][3];
        game = new TicTacToe();
        this.playerName = playerName;
        
        

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 50));
                buttons[row][col].addActionListener(this);
                gamePanel.add(buttons[row][col]);
            }
        }
        

        statusLabel = new JLabel("Turno de " + playerName + " (X)");
        gamePanel.add(statusLabel);

        JLabel infoLabel = new JLabel("Información del jugador:");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoLabel);

        JLabel playerNameLabel = new JLabel("Nickname: " + playerName);
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerNameLabel);

        JLabel playerSymbolLabel = new JLabel("Símbolo seleccionado: " + playerSymbol);
        playerSymbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerSymbolLabel);

        setVisible(true);
        displayInstructions();
        if (!symbolSelectionShown) {
            showSymbolSelectionDialog();
            symbolSelectionShown = true;
        }
    }


    private JPanel createStudentInfoPanel() {
        JPanel studentInfoPanel = new JPanel();
        studentInfoPanel.setLayout(new BoxLayout(studentInfoPanel, BoxLayout.Y_AXIS));

        JLabel student1Label = new JLabel("Integrante 1:");
        JLabel student1NameLabel = new JLabel("Nombres: Juan David");
        JLabel student1LastNameLabel = new JLabel("Apellidos: Lopez Castro");
        JLabel student1CodeLabel = new JLabel("Código: 202023451 ");

        JLabel student2Label = new JLabel("Integrante 2:");
        JLabel student2NameLabel = new JLabel("Nombres: Silvia Juliana ");
        JLabel student2LastNameLabel = new JLabel("Apellidos: Rodriguez Rodriguez ");
        JLabel student2CodeLabel = new JLabel("Código: 202023822 ");

        JLabel universityLabel = new JLabel("Universidad: UPTC ");
        JLabel facultyLabel = new JLabel("Facultad: Facultad de Ingeniería");
        JLabel schoolLabel = new JLabel("Escuela: Escuela de Sistemas ");
        JLabel subjectLabel = new JLabel("Materia: Programacion II ");
        JLabel yearLabel = new JLabel("Año: 2023");
        JLabel semesterLabel = new JLabel("Semestre: Primer semestre ");

        studentInfoPanel.add(student1Label);
        studentInfoPanel.add(student1NameLabel);
        studentInfoPanel.add(student1LastNameLabel);
        studentInfoPanel.add(student1CodeLabel);

        studentInfoPanel.add(student2Label);
        studentInfoPanel.add(student2NameLabel);
        studentInfoPanel.add(student2LastNameLabel);
        studentInfoPanel.add(student2CodeLabel);

        studentInfoPanel.add(universityLabel);
        studentInfoPanel.add(facultyLabel);
        studentInfoPanel.add(schoolLabel);
        studentInfoPanel.add(subjectLabel);
        studentInfoPanel.add(yearLabel);
        studentInfoPanel.add(semesterLabel);

        return studentInfoPanel;
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

    private void showSymbolSelectionDialog() {
        Object[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(null, "Selecciona tu símbolo:", "Selección de símbolo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            playerSymbol = TicTacToe.PLAYER_SYMBOL;
            computerSymbol = TicTacToe.COMPUTER_SYMBOL;
        } else if (choice == 1) {
            playerSymbol = TicTacToe.COMPUTER_SYMBOL;
            computerSymbol = TicTacToe.PLAYER_SYMBOL;
        }

        tabbedPane.setSelectedIndex(0); // Muestra el tab del juego después de la selección del símbolo
        statusLabel.setText("Turno de " + playerName + " (" + playerSymbol + ")");
        
        Component[] infoComponents = ((JPanel) tabbedPane.getComponentAt(1)).getComponents();
        for (Component component : infoComponents) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getText().startsWith("Símbolo seleccionado:")) {
                    label.setText("Símbolo seleccionado: " + playerSymbol);
                    break;
                }
            }
        }
    }
    
    private void showResultDialog(String result) {
        String message;
        Color backgroundColor;

        if (result.equals("Ganó")) {
            message = "¡Ganaste!";
            backgroundColor = Color.GREEN;
        } else if (result.equals("Perdió")) {
            message = "¡Perdiste!";
            backgroundColor = Color.RED;
        } else {
            message = "¡Empate!";
            backgroundColor = Color.GRAY;
        }

        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);

        int choice = JOptionPane.showOptionDialog(null, panel, message, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Regresar"}, null);

        if (choice == 0) {
            setVisible(false);
            WelcomeWindow welcomeWindow = new WelcomeWindow();
            welcomeWindow.setVisible(true);
        }
    }
    

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int row = -1;
        int col = -1;

        // Encuentra la posición del botón clickeado
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // Realiza el movimiento del jugador
        boolean validMove = game.makeMove(row, col, playerSymbol);

        if (validMove) {
            button.setText(String.valueOf(playerSymbol));

            // Verifica si hay un ganador o un empate
            if (game.hasWinningCombination(playerSymbol)) {
                result = "Ganó";
                statusLabel.setText("¡Ganaste!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
                showResultDialog(result);
            } else if (game.isBoardFull()) {
                result = "Empate";
                statusLabel.setText("¡Empate!");
                disableButtons();
                saveGameResult();
                showWelcomeWindow();
                showResultDialog(result);
            } else {
                game.switchPlayer();
                statusLabel.setText("Turno de la computadora (" + computerSymbol + ")");
                computerMove();
               
                if (game.hasWinningCombination(computerSymbol)) {
                    result = "Perdió";
                    statusLabel.setText("¡Perdiste!");
                    disableButtons();
                    saveGameResult();
                    showResultDialog(result);
                } else if (game.isBoardFull()) {
                    result = "Empate";
                    statusLabel.setText("¡Empate!");
                    disableButtons();
                    saveGameResult();
                    showResultDialog(result);
                } else {
                    game.switchPlayer();
                    statusLabel.setText("Turno de " + playerName + " (" + playerSymbol + ")");
                }
                
            }
        }
    }

    private void computerMove() {
        // Verifica si la computadora puede ganar en el siguiente movimiento
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, computerSymbol);
                    if (game.hasWinningCombination(computerSymbol)) {
                        buttons[row][col].setText(String.valueOf(computerSymbol));
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

        // Verifica si el jugador puede ganar en el siguiente movimiento y bloquearlo
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (game.isCellEmpty(row, col)) {
                    game.makeMove(row, col, playerSymbol);
                    if (game.hasWinningCombination(playerSymbol)) {
                        game.clearCell(row, col);
                        game.makeMove(row, col, computerSymbol);
                        buttons[row][col].setText(String.valueOf(computerSymbol));
                        game.switchPlayer();
                        statusLabel.setText("Turno de " + playerName + " (" + playerSymbol + ")");
                        return;
                    }
                    game.clearCell(row, col);
                }
            }
        }

        // Si no es posible realizar un movimiento ganador, realizar un movimiento aleatorio
        Random random = new Random();
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if (game.isCellEmpty(row, col)) {
                game.makeMove(row, col, computerSymbol);
                buttons[row][col].setText(String.valueOf(computerSymbol));
                game.switchPlayer();
                statusLabel.setText("Turno de " + playerName + " (" + playerSymbol + ")");
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
            writer.write("Jugador: " + playerName + "; " + " Resultado: " + result + "; " + "Simbolo: " + playerSymbol + "; " + "Tiempo de finalización: "
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
