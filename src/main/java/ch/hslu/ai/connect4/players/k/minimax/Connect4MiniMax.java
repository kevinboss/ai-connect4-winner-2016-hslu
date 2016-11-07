package ch.hslu.ai.connect4.players.k.minimax;

import ch.hslu.ai.connect4.players.k.common.Connect4GameStateEvaluator;
import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.common.Connect4GameStateHelper;
import ch.hslu.ai.connect4.players.k.heuristic.GenericHeuristicCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public class Connect4MiniMax extends GenericMiniMax<Connect4GameState> {
    private Connect4GameStateEvaluator connect4GameStateEvaluator = new Connect4GameStateEvaluator();

    public Connect4MiniMax(int threadAmount, GenericHeuristicCalculator<Connect4GameState> heuristicCalculator) {
        super(threadAmount, heuristicCalculator);
    }

    protected boolean isTerminalNode(Connect4GameState node) {
        return this.connect4GameStateEvaluator.isGameDrawn(node)
                || this.connect4GameStateEvaluator.didAnyoneWin(node);
    }

    protected List<Connect4GameState> getNodeChildren(Connect4GameState node, boolean maximizingPlayer) {
        List<Connect4GameState> children = new ArrayList<Connect4GameState>();
        int player = maximizingPlayer ? 1 : 2;
        for (int i = 0; i < node.getBoard().length; i++) {
            final Connect4GameState connect4GameState = Connect4GameStateHelper.placeInColumn(node, player, i);
            if (connect4GameState != null)
                children.add(connect4GameState);
        }
        return children;
    }
}
