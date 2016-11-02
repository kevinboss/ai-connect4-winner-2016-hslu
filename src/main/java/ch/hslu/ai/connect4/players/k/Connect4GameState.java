package ch.hslu.ai.connect4.players.k;

import com.google.common.primitives.Ints;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class Connect4GameState {
    private char mySymbol;

    public Connect4GameState(char mySymbol) {
        this.mySymbol = mySymbol;
    }

    public int[][] board;

    public Connect4GameState(char[][] board) {
        this.board = new int[board.length][board[0].length];

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
}
