package ch.hslu.ai.connect4.players.k;

import ch.hslu.ai.connect4.Player;
import ch.hslu.ai.connect4.players.k.common.Connect4Turn;
import ch.hslu.ai.connect4.players.k.heuristic.Connect4HeuristicCalculator;
import ch.hslu.ai.connect4.players.k.minimax.Connect4MiniMax;
import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.knowledgebase.Connect4KnowledgeBase;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public class AIPlayerK extends Player {
    private final Connect4MiniMax connect4MiniMax;
    private final boolean learningMode;
    private final Connect4KnowledgeBase connect4KnowledgeBase;
    private final String filename = "knowledgebase.con4kb";

    public AIPlayerK(String name) {
        super(name);
        this.connect4MiniMax = new Connect4MiniMax(1, new Connect4HeuristicCalculator());
        this.connect4KnowledgeBase = new Connect4KnowledgeBase(999999);
        this.connect4KnowledgeBase.deSerialize(this.filename);
        this.learningMode = false;
    }

    @Override
    public int play(char[][] board) {
        int bestMove = 0;
        try {
            final Connect4GameState connect4GameState = new Connect4GameState(board, this.getSymbol());
            final Connect4Turn kbTurn = this.connect4KnowledgeBase.getKbTurn(connect4GameState);
            if (kbTurn != null) {
                bestMove = kbTurn.getColumn();
            } else {
                int depth = 5;
                if (this.learningMode) {
                    depth = 10;
                }
                final Connect4GameState bestMoveNode = this.connect4MiniMax.getBestMove(connect4GameState, depth, true);
                bestMove = ((Connect4Turn) bestMoveNode.getTurn()).getColumn();
                if (this.learningMode) {
                    this.connect4KnowledgeBase.explainTurn(
                            connect4GameState,
                            new Connect4Turn(bestMove),
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