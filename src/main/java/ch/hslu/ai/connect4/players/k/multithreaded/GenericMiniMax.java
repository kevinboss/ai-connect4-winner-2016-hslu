package ch.hslu.ai.connect4.players.k.multithreaded;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericMiniMax<NodeT extends BaseNode> {

    private final int threadAmount;
    ExecutorService executor = Executors.newCachedThreadPool();

    private final Cache<Key, Integer> payoffCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();

    public GenericMiniMax(int threadAmount) {
        this.threadAmount = threadAmount;
    }

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract int getHeuristicNodeValue(final NodeT node);

    protected abstract List<NodeT> getNodeChildren(final NodeT node, final boolean maximizingPlayer);

    private class Key {
        private int boardHash;
        private int depth;

        private Key(int boardHash, int depth) {
            this.boardHash = boardHash;
            this.depth = depth;
        }
    }
    public NodeT getBestMove(final NodeT node,
                             final int depth) throws ExecutionException, InterruptedException {
        int bestMovePayoff = 0;
        NodeT chosenMove = null;
        final List<NodeT> possibleMoves = getNodeChildren(node, true);
        for (NodeT possibleMove : possibleMoves) {
            final int result = alphabetaCached(possibleMove, depth, null, null, false);
            if (chosenMove == null || result > bestMovePayoff) {
                bestMovePayoff = result;
                chosenMove = possibleMove;
            }
        }
        return chosenMove;
    }


    private int alphabetaCached(final NodeT node,
                                final int depth,
                                final Integer alpha,
                                final Integer beta,
                                final boolean maximizingPlayer) throws ExecutionException, InterruptedException {
        return payoffCache.get(new Key(node.getCacheHash(), depth), new Callable<Integer>() {
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
                    if (beta != null && beta <= alpha) {
                        break;
                    }
                }
                return alpha;
            } else {
                final List<NodeT> children = getNodeChildren(node, maximizingPlayer);
                final List<List<NodeT>> childrenPartitions = Lists.partition(children, this.threadAmount);
                for (List<NodeT> childrenPartition : childrenPartitions) {
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
                    if (alpha != null && beta <= alpha) {
                        break;
                    }
                }
                return beta;
            }
        }
    }
}
