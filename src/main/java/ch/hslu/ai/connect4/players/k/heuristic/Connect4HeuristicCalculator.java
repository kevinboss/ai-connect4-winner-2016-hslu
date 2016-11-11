package ch.hslu.ai.connect4.players.k.heuristic;

import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.common.Connect4GameStateEvaluator;

import java.util.List;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public class Connect4HeuristicCalculator extends GenericHeuristicCalculator<Connect4GameState> {
    private final Connect4GameStateEvaluator connect4GameStateEvaluator;
    private static final float baseValueSingle = 1;
    private static final int baseValueDouble = 84; //1*42*2
    private static final int baseValueTripe = 1848; //=42*21+1*42*2
    private static final int baseValueQuadruple = 27720; //=924*14+42*21+1*42*2

    public Connect4HeuristicCalculator() {
        this.connect4GameStateEvaluator = new Connect4GameStateEvaluator();
    }

    public int getNodePayoff(Connect4GameState node) {
        int columns = node.getBoard().length;
        int bestColumn = (columns - 1) * 10 / 20;
        int valueDouble = baseValueDouble * bestColumn;
        int valueTripe = baseValueTripe * bestColumn;

        int payoff = 0;
        if (this.connect4GameStateEvaluator.didIWin(node)) {
            return baseValueQuadruple * bestColumn;
        } else if (this.connect4GameStateEvaluator.didOpponentWin(node)) {
            return -baseValueQuadruple * bestColumn;
        } else {
            payoff += singlePayoff(node, bestColumn);
            payoff += doubleTriplePayoff(node, valueDouble, valueTripe);
        }
        return payoff;
    }

    private int singlePayoff(Connect4GameState node, int bestColumn) {
        int payoff = 0;
        final float baseValueSingle = this.baseValueSingle * bestColumn;
        final List<Connect4GameStateEvaluator.Field> myFields
                = this.connect4GameStateEvaluator.countFields(node.getBoard(), 1);
        for (Connect4GameStateEvaluator.Field field : myFields) {
            if (bestColumn != field.getX()) {
                int delta = Math.abs(field.getX() - bestColumn);
                payoff += baseValueSingle / delta;
            } else {
                payoff += baseValueSingle;
            }
        }

        final List<Connect4GameStateEvaluator.Field> opponentFields
                = this.connect4GameStateEvaluator.countFields(node.getBoard(), 2);
        for (Connect4GameStateEvaluator.Field field : opponentFields) {
            if (bestColumn != field.getX()) {
                int delta = Math.abs(field.getX() - bestColumn);
                payoff -= baseValueSingle / delta;
            } else {
                payoff -= baseValueSingle;
            }
        }

        return payoff;
    }

    private int doubleTriplePayoff(Connect4GameState node, int valueDouble, int valueTripe) {
        final Connect4GameStateEvaluator.CombinedResult combinedResultColumn
                = this.connect4GameStateEvaluator.fieldsInColumn(node.getBoard(), 1);
        final Connect4GameStateEvaluator.CombinedResult combinedResultColumnOpponent
                = this.connect4GameStateEvaluator.fieldsInColumn(node.getBoard(), 2);
        final Connect4GameStateEvaluator.CombinedResult combinedResultRow
                = this.connect4GameStateEvaluator.fieldsInRow(node.getBoard(), 1);
        final Connect4GameStateEvaluator.CombinedResult combinedResultRowOpponent
                = this.connect4GameStateEvaluator.fieldsInRow(node.getBoard(), 2);
        final Connect4GameStateEvaluator.CombinedResult combinedResultDiagonal
                = this.connect4GameStateEvaluator.fieldsInDiagonal(node.getBoard(), 1);
        final Connect4GameStateEvaluator.CombinedResult combinedResultDiagonalOpponent
                = this.connect4GameStateEvaluator.fieldsInDiagonal(node.getBoard(), 2);
        int payoff = 0;

        payoff += combinedResultColumn.getDouble() * valueDouble;
        payoff += combinedResultColumn.getTripe() * valueTripe;
        payoff += combinedResultRow.getDouble() * valueDouble;
        payoff += combinedResultRow.getTripe() * valueTripe;
        payoff += combinedResultDiagonal.getDouble() * valueDouble;
        payoff += combinedResultDiagonal.getTripe() * valueTripe;

        payoff -= combinedResultColumnOpponent.getDouble() * valueDouble;
        payoff -= combinedResultColumnOpponent.getTripe() * valueTripe;
        payoff -= combinedResultRowOpponent.getDouble() * valueDouble;
        payoff -= combinedResultRowOpponent.getTripe() * valueTripe;
        payoff -= combinedResultDiagonalOpponent.getDouble() * valueDouble;
        payoff -= combinedResultDiagonalOpponent.getTripe() * valueTripe;

        return payoff;
    }
}
