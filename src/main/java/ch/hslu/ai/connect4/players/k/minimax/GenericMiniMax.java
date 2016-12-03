package ch.hslu.ai.connect4.players.k.minimax;

import ch.hslu.ai.connect4.players.k.common.BaseNode;
import ch.hslu.ai.connect4.players.k.heuristic.GenericHeuristicCalculator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericMiniMax<NodeT extends BaseNode> {

    private final int threadAmount;
    private final GenericHeuristicCalculator<NodeT> heuristicCalculator;
    ExecutorService executor;

    private final Cache<Integer, Integer> payoffCache = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .build();

    public GenericMiniMax(int threadAmount, GenericHeuristicCalculator<NodeT> heuristicCalculator) {
        this.threadAmount = threadAmount;
        this.heuristicCalculator = heuristicCalculator;
        this.executor = Executors.newCachedThreadPool();
    }

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract List<NodeT> getNodeChildren(final NodeT node, final boolean maximizingPlayer);

    private class Key {
        private int boardIdentifier;
        private int depth;

        private Key(int boardIdentifier, int depth) {
            this.boardIdentifier = boardIdentifier;
            this.depth = depth;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31).
                    append(boardIdentifier).
                    append(depth).
                    toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof GenericMiniMax.Key))
                return false;
            if (obj == this)
                return true;

            Key rhs = (Key) obj;
            return new EqualsBuilder().
                    append(boardIdentifier, rhs.boardIdentifier).
                    append(depth, rhs.depth).
                    isEquals();
        }
    }

    public NodeT getBestMove(final NodeT node,
                             final int depth, boolean evaluatePossibleMovedIndependently) throws ExecutionException, InterruptedException {
        int bestMovePayoff = 0;
        NodeT chosenMove = null;
        final List<NodeT> possibleMoves = getNodeChildren(node, true);

        if (evaluatePossibleMovedIndependently) {
            List<CompletableFuture<Optional<MoveWithPayoff>>> resultFutures
                    = new ArrayList<>(possibleMoves.size());
            for (final NodeT possibleMove : possibleMoves) {
                CompletableFuture<Optional<MoveWithPayoff>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        final int result = alphabetaCached(possibleMove, depth, null, null, false);
                        return Optional.of(new MoveWithPayoff(possibleMove, result));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Optional.empty();
                });
                resultFutures.add(future);
            }
            List<MoveWithPayoff> results = new ArrayList<>();
            for (CompletableFuture<Optional<MoveWithPayoff>> resultFuture : resultFutures) {
                final Optional<MoveWithPayoff> moveWithPayoff = resultFuture.get();
                moveWithPayoff.ifPresent(results::add);
            }

            for (MoveWithPayoff result : results) {
                if (chosenMove == null || result.getPayoff() > bestMovePayoff) {
                    bestMovePayoff = result.getPayoff();
                    chosenMove = result.getMove();
                }
            }
        } else {
            for (NodeT possibleMove : possibleMoves) {
                final int result = alphabetaCached(possibleMove, depth, null, null, false);
                if (chosenMove == null || result > bestMovePayoff) {
                    bestMovePayoff = result;
                    chosenMove = possibleMove;
                }
            }
        }
        return chosenMove;
    }


    private int alphabetaCached(final NodeT node,
                                final int depth,
                                final Integer alpha,
                                final Integer beta,
                                final boolean maximizingPlayer) throws ExecutionException, InterruptedException {
        return payoffCache.get(new Key(node.hashCode(), depth).hashCode(), new Callable<Integer>() {
            public Integer call() throws Exception {
                return alphabeta(node, depth, alpha, beta, maximizingPlayer);
            }
        });
    }

    private int alphabeta(final NodeT node,
                          final int depth,
                          Integer alpha,
                          Integer beta,
                          final boolean maximizingPlayer) throws ExecutionException, InterruptedException {
        if (depth == 0 || isTerminalNode(node)) {
            return this.heuristicCalculator.getNodePayoff(node);
        } else {
            if (maximizingPlayer) {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                for (NodeT child : children) {
                    int tmp = alphabeta(child, depth - 1, alpha, beta, false);
                    alpha = alpha == null ? tmp : Math.max(alpha, tmp);

                    if (beta != null && beta <= alpha) {
                        break;
                    }
                }
                return alpha;
            } else {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                for (NodeT child : children) {
                    int tmp = alphabeta(child, depth - 1, alpha, beta, true);
                    beta = beta == null ? tmp : Math.min(beta, tmp);

                    if (alpha != null && beta <= alpha) {
                        break;
                    }
                }
                return beta;
            }
        }
    }

    private class MoveWithPayoff {
        private NodeT move;
        private int payoff;

        private MoveWithPayoff(NodeT move, int payoff) {
            this.move = move;
            this.payoff = payoff;
        }

        public NodeT getMove() {
            return move;
        }

        public int getPayoff() {
            return payoff;
        }
    }
}
