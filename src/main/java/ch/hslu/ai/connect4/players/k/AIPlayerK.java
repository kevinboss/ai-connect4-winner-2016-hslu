package ch.hslu.ai.connect4.players.k;

import ch.hslu.ai.connect4.Player;
import ch.hslu.ai.connect4.players.k.common.Connect4GameStateHelper;
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
    private final String filename = "617861577_knowledge_base.con4kb";
    private final int learningDepth;

    public AIPlayerK(String name) {
        this(name, false, 5);
    }

    public AIPlayerK(String name, boolean learningMode, int learningDepth) {
        super(name);
        this.learningDepth = learningDepth;
        this.connect4MiniMax = new Connect4MiniMax(1, new Connect4HeuristicCalculator());
        this.connect4KnowledgeBase = new Connect4KnowledgeBase(999999);
        this.connect4KnowledgeBase.deSerialize(this.filename);
        this.learningMode = learningMode;
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
                bestMove = getBestMove(connect4GameState);
            }
            if (this.learningMode) {
                explainTurn(bestMove, connect4GameState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestMove;
    }

    private void explainTurn(int bestMove, Connect4GameState connect4GameState) {
        final Connect4GameState connect4GameStateAfterBestMove
                = Connect4GameStateHelper.placeInColumn(connect4GameState, 1, bestMove);
        if (connect4GameStateAfterBestMove != null) {
            this.connect4KnowledgeBase.explainTurn(
                    connect4GameState,
                    new Connect4Turn(bestMove),
                    connect4GameStateAfterBestMove
            );
        }
    }

    private int getBestMove(Connect4GameState connect4GameState) throws java.util.concurrent.ExecutionException, InterruptedException {
        int depth = 5;
        if (this.learningMode) {
            depth = this.learningDepth;
        }
        final Connect4GameState bestMoveNode = this.connect4MiniMax.getBestMove(connect4GameState, depth, true);
        return ((Connect4Turn) bestMoveNode.getTurn()).getColumn();
    }

    public void saveWhatYouKnow() {
        if (this.learningMode) {
            this.connect4KnowledgeBase.serialize(this.filename);
        }
    }
}
