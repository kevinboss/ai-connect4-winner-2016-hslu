package ch.hslu.ai.connect4.players.k.multithreaded.heuristic;

import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4GameStateEvaluator;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public class Connect4HeuristicCalculator extends GenericHeuristicCalculator<Connect4GameState> {
    private final Connect4GameStateEvaluator connect4GameStateEvaluator;

    public Connect4HeuristicCalculator() {
        this.connect4GameStateEvaluator = new Connect4GameStateEvaluator();
    }

    public int getNodePayoff(Connect4GameState node) {
        if (this.connect4GameStateEvaluator.didOpponentWin(node))
            return -1;
        if (this.connect4GameStateEvaluator.didIWin(node))
            return 1;
        return 0;
    }
}
