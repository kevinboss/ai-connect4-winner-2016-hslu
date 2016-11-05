package ch.hslu.ai.connect4.players.k.multithreaded;

import ch.hslu.ai.connect4.Player;
import ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase.Connect4KnowledgeBase;
import ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase.Connect4KnowledgeBaseTurn;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public class AIPlayerK extends Player {
    private final Connect4MiniMax connect4MiniMax;
    private final boolean learningMode;
    private final Connect4KnowledgeBase connect4KnowledgeBase;
    private final String filename = "knowledgebase.con4";

    /**
     * Constructor:
     *
     * @param name The name of this player.
     */
    public AIPlayerK(String name) {
        super(name);
        this.connect4MiniMax = new Connect4MiniMax(1);
        this.connect4KnowledgeBase = new Connect4KnowledgeBase(999999);
        this.connect4KnowledgeBase.deSerialize(this.filename);
        this.learningMode = true;
    }

    @Override
    public int play(char[][] board) {
        int bestMove = 0;
        try {
            final Connect4GameState connect4GameState = new Connect4GameState(board, this.getSymbol());
            final Connect4KnowledgeBaseTurn kbTurn = this.connect4KnowledgeBase.getKbTurn(connect4GameState);
            if (kbTurn != null) {
                bestMove = kbTurn.getColumn();
            } else {
                final Connect4GameState bestMoveNode = this.connect4MiniMax.getBestMove(connect4GameState, 5, true);
                bestMove = bestMoveNode.getColumn();
                if (this.learningMode) {
                    this.connect4KnowledgeBase.explainTurn(
                            connect4GameState,
                            new Connect4KnowledgeBaseTurn(bestMove),
                            bestMoveNode
                    );
                    this.connect4KnowledgeBase.serialize(this.filename);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestMove;
    }
}
