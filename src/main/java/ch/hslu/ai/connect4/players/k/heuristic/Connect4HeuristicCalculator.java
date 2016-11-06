package ch.hslu.ai.connect4.players.k.heuristic;

import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.common.Connect4GameStateEvaluator;

import java.util.List;

/**
 * Created by Kevin Boss on 05.11.2016.
 */
public class Connect4HeuristicCalculator extends GenericHeuristicCalculator<Connect4GameState> {
    private final Connect4GameStateEvaluator connect4GameStateEvaluator;
    private static final int baseValueSingle = 1;
    private static final int baseValueDouble = 42; //1*42
    private static final int baseValueTripe = 924; //=42*21+1*42
    private static final int baseValueQuadruple = 13860; //=924*14+42*21+1*42
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    public Connect4HeuristicCalculator() {
        this.connect4GameStateEvaluator = new Connect4GameStateEvaluator();
    }

    public int getNodePayoff(Connect4GameState node) {
        int payoff = 0;
        if (this.connect4GameStateEvaluator.didIWin(node)) {
            return baseValueQuadruple;
        } else if (this.connect4GameStateEvaluator.didOpponentWin(node)) {
            return -baseValueQuadruple;
        } else {
            payoff += singlePayoff(node);
            payoff += doublePayoff(node);
            payoff += triplePayoff(node);
        }
        return payoff;
    }

    private int singlePayoff(Connect4GameState node) {
        int payoff = 0;
        payoff += this.connect4GameStateEvaluator.countFields(node.getBoard(), 1) * this.baseValueSingle;
        payoff -= this.connect4GameStateEvaluator.countFields(node.getBoard(), 2) * this.baseValueSingle;
        return payoff;
    }

    private int doublePayoff(Connect4GameState node) {
        int payoff = 0;
        payoff += this.connect4GameStateEvaluator.count2InRow(node, 1).size() * this.baseValueDouble;
        payoff -= this.connect4GameStateEvaluator.count2InRow(node, 2).size() * this.baseValueDouble;
        payoff += this.connect4GameStateEvaluator.count2InColumn(node, 1).size() * this.baseValueDouble;
        payoff -= this.connect4GameStateEvaluator.count2InColumn(node, 2).size() * this.baseValueDouble;
        payoff += this.connect4GameStateEvaluator.count2InDiagonal(node, 1).size() * this.baseValueDouble;
        payoff -= this.connect4GameStateEvaluator.count2InDiagonal(node, 2).size() * this.baseValueDouble;
        return payoff;
    }

    private int triplePayoff(Connect4GameState node) {
        int payoff = 0;
        payoff += this.connect4GameStateEvaluator.count3InRow(node, 1).size() * this.baseValueTripe;
        payoff -= this.connect4GameStateEvaluator.count3InRow(node, 2).size() * this.baseValueTripe;
        payoff += this.connect4GameStateEvaluator.count3InColumn(node, 1).size() * this.baseValueTripe;
        payoff -= this.connect4GameStateEvaluator.count3InColumn(node, 2).size() * this.baseValueTripe;
        payoff += this.connect4GameStateEvaluator.count3InDiagonal(node, 1).size() * this.baseValueTripe;
        payoff -= this.connect4GameStateEvaluator.count3InDiagonal(node, 2).size() * this.baseValueTripe;
        return payoff;
    }

    private boolean isImmediateInRow(Connect4GameState node, List<Connect4GameStateEvaluator.Field> fields) {
        if (fields.get(0).getX() < fields.get(1).getX()) {
            int potentialX = fields.get(0).getX() - 1;
            int potentialY = fields.get(0).getY() - 1;
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY() - 1;
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        }
        return false;
    }

    private boolean isImmediateInDiagonal(Connect4GameState node, List<Connect4GameStateEvaluator.Field> fields) {
        int size = fields.size();
        if (fields.get(0).getX() < fields.get(1).getX()) {
            int potentialX = fields.get(0).getX() - 1;
            int potentialY = fields.get(0).getY();
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY();
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        }
        if (fields.get(size - 1).getX() < fields.get(size - 2).getX()) {
            int potentialX = fields.get(size - 1).getX() - 1;
            int potentialY = fields.get(size - 1).getY() - 2;
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        } else {
            int potentialX = fields.get(0).getX() + 1;
            int potentialY = fields.get(0).getY() - 2;
            if (potentialX >= 0 && potentialX <= COLUMNS
                    && potentialY >= 0 && potentialY <= ROWS) {
                return node.getBoard()[potentialX][potentialY] != 0;
            }
        }
        return false;
    }
}
