package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.*;
import lombok.*;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class DefaultTriple implements Triple, Comparable<Triple> {

    @NonNull
    @NonFinal
    AbstractPair<Node> edge;

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
        int edgeComp = getEdge().compareTo(o.getEdge());
        if (edgeComp == 0) {
            return getNode().compareTo(o.getNode());
        } else
            return edgeComp;
    }

}
