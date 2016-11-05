package ch.hslu.ai.connect4.players.k.multithreaded;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Kevin Boss on 04.11.2016.
 */
public class Connect4Turn extends BaseTurn {
    private final int column;

    public Connect4Turn(int column) {
        this.column = column;
    }
    public int abstractHashCode() {
        return new HashCodeBuilder(17, 31).
                append(column).
                toHashCode();
    }

    public boolean abstractEquals(BaseTurn obj) {
        if (!(obj instanceof Connect4Turn))
            return false;
        if (obj == this)
            return true;

        Connect4Turn rhs = (Connect4Turn) obj;
        return new EqualsBuilder().
                append(column, rhs.column).
                isEquals();
    }

    public int getColumn() {
        return column;
    }
}
