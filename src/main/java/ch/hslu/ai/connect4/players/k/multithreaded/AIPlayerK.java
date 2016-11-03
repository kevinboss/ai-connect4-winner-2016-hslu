package ch.hslu.ai.connect4.players.k.multithreaded;

import ch.hslu.ai.connect4.Player;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class AIPlayerK extends Player {
    private final Connect4MiniMax connect4MiniMax;

    /**
     * Constructor:
     *
     * @param name The name of this player.
     */
    public AIPlayerK(String name) {
        super(name);
        int cores = Runtime.getRuntime().availableProcessors();
        this.connect4MiniMax = new Connect4MiniMax(cores);
    }

    @Override
    public int play(char[][] board) {
        final Connect4GameState bestMove;
        try {
            bestMove = this.connect4MiniMax.getBestMove(new Connect4GameState(board, this.getSymbol()), 5);
            return bestMove.getColumn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
