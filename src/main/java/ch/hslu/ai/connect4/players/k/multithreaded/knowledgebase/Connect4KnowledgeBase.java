package ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase;

import ch.hslu.ai.connect4.players.k.multithreaded.Connect4GameState;
import ch.hslu.ai.connect4.players.k.multithreaded.Connect4GameStateEvaluator;

/**
 * Created by Kevin Boss on 04.11.2016.
 */
public class Connect4KnowledgeBase extends GenericKnowledgeBase<Connect4GameState, Connect4KnowledgeBaseTurn> {
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
