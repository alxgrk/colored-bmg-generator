package de.uni.leipzig.method;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;

import java.util.List;
import java.util.Set;

import de.uni.leipzig.Util;
import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;

class AhoInformative implements TreeCreation {

    @Override
    public void create(List<List<Node>> adjList) {
        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(adjList);

        AHO.get().create(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }

    @Override
    public void create(Set<Triple> triples, DiGraph diGraph) {
        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(triples, diGraph);

        AHO.get().create(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }
}