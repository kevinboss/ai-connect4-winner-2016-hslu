package ch.hslu.ai.connect4.players.k.common;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Boss on 03.11.2016.
 */
public class Connect4GameStateEvaluator {
    private static final int[][] doublePermutations1 = new int[][]{
            new int[]{0, 0, 1, 1},
            new int[]{0, 1, 0, 1},
            new int[]{1, 0, 0, 1},
            new int[]{1, 0, 1, 0},
            new int[]{1, 1, 0, 0},
            new int[]{0, 1, 1, 0}
    };
    private static final int[][] tripePermutations1 = new int[][]{
            new int[]{0, 1, 1, 1},
            new int[]{1, 0, 1, 1},
            new int[]{1, 1, 0, 1},
            new int[]{1, 1, 1, 0}
    };
    private static final int[][] doublePermutations2 = new int[][]{
            new int[]{0, 0, 2, 2},
            new int[]{0, 2, 0, 2},
            new int[]{2, 0, 0, 2},
            new int[]{2, 0, 2, 0},
            new int[]{2, 2, 0, 0},
            new int[]{0, 2, 2, 0}
    };
    private static final int[][] tripePermutations2 = new int[][]{
            new int[]{0, 2, 2, 2},
            new int[]{2, 0, 2, 2},
            new int[]{2, 2, 0, 2},
            new int[]{2, 2, 2, 0}
    };

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
        return hasFour(gameState.getBoard(), 1);
    }

    public boolean didOpponentWin(Connect4GameState gameState) {
        return hasFour(gameState.getBoard(), 2);
    }

    private boolean hasFour(int[][] board, int symbol) {
        final List<int[]> allGroups = getAllGroupsInColumn(board);
        allGroups.addAll(getAllGroupsInRow(board));
        allGroups.addAll(getAllGroupsInDiagonal(board));
        for (int[] group : allGroups) {
            if (equal(group, new int[][]{
                    new int[]{symbol, symbol, symbol, symbol}
            })) {
                return true;
            }
        }
        return false;
    }

    public List<Field> countFields(int[][] board, int symbol) {
        int columns = board.length;
        int rows = board[0].length;
        List<Field> inASingle = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j] == symbol) {
                    inASingle.add(new Field(i, j));
                }
            }
        }
        return inASingle;
    }

    public CombinedResult fieldsInColumn(int[][] board, int symbol) {
        final List<int[]> allGroupsInColumn = getAllGroupsInColumn(board);
        final int tuplesInARow = countEqualGroups(allGroupsInColumn,
                symbol == 1 ? doublePermutations1 : doublePermutations2);
        final int triplesInARow = countEqualGroups(allGroupsInColumn,
                symbol == 1 ? tripePermutations1 : tripePermutations2);
        return new CombinedResult(tuplesInARow, triplesInARow);
    }

    private List<int[]> getAllGroupsInColumn(int[][] board) {
        int columns = board.length;
        int rows = board[0].length;
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows - 3; j++) {
                int[] cells = new int[4];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i][j + c];
                }
                result.add(cells);
            }
        }
        return result;
    }

    public CombinedResult fieldsInRow(int[][] board, int symbol) {
        final List<int[]> allGroupsInRow = getAllGroupsInRow(board);
        final int tuplesInARow = countEqualGroups(allGroupsInRow,
                symbol == 1 ? doublePermutations1 : doublePermutations2);
        final int triplesInARow = countEqualGroups(allGroupsInRow,
                symbol == 1 ? tripePermutations1 : tripePermutations2);
        return new CombinedResult(tuplesInARow, triplesInARow);
    }

    private List<int[]> getAllGroupsInRow(int[][] board) {
        int columns = board.length;
        int rows = board[0].length;
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 0; j < rows; j++) {
                int[] cells = new int[4];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i + c][j];
                }
                result.add(cells);
            }
        }
        return result;
    }

    public CombinedResult fieldsInDiagonal(int[][] board, int symbol) {
        final List<int[]> allGroupsInDiagonal = getAllGroupsInDiagonal(board);
        final int doubleInADiagonal = countEqualGroups(allGroupsInDiagonal,
                symbol == 1 ? doublePermutations1 : doublePermutations2);
        final int tripeInADiagonal = countEqualGroups(allGroupsInDiagonal,
                symbol == 1 ? tripePermutations1 : tripePermutations2);
        return new CombinedResult(doubleInADiagonal, tripeInADiagonal);
    }

    private List<int[]> getAllGroupsInDiagonal(int[][] board) {
        int columns = board.length;
        int rows = board[0].length;
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= columns - 4; i++) {
            for (int j = 0; j <= rows - 4; j++) {
                int[] cells = new int[4];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i + c][j + c];
                }
                result.add(cells);
            }
        }

        for (int i = columns - 1; i >= 4 - 1; i--) {
            for (int j = 0; j <= rows - 4; j++) {
                int[] cells = new int[4];
                for (int c = 0; c < 4; c++) {
                    cells[c] = board[i - c][j + c];
                }
                result.add(cells);
            }
        }
        return result;
    }

    private int countEqualGroups(List<int[]> groupsInDiagonal, int[][] permutations) {
        int inADiagonal = 0;
        for (int[] group : groupsInDiagonal) {
            if (equal(group, permutations)) {
                inADiagonal++;
            }
        }
        return inADiagonal;
    }

    private boolean equal(int[] array, int[][] permutations) {
        for (int[] permutation : permutations) {
            if (array.length == permutation.length) {
                int counter = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == permutation[i]) {
                        counter++;
                    } else {
                        i = array.length;
                    }
                }
                if (counter == array.length) {
                    return true;
                }
            }
        }
        return false;
    }

    public class CombinedResult {
        private int tuples;
        private int triples;

        public CombinedResult(int tuples, int triples) {
            this.tuples = tuples;
            this.triples = triples;
        }

        public int getTripe() {
            return triples;
        }

        public int getDouble() {
            return tuples;
        }
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
