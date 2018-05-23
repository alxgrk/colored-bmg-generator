package de.uni.leipzig.model;

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
