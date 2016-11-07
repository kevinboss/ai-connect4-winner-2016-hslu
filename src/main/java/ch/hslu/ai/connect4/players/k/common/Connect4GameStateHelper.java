package ch.hslu.ai.connect4.players.k.common;

/**
 * Created by kevin_emgquz4 on 07.11.2016.
 */
public class Connect4GameStateHelper {
    public static Connect4GameState placeInColumn(Connect4GameState node, int player, int i) {
        final int[] column = node.getBoard()[i];
        for (int j = 0; j < column.length; j++) {
            if (column[j] != 0 && j - 1 >= 0 && column[j - 1] == 0) {
                final int[][] board = copyOf(node.getBoard());
                return new Connect4GameState(board, node.getMySymbol()).setField(i, j - 1, player);
            } else if (column[j] == 0 && j == column.length - 1) {
                final int[][] board = copyOf(node.getBoard());
                return new Connect4GameState(board, node.getMySymbol()).setField(i, j, player);
            }
        }
        return null;
    }

    public static int[][] copyOf(int[][] oldArray) {
        int[][] myInt = new int[oldArray.length][];
        for (int i = 0; i < oldArray.length; i++)
            myInt[i] = oldArray[i].clone();
        return myInt;
    }
}
