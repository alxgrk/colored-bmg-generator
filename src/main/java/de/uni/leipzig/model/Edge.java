package de.uni.leipzig.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@AllArgsConstructor
@NonFinal
public class Edge {

    Node first;

    Node second;

    @Override
    public String toString() {
        return first + "," + second;
    }

}
