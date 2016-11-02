package ch.hslu.ai.connect4.players.k;

import ch.hslu.ai.connect4.Player;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class AIPlayerK extends Player {
    /**
     * Constructor:
     *
     * @param name The name of this player.
     */
    public AIPlayerK(String name) {
        super(name);
    }

    @Override
    public int play(char[][] board) {
        int column = 0;
        while (board[column][0] != '-') {
            column++;
        }
        return column;
    }
}