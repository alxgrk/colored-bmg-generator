package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.Edge;
import lombok.Value;

@Value
public class Triple {

    Edge edge;

    Node node;

    @Override
    public String toString() {
        return "(" + edge + "|" + node + ")";
    }

}
