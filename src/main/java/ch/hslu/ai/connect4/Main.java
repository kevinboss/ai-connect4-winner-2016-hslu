package ch.hslu.ai.connect4;

import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4GameState;
import ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase.Connect4KnowledgeBase;
import ch.hslu.ai.connect4.players.k.multithreaded.common.Connect4Turn;

/**
 * Created by kevin_emgquz4 on 04.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        Connect4KnowledgeBase kb = new Connect4KnowledgeBase(999999);
        playGame1(kb);
        playGame1(kb);
        playGame2(kb);
        final Connect4Turn kbturn = kb.getKbTurn(new Connect4GameState(
                new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0}
                }, 'b'));
    }

    private static void playGame1(Connect4KnowledgeBase kb) {
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0}
                        }, 'b'),
                new Connect4Turn(1),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 2}
                        }, 'b'),
                new Connect4Turn(2),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 2}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 2}
                        }, 'b'),
                new Connect4Turn(3),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 2}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 2, 2}
                        }, 'b'),
                new Connect4Turn(4),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 2, 2}
                }, 'b'));
    }

    private static void playGame2(Connect4KnowledgeBase kb) {
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0}
                        }, 'b'),
                new Connect4Turn(1),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 2}
                        }, 'b'),
                new Connect4Turn(3),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 2}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 2}
                        }, 'b'),
                new Connect4Turn(2),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 2}
                }, 'b'));
        kb.explainTurn(new Connect4GameState(
                        new int[][]{
                                new int[]{0, 0, 0, 0, 0, 2},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 1},
                                new int[]{0, 0, 0, 0, 0, 0},
                                new int[]{0, 0, 0, 0, 2, 2},
                                new int[]{0, 0, 0, 0, 0, 2}
                        }, 'b'),
                new Connect4Turn(4),
                new Connect4GameState(new int[][]{
                        new int[]{0, 0, 0, 0, 0, 2},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 2, 2},
                        new int[]{0, 0, 0, 0, 0, 2}
                }, 'b'));
    }
}
