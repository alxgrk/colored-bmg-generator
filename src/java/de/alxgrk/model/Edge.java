package de.alxgrk.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Edge {

    Node first;

    Node second;

    public static Edge of(String first, String second) {
        return new Edge(Node.of(first), Node.of(second));
    }

    @Override
    public String toString() {
        return first + "," + second;
    }

}
