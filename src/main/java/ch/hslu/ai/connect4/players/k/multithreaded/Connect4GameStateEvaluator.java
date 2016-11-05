package ch.hslu.ai.connect4.players.k.multithreaded;

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
        int columns = board.length;
        int rows = board[0].length;
        for (int i = 0; i < columns; i++) {
            int counter = 0;
            for (int j = 0; j < rows && counter < 4; j++) {
                if (board[i][j] == symbol) {
                    counter++;
                } else {
                    counter = 0;
                }
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFourInRow(int[][] board, int symbol) {
        int columns = board.length;
        int rows = board[0].length;
        for (int i = 0; i < rows; i++) {
            int counter = 0;
            for (int j = 0; j < columns && counter < 4; j++) {
                if (board[j][i] == symbol) {
                    counter++;
                } else {
                    counter = 0;
                }
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFourInDiagonal(int[][] board, int symbol) {
        int columns = board.length;
        int rows = board[0].length;

        for (int i = 0; i <= columns - 4; i++) {
            for (int j = 0; j <= rows - 4; j++) {
                int[] cells = new int[]{board[i][j], board[i + 1][j + 1],
                        board[i + 2][j + 2], board[i + 3][j + 3]};
                if (equal(cells, symbol)) {
                    return true;
                }
            }
        }

        for (int i = columns - 1; i >= 3; i--) {
            for (int j = 0; j <= rows - 4; j++) {
                int[] cells = new int[]{board[i][j], board[i - 1][j + 1],
                        board[i - 2][j + 2], board[i - 3][j + 3]};
                if (equal(cells, symbol)) {
                    return true;
                }
            }
        }

        return false;
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
