package ch.hslu.ai.connect4;

import ch.hslu.ai.connect4.players.RandomPlayer;
import ch.hslu.ai.connect4.players.k.AIPlayerK;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Main class with connect-4 single game and tournament mode.
 *
 * @author Marc Pouly
 */

public class Connect4 {

    /**
     * Game board dimensions:
     */

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    /**
     * Main method:
     *
     * @param args standard input parameters (not used)
     */

    public static void main(String[] args) {

        final AIPlayerK kevin = new AIPlayerK("Kevin", false, 8);
        //final AIPlayerK hans = new AIPlayerK("Hans", false, 8);
        // Create players:
        Player[] players = {
                //new FirstRowPlayer("FR"),
                new RandomPlayer("Random"),
                kevin,
                //hans,
                //new FirstColumnPlayer("FirstColumn"),
        };

        if (true) {
            for (int i = 0; i < 1; i++) {
                singleGameMode(players[0], players[1]);
            }
        } else {
            long startTime = System.currentTimeMillis();
            tournamentMode(players, 1000);
            kevin.saveWhatYouKnow();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(elapsedTime);
        }
    }

    /**
     * Starts a single game with GUI. Remember that Connect-4 has a first mover advantage.
     *
     * @param player1 The first mover
     * @param player2 The second mover
     */

    public static void singleGameMode(Player player1, Player player2) {
        Game game = new Game(ROWS, COLUMNS, player1, player2);
        GameBoard board = new GameBoard(game, 500, player1, player2);
        board.startGame();
    }

    /**
     * Start blind tournament mode with many rounds
     * One round consists of two matches such that each player has the first move in one match
     * This is important because Connect-4 has a first mover advantage.
     *
     * @param players All players participating in the tournament
     * @param rounds  The number of rounds (= 2 * number of matches) to be played
     */

    public static void tournamentMode(Player[] players, int rounds) {

        HashMap<Player, Integer> ranking = new HashMap<Player, Integer>();
        for (Player p : players) {
            ranking.put(p, 0);
        }

		/*
         * Start Tournament:
		 */

        System.out.println("***********************************************");
        System.out.println("* Tournament started ...");
        System.out.println("***********************************************\n");

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players.length; j++) {

                if (i != j) {
                    // Let Player i play Player j
                    Tournament tournament = new Tournament(ROWS, COLUMNS, players[i], players[j]);
                    tournament.play(rounds);

                    // Add number of wins of player 1
                    int v1 = ranking.get(players[i]) + tournament.getWinsOfPlayer1();
                    ranking.put(players[i], v1);

                    // Add number of wins of player 2
                    int v2 = ranking.get(players[j]) + tournament.getWinsOfPlayer2();
                    ranking.put(players[j], v2);

                    System.out.println(players[i].getName() + " vs. " + players[j].getName() + ": "
                            + tournament.getWinsOfPlayer1() + " - "
                            + tournament.getWinsOfPlayer2() + " (draws: " + tournament.getDraws() + ")");

                }
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("***********************************************");
        System.out.println("* Final Ranking ...");
        System.out.println("***********************************************\n");

		/*
         * Produce Ranking:
		 */

        TreeSet<Integer> points = new TreeSet<Integer>(ranking.values());
        Iterator<Integer> iter = points.descendingIterator();

        int rank = 1;

        while (iter.hasNext()) {

            int p = iter.next();

            System.out.println("Rank " + rank + " with " + p + " points:");
            for (Player player : ranking.keySet()) {
                if (ranking.get(player) == p) {
                    System.out.println("\t- " + player.getName());
                }
            }

            System.out.println();

            rank++;
        }

    }
}
