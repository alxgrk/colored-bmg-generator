package de.uni.leipzig.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@NoArgsConstructor
@AllArgsConstructor
public class AdjacencyList implements List<List<Node>> {

    @Delegate
    private List<List<Node>> adjList = new ArrayList<>();

}
