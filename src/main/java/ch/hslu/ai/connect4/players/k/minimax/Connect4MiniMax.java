package ch.hslu.ai.connect4.players.k.minimax;

import ch.hslu.ai.connect4.players.k.common.Connect4GameStateEvaluator;
import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
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

    private int[][] copyOf(int[][] oldArray) {
        int[][] myInt = new int[oldArray.length][];
        for (int i = 0; i < oldArray.length; i++)
            myInt[i] = oldArray[i].clone();
        return myInt;
    }

    protected List<Connect4GameState> getNodeChildren(Connect4GameState node, boolean maximizingPlayer) {
        List<Connect4GameState> children = new ArrayList<Connect4GameState>();
        int player = maximizingPlayer ? 1 : 2;
        for (int i = 0; i < node.getBoard().length; i++) {
            final int[] column = node.getBoard()[i];
            for (int j = 0; j < column.length; j++) {
                if (column[j] != 0 && j - 1 >= 0 && column[j - 1] == 0) {
                    final int[][] board = copyOf(node.getBoard());
                    children.add(new Connect4GameState(board, node.getMySymbol()).setField(i, j - 1, player));
                    j = column.length;
                } else if (column[j] == 0 && j == column.length - 1) {
                    final int[][] board = copyOf(node.getBoard());
                    children.add(new Connect4GameState(board, node.getMySymbol()).setField(i, j, player));
                }
            }
        }
        return children;
    }
}
