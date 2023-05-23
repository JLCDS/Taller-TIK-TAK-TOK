package game;

public class TicTacToe {
    public static final char PLAYER_SYMBOL = 'X';
    public static final char COMPUTER_SYMBOL = 'O';
    public static final char EMPTY_SYMBOL = ' ';

    private char[][] board;
    private char currentPlayerSymbol;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayerSymbol = PLAYER_SYMBOL;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = EMPTY_SYMBOL;
            }
        }
    }

    public boolean makeMove(int row, int col, char symbol) {
        if (isCellEmpty(row, col)) {
            board[row][col] = symbol;
            return true;
        } else {
            return false;
        }
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == EMPTY_SYMBOL;
    }

    public boolean hasWinningCombination(char symbol) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }

        return false;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY_SYMBOL) {
                    return false;
                }
            }
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayerSymbol = (currentPlayerSymbol == PLAYER_SYMBOL) ? COMPUTER_SYMBOL : PLAYER_SYMBOL;
    }

    public char getCurrentPlayerSymbol() {
        return currentPlayerSymbol;
    }

    public void clearCell(int row, int col) {
        board[row][col] = EMPTY_SYMBOL;
    }

	
}


