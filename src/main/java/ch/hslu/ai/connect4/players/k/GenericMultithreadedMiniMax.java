package ch.hslu.ai.connect4.players.k;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
abstract class GenericMultithreadedMiniMax<NodeT> {

    private final int threadAmount;
    ExecutorService executor = Executors.newCachedThreadPool();

    public GenericMultithreadedMiniMax(int threadAmount) {
        this.threadAmount = threadAmount;
    }

    protected abstract boolean isTerminalNode(final NodeT node);

    protected abstract int getHeuristicNodeValue(final NodeT node);

    protected abstract List<NodeT> getNodeChildren(final NodeT node, final boolean maximizingPlayer);

    public int alphabeta(final NodeT node,
                         final int depth,
                         final int alpha,
                         final int beta,
                         final boolean maximizingPlayer) {
        if (depth == 0 || isTerminalNode(node)) {
            return getHeuristicNodeValue(node);
        } else {
            if (maximizingPlayer) {
                return goDeeper(node, depth, alpha, beta, false, new GoDeeperVariations() {
                    public int getCorrectValue(List<Integer> list) {
                        return Collections.max(list);
                    }

                    public int selectAlphaOrBeta(int alpha, int beta, boolean selectOpposite) {
                        if (selectOpposite) return beta;
                        return alpha;
                    }
                });
            } else {
                return goDeeper(node, depth, alpha, beta, true, new GoDeeperVariations() {
                    public int getCorrectValue(List<Integer> list) {
                        return Collections.min(list);
                    }

                    public int selectAlphaOrBeta(int alpha, int beta, boolean selectOpposite) {
                        if (selectOpposite) return alpha;
                        return beta;
                    }
                });
            }
        }
    }

    private int goDeeper(NodeT node, final int depth, final int alpha, final int beta,
                         final boolean maximizingPlayer
            , final GoDeeperVariations goDeeperVariations) {
        int result = goDeeperVariations.selectAlphaOrBeta(alpha, beta, false);
        final List<NodeT> nodeChildren = getNodeChildren(node, maximizingPlayer);
        final List<List<NodeT>> partitions = Lists.partition(nodeChildren, threadAmount);
        for (final List<NodeT> partition : partitions) {
            List<Future<Integer>> resultFutures = new ArrayList<Future<Integer>>();
            for (final NodeT partitionNode : partition) {
                resultFutures.add(executor.submit(new Callable<Integer>() {
                    public Integer call() throws Exception {
                        return alphabeta(
                                partitionNode,
                                depth - 1,
                                alpha,
                                beta,
                                false);
                    }
                }));
            }
            List<Integer> candidates = new ArrayList<Integer>(resultFutures.size() + 1);
            candidates.add(goDeeperVariations.selectAlphaOrBeta(alpha, beta, false));
            for (Future<Integer> resultFuture : resultFutures) {
                try {
                    candidates.add(resultFuture.get());
                } catch (InterruptedException e) {
                    //Well, forget the tree
                } catch (ExecutionException e) {
                    //Well, forget the tree
                }
            }
            result = goDeeperVariations.getCorrectValue(candidates);
            if (goDeeperVariations.selectAlphaOrBeta(alpha, beta, true) <= result) {
                break;
            }
        }
        return result;
    }

    private interface GoDeeperVariations {
        int getCorrectValue(List<Integer> list);

        int selectAlphaOrBeta(int alpha, int beta, boolean selectOpposite);
    }
}
