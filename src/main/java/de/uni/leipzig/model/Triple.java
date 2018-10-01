package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.Edge;

public interface Triple {

    Edge getEdge();

    Node getNode();

    boolean contains(Node node);

    void invertEdge();

}
