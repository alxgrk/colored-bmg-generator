package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.Edge;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class DefaultTriple implements Triple, Comparable<Triple> {

    @NonNull
    @NonFinal
    Edge edge;

    @NonNull
    Node node;

    @Override
    public String toString() {
        return "(" + edge + "|" + node + ")";
    }

    public boolean contains(Node contained) {
        return contained.equals(edge.getFirst())
                || contained.equals(edge.getSecond())
                || contained.equals(node);
    }

    @Override
    public void invertEdge() {
        edge = new Edge(edge.getSecond(), edge.getFirst());
    }

    @Override
    public int compareTo(Triple o) {
        int firstNodeComp = getEdge().getFirst().compareTo(o.getEdge().getFirst());
        if (firstNodeComp == 0) {
            int secondNodeComp = getEdge().getSecond().compareTo(o.getEdge().getSecond());
            if (secondNodeComp == 0) {
                return getNode().compareTo(o.getNode());
            } else
                return secondNodeComp;
        } else
            return firstNodeComp;
    }

}
