package de.uni.leipzig.informative.model;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;

public class InformativeTriple extends DefaultTriple implements Triple {

    public InformativeTriple(Edge edge, Node node) {
        super(edge, node);
    }

}
