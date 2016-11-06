package ch.hslu.ai.connect4.players.k.common;

import java.io.Serializable;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public abstract class BaseTurn implements Serializable {
    private static final long serialVersionUID = 3487495895819391L;

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
        if (other instanceof BaseTurn) {
            return abstractEquals((BaseTurn)other);
        }
        return false;
    }

    public abstract boolean abstractEquals(BaseTurn other);


}
