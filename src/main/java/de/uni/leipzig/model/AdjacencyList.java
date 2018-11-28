package de.uni.leipzig.model;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;

import lombok.*;
import lombok.experimental.Delegate;

@NoArgsConstructor
public class AdjacencyList implements List<List<Node>> {

    @Getter
    @Setter
    private Set<Node> childNodes = new HashSet<>();

    @Delegate
    private List<List<Node>> adjList = new ArrayList<>();

    @VisibleForTesting
    public AdjacencyList(List<List<Node>> adjList) {
        this.adjList = adjList;
        childNodes = adjList.stream()
                .filter(l -> l.size() == 1)
                .map(l -> l.get(0))
                .collect(Collectors.toSet());
    }

}
