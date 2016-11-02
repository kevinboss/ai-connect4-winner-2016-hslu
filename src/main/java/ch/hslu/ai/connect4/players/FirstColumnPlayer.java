package ch.hslu.ai.connect4.players;

import ch.hslu.ai.connect4.Player;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class FirstColumnPlayer extends Player {
    /**
     * Constructor:
     *
     * @param name The name of this player.
     */
    public FirstColumnPlayer(String name) {
        super(name);
    }

    public int play(char[][] board) {
        int column = 0;
        while (board[column][0] != '-') {
            column++;
        }
        return column;
    }
}
