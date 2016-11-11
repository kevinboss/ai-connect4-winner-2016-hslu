package ch.hslu.ai.connect4;

import ch.hslu.ai.connect4.players.k.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.heuristic.Connect4HeuristicCalculator;

/**
 * Created by kevin_emgquz4 on 04.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        final Connect4HeuristicCalculator connect4HeuristicCalculator = new Connect4HeuristicCalculator();
        final int a = connect4HeuristicCalculator.getNodePayoff(new Connect4GameState(
                new int[][]{
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1}
                }
                , 'a'));
        final int b = connect4HeuristicCalculator.getNodePayoff(new Connect4GameState(
                new int[][]{
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 1, 0, 0},
                        new int[]{0, 0, 1, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0}
                }
                , 'a'));
        final int c = connect4HeuristicCalculator.getNodePayoff(new Connect4GameState(
                new int[][]{
                        new int[]{0, 0, 1, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0}
                }
                , 'a'));
    }
}
