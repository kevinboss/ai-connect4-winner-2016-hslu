package ch.hslu.ai.connect4.players.k.multithreaded.minimax;

import ch.hslu.ai.connect4.players.k.multithreaded.BaseNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericMiniMax<NodeT extends BaseNode> {

    private final int threadAmount;
    ExecutorService executor;

    private final Cache<Integer, Integer> payoffCache = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .build();

    public GenericMiniMax(int threadAmount) {
        this.threadAmount = threadAmount;
        this.executor = Executors.newCachedThreadPool();
    }

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract int getHeuristicNodeValue(final NodeT node);

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
            List<Future<MoveWithPayoff>> resultFutures = new ArrayList<Future<MoveWithPayoff>>(possibleMoves.size());
            for (final NodeT possibleMove : possibleMoves) {
                resultFutures.add(executor.submit(new Callable<MoveWithPayoff>() {
                    public MoveWithPayoff call() throws Exception {
                        final int result = alphabetaCached(possibleMove, depth, null, null, false);
                        return new MoveWithPayoff(possibleMove, result);
                    }
                }));
            }
            List<MoveWithPayoff> results = new ArrayList<MoveWithPayoff>();
            for (Future<MoveWithPayoff> resultFuture : resultFutures) {
                results.add(resultFuture.get());
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
            return getHeuristicNodeValue(node);
        } else {
            if (maximizingPlayer) {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                final List<List<NodeT>> childrenPartitions = Lists.partition(children, this.threadAmount);
                for (List<NodeT> childrenPartition : childrenPartitions) {
                    if (childrenPartition.size() > 1) {
                        List<Future<Integer>> tmpFutures = new ArrayList<Future<Integer>>();
                        for (final NodeT child : childrenPartition) {
                            final Integer finalNestedAlpha = alpha;
                            final Integer finalNestedBeta = beta;
                            tmpFutures.add(executor.submit(new Callable<Integer>() {
                                public Integer call() throws Exception {
                                    return alphabetaCached(child, depth - 1, finalNestedAlpha, finalNestedBeta, false);
                                }
                            }));
                        }
                        List<Integer> candidates = new ArrayList<Integer>();
                        if (alpha != null) candidates.add(alpha);
                        for (Future<Integer> tmpFuture : tmpFutures) {
                            candidates.add(tmpFuture.get());
                        }
                        alpha = Collections.max(candidates);
                    } else if (childrenPartition.size() > 0) {
                        int tmp = alphabeta(childrenPartition.get(0), depth - 1, alpha, beta, false);
                        alpha = alpha == null ? tmp : Math.max(alpha, tmp);
                    }
                    if (beta != null && beta <= alpha) {
                        break;
                    }
                }
                return alpha;
            } else {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                final List<List<NodeT>> childrenPartitions = Lists.partition(children, this.threadAmount);
                for (List<NodeT> childrenPartition : childrenPartitions) {
                    if (childrenPartition.size() > 1) {
                        List<Future<Integer>> tmpFutures = new ArrayList<Future<Integer>>();
                        for (final NodeT child : childrenPartition) {
                            final Integer finalNestedAlpha = alpha;
                            final Integer finalNestedBeta = beta;
                            tmpFutures.add(executor.submit(new Callable<Integer>() {
                                public Integer call() throws Exception {
                                    return alphabetaCached(child, depth - 1, finalNestedAlpha, finalNestedBeta, true);
                                }
                            }));
                        }
                        List<Integer> candidates = new ArrayList<Integer>();
                        if (beta != null) candidates.add(beta);
                        for (Future<Integer> tmpFuture : tmpFutures) {
                            candidates.add(tmpFuture.get());
                        }
                        beta = Collections.min(candidates);
                    } else if (childrenPartition.size() > 0) {
                        int tmp = alphabeta(childrenPartition.get(0), depth - 1, alpha, beta, true);
                        beta = beta == null ? tmp : Math.min(beta, tmp);
                    }
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
