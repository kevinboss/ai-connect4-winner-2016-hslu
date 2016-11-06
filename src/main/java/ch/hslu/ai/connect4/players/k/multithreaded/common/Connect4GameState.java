package ch.hslu.ai.connect4.players.k.multithreaded.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public class Connect4GameState extends BaseNode {
    private final char mySymbol;
    private int[][] board;

    public int[][] getBoard() {
        return this.board;
    }

    public char getMySymbol() {
        return this.mySymbol;
    }

    public Connect4GameState setField(int column, int row, int value) {
        this.setTurn(new Connect4Turn(column));
        this.board[column][row] = value;
        return this;
    }

    public Connect4GameState(int[][] board, char mySymbol) {
        super(null);
        this.board = board;
        this.mySymbol = mySymbol;
    }

    public Connect4GameState(char[][] board, char mySymbol) {
        super(null);
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

    @Override
    public int abstractHashCode() {
        return new HashCodeBuilder(17, 31).
                append(board).
                toHashCode();
    }

    @Override
    public boolean abstractEquals(BaseNode obj) {
        if (!(obj instanceof Connect4GameState))
            return false;
        if (obj == this)
            return true;

        Connect4GameState rhs = (Connect4GameState) obj;
        return new EqualsBuilder().
                append(board, rhs.board).
                isEquals();
    }
}
