package ch.hslu.ai.connect4.players.k.multithreaded.common;

import com.google.common.primitives.Ints;

/**
 * Created by Kevin Boss on 03.11.2016.
 */
public class Connect4GameStateEvaluator {
    public boolean isInitialStateForMe(Connect4GameState gameState) {
        int[][] board = gameState.getBoard();
        if (!Ints.contains(Ints.concat(board), 1)) {
            return true;
        }
        return false;
    }

    public boolean isInitialStateForOpponent(Connect4GameState gameState) {
        int[][] board = gameState.getBoard();
        if (!Ints.contains(Ints.concat(board), 2)) {
            return true;
        }
        return false;
    }

    public boolean isGameDrawn(Connect4GameState gameState) {
        int[][] board = gameState.getBoard();
        int columns = board.length;
        int rows = board[0].length;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean didAnyoneWin(Connect4GameState gameState) {
        return didIWin(gameState) || didOpponentWin(gameState);
    }

    public boolean didIWin(Connect4GameState gameState) {
        int[][] board = gameState.getBoard();
        return hasFourInColumn(board, 1)
                || hasFourInDiagonal(board, 1)
                || hasFourInRow(board, 1);
    }

    public boolean didOpponentWin(Connect4GameState gameState) {
        int[][] board = gameState.getBoard();
        return hasFourInColumn(board, 2)
                || hasFourInDiagonal(board, 2)
                || hasFourInRow(board, 2);
    }

    private boolean hasFourInColumn(int[][] board, int symbol) {
        if (countXInColumn(board, symbol, 4, true) > 0) {
            return true;
        }
        return false;
    }

    private boolean hasFourInRow(int[][] board, int symbol) {
        if (countXInRow(board, symbol, 4, true) > 0) {
            return true;
        }
        return false;
    }

    private boolean hasFourInDiagonal(int[][] board, int symbol) {
        if (countXInDiagonal(board, symbol, 4, true) > 0) {
            return true;
        }
        return false;
    }

    public int countXInColumn(int[][] board, int symbol, int xInAColumn, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        int inAColumn = 0;
        for (int i = 0; i < columns; i++) {
            int counter = 0;
            for (int j = 0; j < rows && counter < xInAColumn; j++) {
                if (board[i][j] == symbol) {
                    counter++;
                } else {
                    counter = 0;
                }
            }
            if (counter == xInAColumn) {
                inAColumn++;
                if (returnOnFirst) {
                    return inAColumn;
                }
            }
        }
        return inAColumn;
    }

    public int countXInRow(int[][] board, int symbol, int xInARow, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        int inARow = 0;
        for (int i = 0; i < rows; i++) {
            int counter = 0;
            for (int j = 0; j < columns && counter < xInARow; j++) {
                if (board[j][i] == symbol) {
                    counter++;
                } else {
                    counter = 0;
                }
            }
            if (counter == xInARow) {
                inARow++;
                if (returnOnFirst) {
                    return inARow;
                }
            }
        }
        return inARow;
    }

    public int countXInDiagonal(int[][] board, int symbol, int xInARow, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        int inADiagonal = 0;
        for (int i = 0; i <= columns - xInARow; i++) {
            for (int j = 0; j <= rows - xInARow; j++) {
                int[] cells = new int[xInARow];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i + c][j + c];
                }
                if (equal(cells, symbol)) {
                    if (returnOnFirst) {
                        return inADiagonal;
                    }
                }
            }
        }

        for (int i = columns - 1; i >= xInARow - 1; i--) {
            for (int j = 0; j <= rows - xInARow; j++) {
                int[] cells = new int[xInARow];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i - c][j + c];
                }
                if (equal(cells, symbol)) {
                    if (returnOnFirst) {
                        return inADiagonal;
                    }
                }
            }
        }

        return inADiagonal;
    }

    public int countX(int[][] board, int symbol) {
        int columns = board.length;
        int rows = board[0].length;
        int inASingle = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; i++) {
                if (board[i][j] == symbol) {
                    inASingle++;
                }
            }
        }
        return inASingle;
    }

    private boolean equal(int[] array, int symbol) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != symbol) {
                return false;
            }
        }
        return true;
    }
}
