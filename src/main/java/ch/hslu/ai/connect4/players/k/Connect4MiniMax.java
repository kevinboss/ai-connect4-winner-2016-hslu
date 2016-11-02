package ch.hslu.ai.connect4.players.k;

import java.util.List;

/**
 * Created by kevin_emgquz4 on 02.11.2016.
 */
public class Connect4MiniMax extends GenericMultithreadedMiniMax<Connect4GameState> {
    public Connect4MiniMax(int threadAmount) {
        super(threadAmount);
    }

    protected boolean isTerminalNode(Connect4GameState node) {
        for (int space : node.getBoardOneDimensional()) {
            if (space == 0) {
                return false;
            }
            return true;
        }
    }

    protected int getHeuristicNodeValue(Connect4GameState node) {
        return 1;
    }

    protected List<Connect4GameState> getNodeChildren(Connect4GameState node, boolean maximizingPlayer) {
        
    }
}
