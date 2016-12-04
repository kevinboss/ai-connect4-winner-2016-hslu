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
    private Map<Integer, List<KnowledgeEntry>> knowledgeEntries;

    public GenericKnowledgeBase(int knowledgeEntriesToKeep) {
        this.knowledgeEntriesToKeep = knowledgeEntriesToKeep;
        this.knowledgeEntries = new HashMap<Integer, List<KnowledgeEntry>>();
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
        } catch (FileNotFoundException e) {
        } catch (IOException i) {
        }
    }

    /**
     * @param filename The name of the knowledgebase file. Has to be within the classpath,
     *                 e.g. inside the jar or provided by the classpath-argument when starting the program.
     */
    public void deSerialize(String filename) {
        Map<Integer, List<KnowledgeEntry>> e = null;
        try (
                InputStream stream = getClass().getClassLoader().getResourceAsStream(filename)
        ) {
            if (stream != null) {
                ObjectInputStream in = new ObjectInputStream(stream);
                e = (Map<Integer, List<KnowledgeEntry>>) in.readObject();
            } else {
                System.out.printf("KB with filename [%s] could not be found or opened.\n", filename);
            }
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
        final List<KnowledgeEntry> knowledgeEntries = this.knowledgeEntries.get(node.hashCode());
        if (knowledgeEntries != null) {
            for (KnowledgeEntry knowledgeEntry : knowledgeEntries) {
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

    private void addKnowledgeEntries(NodeT finalNode) {
        for (Map.Entry<NodeT, TurnT> turnEntry : this.runningMatch.getTurns().entrySet()) {
            boolean turnExistedAlready = false;
            final int code = turnEntry.getKey().hashCode();
            List<KnowledgeEntry> knowledgeEntries = this.knowledgeEntries.get(code);
            if (knowledgeEntries == null) {
                knowledgeEntries = new ArrayList<KnowledgeEntry>();
                this.knowledgeEntries.put(code, knowledgeEntries);
            }
            for (KnowledgeEntry knowledgeEntry : knowledgeEntries) {
                if (knowledgeEntry.getTurn().hashCode() == turnEntry.getValue().hashCode()) {
                    if (!knowledgeEntry.hasFinalNode(finalNode)) {
                        knowledgeEntry.addFinalNode(finalNode);
                    }
                    turnExistedAlready = true;
                }
            }
            if (!turnExistedAlready) {
                knowledgeEntries.add(new KnowledgeEntry(
                        turnEntry.getKey(),
                        turnEntry.getValue(),
                        finalNode));
            }
        }
    }

    private void cleanUpKnowledgeEntries() {
        //TODO: If this becomes really necessary clean up by removing Knowledge Entries with the lowest value
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
