package ch.hslu.ai.connect4.players.k.multithreaded.knowledgebase;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by kevin_emgquz4 on 04.11.2016.
 */
public class Connect4KnowledgeBaseTurn extends BaseTurn {
    private final int column;

    public Connect4KnowledgeBaseTurn(int column) {
        this.column = column;
    }
    public int abstractHashCode() {
        return new HashCodeBuilder(17, 31).
                append(column).
                toHashCode();
    }

    public boolean abstractEquals(BaseTurn obj) {
        if (!(obj instanceof Connect4KnowledgeBaseTurn))
            return false;
        if (obj == this)
            return true;

        Connect4KnowledgeBaseTurn rhs = (Connect4KnowledgeBaseTurn) obj;
        return new EqualsBuilder().
                append(column, rhs.column).
                isEquals();
    }

    public int getColumn() {
        return column;
    }
}
