package ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase;

import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4GameStateEvaluator;
import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4Turn;

/**
 * Created by Kevin Boss on 04.11.2016.
 */
public class Connect4KnowledgeBase extends GenericKnowledgeBase<Connect4GameState, Connect4Turn> {
    private Connect4GameStateEvaluator connect4GameStateEvaluator = new Connect4GameStateEvaluator();

    public Connect4KnowledgeBase(int knowledgeEntriesToKeep) {
        super(knowledgeEntriesToKeep);
    }

    public boolean isInitialStateForMe(Connect4GameState node) {
        return this.connect4GameStateEvaluator.isInitialStateForMe(node);
    }

    public boolean didIWin(Connect4GameState node) {
        return this.connect4GameStateEvaluator.didIWin(node);
    }
}
