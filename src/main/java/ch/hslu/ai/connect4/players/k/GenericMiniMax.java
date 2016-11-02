package ch.hslu.ai.connect4.players.k;

import java.util.List;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericMiniMax<NodeT> {

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract int getHeuristicNodeValue(final NodeT node);

    protected abstract List<NodeT> getNodeChildren(final NodeT node);

    public int alphabeta(final NodeT node,
                         final int depth,
                         final int alpha,
                         final int beta,
                         final boolean maximizingPlayer) {
        if (depth == 0 || isTerminalNode(node)) {
            return getHeuristicNodeValue(node);
        } else {
            if (maximizingPlayer) {
                final List<NodeT> nodeChildren = getNodeChildren(node);

            } else {

            }
        }
        return 0;
    }
}
