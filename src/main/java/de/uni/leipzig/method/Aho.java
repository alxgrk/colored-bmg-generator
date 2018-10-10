package de.uni.leipzig.method;

import java.util.Set;

import de.uni.leipzig.manipulation.Manipulation;
import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.uncolored.TripleFinder;

class Aho implements TreeCreation {

    @Override
    public void create(AdjacencyList adjList) {
        TripleFinder tripleFinder = new TripleFinder();
        create(tripleFinder.findTriple(adjList), tripleFinder.getLeaves());
    }

    @Override
    public void create(Set<Triple> triples, Set<Node> leaves) {
        System.out.println(triples.toString());

        Manipulation.askForManipulation(triples, leaves);

        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(triples, leaves);

        System.out.println(result.toNewickNotation());
        System.out.println(result.print());
    }
}