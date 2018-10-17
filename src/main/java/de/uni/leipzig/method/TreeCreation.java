package de.uni.leipzig.method;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.*;
import lombok.RequiredArgsConstructor;

/**
 * An interface for tree creation methods. The implementation of the creation via a adjacency list
 * is required, all other creation ways may or may not be implemented.
 */
public interface TreeCreation {

    Tree create(AdjacencyList adjList);

    default Tree create(Set<Triple> triples, DiGraph diGraph) {
        return new Tree(Sets.newHashSet(Node.helpNode()));
    }

    default Tree create(Set<Triple> triples, Set<Node> leaves) {
        return new Tree(Sets.newHashSet(Node.helpNode()));
    }

    default Tree create(DiGraph graph) {
        return new Tree(Sets.newHashSet(Node.helpNode()));
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
