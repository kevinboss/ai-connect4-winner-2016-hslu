package ch.hslu.ai.connect4.players;

import ch.hslu.ai.connect4.Player;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class FirstRowPlayer extends Player {
    /**
     * Constructor:
     *
     * @param name The name of this player.
     */
    public FirstRowPlayer(String name) {
        super(name);
    }

    int row = 0;
    public int play(char[][] board) {
        row = (row + 1) % 6;
        return row;
    }
}
