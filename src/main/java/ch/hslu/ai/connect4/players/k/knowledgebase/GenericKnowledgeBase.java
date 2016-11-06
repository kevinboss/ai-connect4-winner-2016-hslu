package ch.hslu.ai.connect4.players.k.knowledgebase;

import ch.hslu.ai.connect4.players.k.common.BaseNode;
import ch.hslu.ai.connect4.players.k.common.BaseTurn;

import java.io.*;
import java.util.*;

/**
 * Created by Kevin Boss on 03.11.2016.
 */
public abstract class GenericKnowledgeBase<NodeT extends BaseNode, TurnT extends BaseTurn> {
    private final int knowledgeEntriesToKeep;
    private Match runningMatch = new Match();
    private List<KnowledgeEntry> knowledgeEntries;

    public GenericKnowledgeBase(int knowledgeEntriesToKeep) {
        this.knowledgeEntriesToKeep = knowledgeEntriesToKeep;
        this.knowledgeEntries = new ArrayList<KnowledgeEntry>();
    }

    public abstract boolean isInitialStateForMe(NodeT node);

    public abstract boolean didIWin(NodeT node);

    public void serialize(String filepath) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.knowledgeEntries);
            out.close();
            fileOut.close();
            //System.out.println("Saved kb file.");
        } catch (FileNotFoundException e) {
        } catch (IOException i) {
        }
    }

    public void deSerialize(String filepath) {
        List<KnowledgeEntry> e = null;
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (List<KnowledgeEntry>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("No kb file existing.");
        } catch (ClassNotFoundException c) {
            System.out.println("Problem loading kb file.");
        }
        if (e != null) {
            this.knowledgeEntries = e;
        }
    }

    public TurnT getKbTurn(NodeT node) {
        TurnT bestTurn = null;
        int bestTurnValue = 0;
        for (KnowledgeEntry knowledgeEntry : this.knowledgeEntries) {
            if (knowledgeEntry.getNode().hashCode() == node.hashCode()) {
                if (knowledgeEntry.getValue() > bestTurnValue) {
                    bestTurn = (TurnT) knowledgeEntry.getTurn();
                    bestTurnValue = knowledgeEntry.getValue();
                }
            }
        }
        return bestTurn;
    }

    public void explainTurn(NodeT node, TurnT turn, NodeT nodeAfterTurn) {
        if (this.isInitialStateForMe(node)) {
            runningMatch.begin(node, turn);
        } else if (this.didIWin(nodeAfterTurn)) {
            runningMatch.intermediateTurn(node, turn);
            addKnowledgeEntries(nodeAfterTurn);
            cleanUpKnowledgeEntries();
        } else {
            runningMatch.intermediateTurn(node, turn);
        }
    }

    private void addKnowledgeEntries(NodeT nodeAfterTurn) {
        for (Map.Entry<NodeT, TurnT> turnEntry : this.runningMatch.getTurns().entrySet()) {
            boolean turnExistedAlready = false;
            for (KnowledgeEntry knowledgeEntry : this.knowledgeEntries) {
                if (knowledgeEntry.getNode().hashCode() == turnEntry.getKey().hashCode()
                        && knowledgeEntry.getTurn().hashCode() == turnEntry.getValue().hashCode()) {
                    if (!knowledgeEntry.hasFinalNode(nodeAfterTurn)) {
                        knowledgeEntry.addFinalNode(nodeAfterTurn);
                    }
                    turnExistedAlready = true;
                }
            }
            if (!turnExistedAlready) {
                knowledgeEntries.add(new KnowledgeEntry(
                        turnEntry.getKey(),
                        turnEntry.getValue(),
                        nodeAfterTurn));
            }
        }
    }

    private void cleanUpKnowledgeEntries() {
        Collections.sort(this.knowledgeEntries, new Comparator<KnowledgeEntry>() {
            public int compare(KnowledgeEntry o1, KnowledgeEntry o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        if (this.knowledgeEntries.size() > this.knowledgeEntriesToKeep + 1) {
            this.knowledgeEntries = new ArrayList<KnowledgeEntry>(this.knowledgeEntries.subList(0, this.knowledgeEntriesToKeep));
        }
    }

    private class Match {
        private Map<NodeT, TurnT> turns = new HashMap<NodeT, TurnT>();

        public void begin(NodeT initialGameStateForMatch, TurnT turn) {
            this.turns.clear();
            this.turns.put(initialGameStateForMatch, turn);
        }

        public void intermediateTurn(NodeT gameState, TurnT turn) {
            this.turns.put(gameState, turn);
        }

        public Map<NodeT, TurnT> getTurns() {
            return this.turns;
        }
    }
}
