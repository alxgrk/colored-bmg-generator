package de.uni.leipzig.model.edges;

import java.util.Set;

import de.uni.leipzig.model.Node;

public class SetDiEdge extends AbstractEdge<Set<Node>> {

    public SetDiEdge(Set<Node> from, Set<Node> to) {
        super(from, to);
    }

    @Override
    public String toString() {
        return getFirst() + "->" + getSecond();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SetDiEdge other = (SetDiEdge) obj;

        if (!getFirst().equals(other.getFirst()))
            return false;
        if (!getSecond().equals(other.getSecond()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getFirst().hashCode();
        result = prime * result + getSecond().hashCode();
        return result;
    }

}
