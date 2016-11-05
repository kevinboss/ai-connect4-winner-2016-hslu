package ch.hslu.ai.connect4.players.k.multithreaded;

import java.io.Serializable;

/**
 * Created by Kevin Boss on 02.11.2016.
 */
public abstract class BaseNode implements Serializable {
    private static final long serialVersionUID = 3487495895819392L;

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
