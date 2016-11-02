package ch.hslu.ai.connect4.players.k.multithreaded;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericSingleThreadedMiniMax<NodeT> {

    private final int threadAmount;
    ExecutorService executor = Executors.newCachedThreadPool();

    public GenericSingleThreadedMiniMax(int threadAmount) {
        this.threadAmount = threadAmount;
    }

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract int getHeuristicNodeValue(final NodeT node);

    protected abstract List<NodeT> getNodeChildren(final NodeT node, final boolean maximizingPlayer);

    public NodeT getBestMove(final NodeT node,
                             final int depth) {
        int bestMovePayoff = 0;
        NodeT chosenMove = null;
        final List<NodeT> possibleMoves = getNodeChildren(node, true);
        for (NodeT possibleMove : possibleMoves) {
            final int result = alphabeta(possibleMove, depth, null, null, false);
            if (chosenMove == null || result > bestMovePayoff) {
                bestMovePayoff = result;
                chosenMove = possibleMove;
            }
        }
        return chosenMove;
    }

    private int alphabeta(final NodeT node,
                          final int depth,
                          Integer alpha,
                          Integer beta,
                          final boolean maximizingPlayer) {
        if (depth == 0 || isTerminalNode(node)) {
            return getHeuristicNodeValue(node);
        } else {
            if (maximizingPlayer) {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                for(NodeT child : children) {
                    int tmp = alphabeta(child, depth - 1, alpha, beta, false);
                    alpha = alpha == null ? tmp : Math.max(alpha, tmp);
                    if (beta != null && beta <= alpha) {
                        break;
                    }
                }
                return alpha;
            } else {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                for(NodeT child : children) {
                    int tmp = alphabeta(child, depth - 1, alpha, beta, true);
                    beta = beta == null ? tmp : Math.min(beta, tmp);
                    if (alpha != null && beta <= alpha) {
                        break;
                    }
                }
                return beta;
            }
        }
    }
}
