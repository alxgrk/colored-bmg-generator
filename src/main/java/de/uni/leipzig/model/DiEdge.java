package de.uni.leipzig.model;

public class DiEdge extends Edge {

    public DiEdge(Node from, Node to) {
        super(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Edge other = (Edge) obj;

        if (!first.equals(other.first))
            return false;
        if (!second.equals(other.second))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + first.hashCode();
        result = prime * result + second.hashCode();
        return result;
    }

}
