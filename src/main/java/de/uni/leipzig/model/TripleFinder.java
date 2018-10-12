package de.uni.leipzig.model;

import java.util.Set;

public interface TripleFinder<T extends Triple> {

    Set<T> findTriple(AdjacencyList list);

    Set<Node> getLeaves();

}
