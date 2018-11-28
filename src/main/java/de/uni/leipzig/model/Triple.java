package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.AbstractPair;

public interface Triple extends Comparable<Triple> {

    <E extends AbstractPair<Node>> E getEdge();

    Node getNode();

    boolean contains(Node node);

    void invertEdge();

}
