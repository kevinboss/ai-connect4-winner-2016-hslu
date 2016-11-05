package ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase;

import ch.hslu.ai.connect4.players.k.multithreaded.BaseNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public class KnowledgeEntry<NodeT extends BaseNode, TurnT extends BaseTurn> implements Serializable {
    private static final long serialVersionUID = 3487495895819393L;
    private NodeT node;
    private TurnT turn;
    private List<NodeT> finalNodes;
    private int value;

    public KnowledgeEntry(NodeT node, TurnT turn, NodeT finalNode) {
        this.node = node;
        this.turn = turn;
        this.finalNodes = new ArrayList<NodeT>();
        this.finalNodes.add(finalNode);
        this.value = 1;
    }

    public void addFinalNode(NodeT finalNode) {
        this.finalNodes.add(finalNode);
        this.value++;
    }

    public List<NodeT> getFinalNodes() {
        return finalNodes;
    }

    boolean hasFinalNode(NodeT finalNode) {
        for (NodeT nodeEntry : finalNodes) {
            if (nodeEntry.hashCode() == finalNode.hashCode()) {
                return true;
            }
        }
        return false;
    }

    public NodeT getNode() {
        return node;
    }

    public TurnT getTurn() {
        return turn;
    }

    public int getValue() {
        return this.value;
    }
}
