package ch.hslu.ai.connect4.players.k.multithreaded.common;

import java.io.Serializable;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public abstract class BaseNode<TurnT extends BaseTurn> implements Serializable {
    private static final long serialVersionUID = 3487495895819392L;
    private TurnT turn;

    public BaseNode(TurnT turn) {
        this.turn = turn;
    }

    public TurnT getTurn() {
        return this.turn;
    }

    protected void setTurn(TurnT turn) {
        this.turn = turn;
    }

    @Override
    public final int hashCode() {
        return abstractHashCode();
    }

    public abstract int abstractHashCode();

    @Override
    public final boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof BaseNode) {
            return abstractEquals((BaseNode) other);
        }
        return false;
    }

    public abstract boolean abstractEquals(BaseNode other);


}
