package ch.hslu.ai.connect4.players.k.common;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

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
        if (countXFieldsInColumn(board, symbol, 4, true).size() > 0) {
            return true;
        }
        return false;
    }

    private boolean hasFourInRow(int[][] board, int symbol) {
        if (countXFieldsInRow(board, symbol, 4, true).size() > 0) {
            return true;
        }
        return false;
    }

    private boolean hasFourInDiagonal(int[][] board, int symbol) {
        if (countXFieldsInDiagonal(board, symbol, 4, true).size() > 0) {
            return true;
        }
        return false;
    }

    private List<List<Field>> countXFieldsInColumn(int[][] board, int symbol, int xInAColumn, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        List<List<Field>> inAColumn = new ArrayList<List<Field>>();
        for (int i = 0; i < columns; i++) {
            int counter = 0;
            List<Field> potentialGroup = new ArrayList<Field>();
            for (int j = 0; j < rows && counter < xInAColumn; j++) {
                if (board[i][j] == symbol) {
                    counter++;
                    potentialGroup.add(new Field(i, j));
                } else {
                    counter = 0;
                }
            }
            if (counter == xInAColumn) {
                inAColumn.add(potentialGroup);
                if (returnOnFirst) {
                    return inAColumn;
                }
            }
        }
        return inAColumn;
    }

    private List<List<Field>> countXFieldsInRow(int[][] board, int symbol, int xInARow, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        List<List<Field>> inARow = new ArrayList<List<Field>>();
        for (int i = 0; i < rows; i++) {
            int counter = 0;
            List<Field> potentialGroup = new ArrayList<Field>();
            for (int j = 0; j < columns && counter < xInARow; j++) {
                if (board[j][i] == symbol) {
                    counter++;
                    potentialGroup.add(new Field(j, i));
                } else {
                    counter = 0;
                }
            }
            if (counter == xInARow) {
                inARow.add(potentialGroup);
                if (returnOnFirst) {
                    return inARow;
                }
            }
        }
        return inARow;
    }

    private List<List<Field>> countXFieldsInDiagonal(int[][] board, int symbol, int xInARow, boolean returnOnFirst) {
        int columns = board.length;
        int rows = board[0].length;
        List<List<Field>> inADiagonal = new ArrayList<List<Field>>();
        for (int i = 0; i <= columns - xInARow; i++) {
            for (int j = 0; j <= rows - xInARow; j++) {
                int[] cells = new int[xInARow];
                List<Field> potentialGroup = new ArrayList<Field>();
                for (int c = 0; c < xInARow; c++) {
                    cells[c] = board[i + c][j + c];
                    potentialGroup.add(new Field(i + c, j + c));
                }
                if (equal(cells, symbol)) {
                    inADiagonal.add(potentialGroup);
                    if (returnOnFirst) {
                        return inADiagonal;
                    }
                }
            }
        }

        for (int i = columns - 1; i >= xInARow - 1; i--) {
            for (int j = 0; j <= rows - xInARow; j++) {
                int[] cells = new int[xInARow];
                List<Field> potentialGroup = new ArrayList<Field>();
                for (int c = 0; c < xInARow; c++) {
                    cells[c] = board[i - c][j + c];
                    potentialGroup.add(new Field(i + c, j + c));
                }
                if (equal(cells, symbol)) {
                    inADiagonal.add(potentialGroup);
                    if (returnOnFirst) {
                        return inADiagonal;
                    }
                }
            }
        }
        return inADiagonal;
    }

    public List<Field> countFields(int[][] board, int symbol) {
        int columns = board.length;
        int rows = board[0].length;
        List<Field> inASingle = new ArrayList<Field>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j] == symbol) {
                    inASingle.add(new Field(i, j));
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

    public List<List<Field>> count2InRow(Connect4GameState node, int i) {
        return countXFieldsInRow(node.getBoard(), i, 2, false);
    }

    public List<List<Field>> count3InRow(Connect4GameState node, int i) {
        return countXFieldsInRow(node.getBoard(), i, 3, false);
    }

    public List<List<Field>> count2InColumn(Connect4GameState node, int i) {
        return countXFieldsInColumn(node.getBoard(), i, 2, false);
    }

    public List<List<Field>> count3InColumn(Connect4GameState node, int i) {
        return countXFieldsInColumn(node.getBoard(), i, 3, false);
    }

    public List<List<Field>> count2InDiagonal(Connect4GameState node, int i) {
        return countXFieldsInDiagonal(node.getBoard(), i, 2, false);
    }

    public List<List<Field>> count3InDiagonal(Connect4GameState node, int i) {
        return countXFieldsInDiagonal(node.getBoard(), i, 3, false);
    }

    public class Field {
        private final int x;
        private final int y;

        public Field(int x, int y) {

            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
