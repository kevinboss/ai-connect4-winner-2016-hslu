package ch.hslu.ai.connect4.players.k.multithreaded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class Connect4MiniMax extends GenericMiniMax<Connect4GameState> {
    public Connect4MiniMax(int threadAmount) {
        super(threadAmount);
    }

    protected boolean isTerminalNode(Connect4GameState node) {
        return isGameDrawn(node.getBoard()) || didAnyoneWin(node.getBoard());
    }

    protected int getHeuristicNodeValue(Connect4GameState node) {
        if (didOpponentWin(node.getBoard()))
            return -1;
        if (didIWin(node.getBoard()))
            return 1;
        return 0;
    }

    private int[][] copyOf(int[][] oldArray) {
        int [][] myInt = new int[oldArray.length][];
        for(int i = 0; i < oldArray.length; i++)
            myInt[i] = oldArray[i].clone();
        return myInt;
    }

    protected List<Connect4GameState> getNodeChildren(Connect4GameState node, boolean maximizingPlayer) {
        List<Connect4GameState> children = new ArrayList<Connect4GameState>();
        int player = maximizingPlayer ? 1 : 2;
        for (int i = 0; i < node.getBoard().length; i++) {
            final int[] column = node.getBoard()[i];
            for (int j = 0; j < column.length; j++) {
                if (column[j] != 0 && j - 1 >= 0 && column[j - 1] == 0) {
                    final int[][] board = copyOf(node.getBoard());
                    children.add(new Connect4GameState(board, node.getMySymbol()).setField(i, j - 1, player));
                    j = column.length;
                } else if (column[j] == 0 && j == column.length - 1) {
                    final int[][] board = copyOf(node.getBoard());
                    children.add(new Connect4GameState(board, node.getMySymbol()).setField(i, j, player));
                }
            }
        }
        return children;
    }

    private boolean isGameDrawn(int[][] board) {
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

    private boolean didAnyoneWin(int[][] board) {
        return didIWin(board) || didOpponentWin(board);
    }

    private boolean didIWin(int[][] board) {
        return hasFourInColumn(board, 1)
                || hasFourInDiagonal(board, 1)
                || hasFourInRow(board, 1);
    }

    private boolean didOpponentWin(int[][] board) {
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
