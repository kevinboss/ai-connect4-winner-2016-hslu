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
    private int bestColumn;

    public Connect4HeuristicCalculator() {
        this.connect4GameStateEvaluator = new Connect4GameStateEvaluator();
    }

    public int getNodePayoff(Connect4GameState node) {
        int columns = node.getBoard().length;
        int rows = node.getBoard()[0].length;
        bestColumn = (columns - 1) * 10 / 20;
        int payoff = 0;
        if (this.connect4GameStateEvaluator.didIWin(node)) {
            return baseValueQuadruple * bestColumn;
        } else if (this.connect4GameStateEvaluator.didOpponentWin(node)) {
            return -baseValueQuadruple * bestColumn;
        } else {
            payoff += singlePayoff(node);
            payoff += doublePayoff(node, columns, rows);
            payoff += triplePayoff(node, columns, rows);
        }
        return payoff;
    }

    private int singlePayoff(Connect4GameState node) {
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
        payoff -= opponentFields.size() * this.baseValueSingle;
        return payoff;
    }

    private int doublePayoff(Connect4GameState node,
                             int columns, int rows) {
        int payoff = 0;
        final int baseValueDouble = this.baseValueDouble * bestColumn;
        java.util.function.Function<? super List<Connect4GameStateEvaluator.Field>, Integer> funcRow = (e) -> {
            if (isImmediateInRow(node, e, columns, rows)) {
                return baseValueDouble / 2;
            }
            return baseValueDouble;
        };
        java.util.function.Function<? super List<Connect4GameStateEvaluator.Field>, Integer> funcDiagonale = (e) -> {
            if (isImmediateInDiagonal(node, e, columns, rows)) {
                return baseValueDouble / 2;
            }
            return baseValueDouble;
        };
        payoff += this.connect4GameStateEvaluator.count2InRow(node, 1)
                .stream().map(funcRow).mapToInt(i -> i.intValue()).sum();
        payoff -= this.connect4GameStateEvaluator.count2InRow(node, 2)
                .stream().map(funcRow).mapToInt(i -> i.intValue()).sum();
        payoff += this.connect4GameStateEvaluator.count2InColumn(node, 1).size() * baseValueTripe;
        payoff -= this.connect4GameStateEvaluator.count2InColumn(node, 2).size() * baseValueTripe;
        payoff += this.connect4GameStateEvaluator.count2InDiagonal(node, 1)
                .stream().map(funcDiagonale).mapToInt(i -> i.intValue()).sum();
        payoff -= this.connect4GameStateEvaluator.count2InDiagonal(node, 2)
                .stream().map(funcDiagonale).mapToInt(i -> i.intValue()).sum();
        return payoff;
    }

    private int triplePayoff(final Connect4GameState node,
                             final int columns, final int rows) {
        int payoff = 0;
        final int baseValueTripe = this.baseValueTripe * bestColumn;
        java.util.function.Function<? super List<Connect4GameStateEvaluator.Field>, Integer> funcRow = (e) -> {
            if (isImmediateInRow(node, e, columns, rows)) {
                return baseValueTripe / 2;
            }
            return baseValueTripe;
        };
        java.util.function.Function<? super List<Connect4GameStateEvaluator.Field>, Integer> funcDiagonale = (e) -> {
            if (isImmediateInDiagonal(node, e, columns, rows)) {
                return baseValueTripe / 2;
            }
            return baseValueTripe;
        };
        payoff += this.connect4GameStateEvaluator.count3InRow(node, 1)
                .stream().map(funcRow).mapToInt(i -> i.intValue()).sum();
        payoff -= this.connect4GameStateEvaluator.count3InRow(node, 2)
                .stream().map(funcRow).mapToInt(i -> i.intValue()).sum();
        payoff += this.connect4GameStateEvaluator.count3InColumn(node, 1).size() * baseValueTripe;
        payoff -= this.connect4GameStateEvaluator.count3InColumn(node, 2).size() * baseValueTripe;
        payoff += this.connect4GameStateEvaluator.count3InDiagonal(node, 1)
                .stream().map(funcDiagonale).mapToInt(i -> i.intValue()).sum();
        payoff -= this.connect4GameStateEvaluator.count3InDiagonal(node, 2)
                .stream().map(funcDiagonale).mapToInt(i -> i.intValue()).sum();
        return payoff;
    }

    private boolean isImmediateInRow(Connect4GameState node, List<Connect4GameStateEvaluator.Field> fields,
                                     int columns, int rows) {
        boolean result = false;
        if (fields.get(0).getX() < fields.get(1).getX()) {
            int potentialX = fields.get(0).getX() - 1;
            int potentialY = fields.get(0).getY() - 1;
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY() - 1;
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0;
            }
        }
        return result;
    }

    private boolean isImmediateInDiagonal(Connect4GameState node, List<Connect4GameStateEvaluator.Field> fields,
                                          int rows, int columns) {
        boolean result = false;
        int size = fields.size();
        if (fields.get(0).getX() < fields.get(1).getX()) {
            int potentialX = fields.get(0).getX() - 1;
            int potentialY = fields.get(0).getY();
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY();
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0;
            }
        }
        if (fields.get(size - 1).getX() < fields.get(size - 2).getX()) {
            int potentialX = fields.get(size - 1).getX() - 1;
            int potentialY = fields.get(size - 1).getY() - 2;
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0 || result;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY() - 2;
            if (potentialX >= 0 && potentialX <= columns
                    && potentialY >= 0 && potentialY <= rows) {
                result = node.getBoard()[potentialX][potentialY] != 0 || result;
            }
        }
        return result;
    }
}
