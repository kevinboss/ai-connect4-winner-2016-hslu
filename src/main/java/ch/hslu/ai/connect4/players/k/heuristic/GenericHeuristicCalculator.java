package ch.hslu.ai.connect4.players.k.heuristic;

import ch.hslu.ai.connect4.players.k.common.BaseNode;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public interface GenericHeuristicCalculator<NodeT extends BaseNode> {
    int getNodePayoff(NodeT node);
}
