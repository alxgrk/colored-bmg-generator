package de.uni.leipzig.method;

import java.util.List;
import java.util.Set;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import lombok.RequiredArgsConstructor;

/**
 * An interface for tree creation methods. The implementation of the creation via a adjacency list
 * is required, all other creation ways may or may not be implemented.
 */
public interface TreeCreation {

    void create(List<List<Node>> adjList);

    default void create(Set<Triple> triples, DiGraph diGraph) {
    }

    default void create(Set<Triple> triples, Set<Node> leaves) {
    }

    default void create(DiGraph graph) {
    }

    @RequiredArgsConstructor
    enum Method {
        AHO(new Aho()),
        AHO_INFORMATIVE(new AhoInformative()),
        THINNESS_CLASS(new ThinnessClass());

        private final TreeCreation method;

        public TreeCreation get() {
            return method;
        }
    }

}
