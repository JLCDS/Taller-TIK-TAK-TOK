package game;


	import java.io.BufferedWriter;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.time.LocalDateTime;
	import java.time.format.DateTimeFormatter;
	import java.util.Random;
	import java.util.Scanner;

	public class Game {
	    private static final char EMPTY = '-';
	    private static final char PLAYER_SYMBOL = 'X';
	    private static final char COMPUTER_SYMBOL = 'O';
	    private static final int BOARD_SIZE = 3;
	    private static final int WINNING_CONDITION = 3;

	    private char[][] board;
	    private Random random;
	    private Scanner scanner;
	    private String playerName;
	    private String result;
	    private LocalDateTime endTime;

	    public Game() {
	        board = new char[BOARD_SIZE][BOARD_SIZE];
	        random = new Random();
	        scanner = new Scanner(System.in);
	    }

	    public void play() {
	        initializeBoard();
	        playerName = getPlayerName();
	        displayInstructions();

	        while (true) {
	            playerMove();
	            if (checkGameEnd(PLAYER_SYMBOL)) {
	                result = "Gano";
	                break;
	            }

	            computerMove();
	            if (checkGameEnd(COMPUTER_SYMBOL)) {
	                result = "Perdio";
	                break;
	            }

	            if (isBoardFull()) {
	                result = "Empato";
	                break;
	            }
	        }

	        endTime = LocalDateTime.now();
	        displayResult();
	        saveGameResult();
	    }

	    private void initializeBoard() {
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                board[i][j] = EMPTY;
	            }
	        }
	    }

	    private String getPlayerName() {
	        System.out.print("Ingresa tu nombre: ");
	        return scanner.nextLine();
	    }

	    private void displayInstructions() {
	        System.out.println("Instrucciones:");
	        System.out.println("Ingresa las coordenadas (fila y columna) para realizar tu movimiento.");
	        System.out.println("El tablero tiene las siguientes coordenadas:");
	        System.out.println(" 0 1 2");
	        System.out.println("0 | | ");
	        System.out.println("1 | | ");
	        System.out.println("2 | | ");
	        System.out.println("¡Buena suerte!");
	        System.out.println();
	    }

	    private void playerMove() {
	        while (true) {
	            System.out.println(playerName + ", es tu turno (X).");
	            System.out.print("Ingresa la fila: ");
	            int row = scanner.nextInt();
	            System.out.print("Ingresa la columna: ");
	            int col = scanner.nextInt();

	            if (isValidMove(row, col)) {
	                board[row][col] = PLAYER_SYMBOL;
	                break;
	            } else {
	                System.out.println("Movimiento inválido. Inténtalo de nuevo.");
	            }
	        }

	        System.out.println();
	        displayBoard();
	        System.out.println();
	    }

	    private boolean isValidMove(int row, int col) {
	        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
	            return false;
	        }

	        return board[row][col] == EMPTY;
	    }

	    private void computerMove() {
	        System.out.println("Turno de la máquina (O).");
	        int row;
	        int col;

	       
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                if (board[i][j] == EMPTY && canWin(i, j, COMPUTER_SYMBOL)) {
	                    board[i][j] = COMPUTER_SYMBOL;
	                    displayBoard();
	                    System.out.println();
	                    return;
	                }
	            }
	        }

	       
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                if (board[i][j] == EMPTY && canWin(i, j, PLAYER_SYMBOL)) {
	                    board[i][j] = COMPUTER_SYMBOL;
	                    displayBoard();
	                    System.out.println();
	                    return;
	                }
	            }
	        }

	        do {
	            row = random.nextInt(BOARD_SIZE);
	            col = random.nextInt(BOARD_SIZE);
	        } while (!isValidMove(row, col));

	        board[row][col] = COMPUTER_SYMBOL;
	        displayBoard();
	        System.out.println();
	    }

	    private boolean canWin(int row, int col, char symbol) {
	       
	        int count = 0;
	        for (int j = 0; j < BOARD_SIZE; j++) {
	            if (board[row][j] == symbol) {
	                count++;
	            }
	        }
	        if (count == WINNING_CONDITION - 1) {
	            return true;
	        }

	        
	        count = 0;
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            if (board[i][col] == symbol) {
	                count++;
	            }
	        }
	        if (count == WINNING_CONDITION - 1) {
	            return true;
	        }

	        
	        if (row == col) {
	            count = 0;
	            for (int i = 0; i < BOARD_SIZE; i++) {
	                if (board[i][i] == symbol) {
	                    count++;
	                }
	            }
	            if (count == WINNING_CONDITION - 1) {
	                return true;
	            }
	        }

	        
	        if (row + col == BOARD_SIZE - 1) {
	            count = 0;
	            for (int i = 0; i < BOARD_SIZE; i++) {
	                if (board[i][BOARD_SIZE - 1 - i] == symbol) {
	                    count++;
	                }
	            }
	            if (count == WINNING_CONDITION - 1) {
	                return true;
	            }
	        }

	        return false;
	    }

	    private boolean checkGameEnd(char symbol) {
	        
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            int rowCount = 0;
	            int colCount = 0;
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                if (board[i][j] == symbol) {
	                    rowCount++;
	                }
	                if (board[j][i] == symbol) {
	                    colCount++;
	                }
	            }
	            if (rowCount == WINNING_CONDITION || colCount == WINNING_CONDITION) {
	                return true;
	            }
	        }

	      
	        int diagCount = 0;
	        int antiDiagCount = 0;
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            if (board[i][i] == symbol) {
	                diagCount++;
	            }
	            if (board[i][BOARD_SIZE - 1 - i] == symbol) {
	                antiDiagCount++;
	            }
	        }
	          if (diagCount == WINNING_CONDITION || antiDiagCount == WINNING_CONDITION) {
	            return true;
	        }

	        return false;
	    }

	    private boolean isBoardFull() {
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                if (board[i][j] == EMPTY) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }

	    private void displayBoard() {
	        for (int i = 0; i < BOARD_SIZE; i++) {
	            for (int j = 0; j < BOARD_SIZE; j++) {
	                System.out.print(board[i][j] + " ");
	            }
	            System.out.println();
	        }
	    }

	    private void displayResult() {
	        System.out.println();
	        if (result.equals("Gano")) {
	            System.out.println("¡Felicidades, " + playerName + "! Ganaste.");
	        } else if (result.equals("Perdio")) {
	            System.out.println("Lo siento, " + playerName + ". Perdiste.");
	        } else {
	            System.out.println("¡Es un empate!");
	        }
	    }

	    private void saveGameResult() {
	        String fileName = "src/resources/history_game.txt";
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
	            String dateTime = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	            String line = playerName + ";" + result + ";" + PLAYER_SYMBOL + ";" + dateTime;
	            writer.write(line);
	            writer.newLine();
	            writer.flush();
	            System.out.println("Resultado de la partida guardado en " + fileName);
	        } catch (IOException e) {
	            System.out.println("Error al guardar el resultado de la partida.");
	        }
	    }

	    public static void main(String[] args) {
	        Game game = new Game();
	        game.play();
	    }
	}



