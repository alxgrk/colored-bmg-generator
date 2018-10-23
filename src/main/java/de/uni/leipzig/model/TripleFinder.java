package de.uni.leipzig.model;

import java.util.Set;

public interface TripleFinder<T extends Triple> {

    org.jgrapht.alg.util.Pair<Set<T>, Set<Node>> findTriple(AdjacencyList list);

}
