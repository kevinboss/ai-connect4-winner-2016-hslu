package ch.hslu.ai.connect4.players.k.multithreaded;

import com.google.common.primitives.Ints;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class Connect4GameState {
    private final char mySymbol;

    private int[][] board;
    private int column;
    private int row;

    public int[][] getBoard () {
        return this.board;
    }

    public char getMySymbol() {
        return this.mySymbol;
    }

    public Connect4GameState setField(int column, int row, int value) {
        this.column = column;
        this.row = row;
        this.board[column][row] = value;
        return this;
    }

    public Connect4GameState(int[][] board, char mySymbol) {
        this.board = board;
        this.mySymbol = mySymbol;
    }

    public Connect4GameState(char[][] board, char mySymbol) {
        this.board = new int[board.length][board[0].length];
        this.mySymbol = mySymbol;

        for (int i = 0; i < board.length; i++) {
            final char[] column = board[i];
            for (int j = 0; j < column.length; j++) {
                if (column[j] == '-') {
                    this.board[i][j] = 0;
                } else if (column[j] == this.mySymbol) {
                    this.board[i][j] = 1;
                } else {
                    this.board[i][j] = 2;
                }
            }
        }
    }

    public int[] getBoardOneDimensional() {
        return Ints.concat(board);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
