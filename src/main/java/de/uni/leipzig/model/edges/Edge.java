package de.uni.leipzig.model.edges;

import de.uni.leipzig.model.Node;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@EqualsAndHashCode(callSuper = true)
public class Edge extends AbstractEdge<Node> {

    public Edge(Node first, Node second) {
        super(first, second);
    }

    @Override
    public String toString() {
        return getFirst() + "," + getSecond();
    }
}
