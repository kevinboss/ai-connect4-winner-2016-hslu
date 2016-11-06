package ch.hslu.ai.connect4.players.k.multithreaded.heuristic;

import ch.hslu.ai.connect4.players.k.multithreaded.common.BaseNode;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public abstract class GenericHeuristicCalculator<NodeT extends BaseNode> {
    public abstract int getNodePayoff(NodeT node);
}
