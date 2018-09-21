package de.uni.leipzig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.colored.axiom.Axioms;
import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Reachables;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.uncolored.TripleFinder;

public class Main {

    /**
     * Uses a random tree (disable with <code>-Dno-random</code>). Three possible arguments (can be
     * mixed):<br>
     * <ul>
     * <li><b>aho</b> - the normal Aho-Build algorithm</li>
     * <li><b>equivalence-class-based</b> - calculates the least resolved tree using equivalence
     * classes, neighbourships and reachability relations</li>
     * <li><b>aho-informative</b> - calculates the least resolved tree using informative
     * triples</li>
     * </ul>
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new Main(args);
    }

    public Main(String[] args) {
        List<String> argList = Arrays.asList(args);

        // TODO disable with "no-random"
        RandomTree randomTree = new RandomTree(3, 4, 2);
        List<List<Node>> adjList = randomTree.create();

        if (argList.contains("aho")) {
            ahoBuild(adjList);
        }

        if (argList.contains("equivalence-class-based")) {
            equivalenceClassBased(adjList);
        }

        if (argList.contains("aho-informative")) {
            ahoWithInformativeTriples(adjList);
        }
    }

    private void ahoBuild(List<List<Node>> adjList) {
        // **** Aho Build ohne informative triple ****
        TripleFinder tripleFinder = new TripleFinder();
        Set<Triple> triples = tripleFinder.findTriple(adjList);

        System.out.println(triples.toString());

        AhoBuild<Triple> ahoBuild = new AhoBuild<>();
        Tree result = ahoBuild.build(triples, tripleFinder.getLeaves());

        System.out.println(result.toNewickNotation());
    }

    private void equivalenceClassBased(List<List<Node>> adjList) {
        // **** Equivalence class based part ****
        DiGraphExtractor extractor = new DiGraphExtractor();
        DiGraph graph = extractor.extract(adjList);

        System.out.println("Nodes: " + graph.getNodes());
        System.out.println("Edges: " + graph.getEdges());

        System.out.println("Neighbourhood: " + graph.getNeighboursByEc());
        graph.getReachablesByEc()
                .values()
                .stream()
                .map(Reachables::getRq)
                .forEach(rq -> System.out.println("RQ: " + rq));

        boolean axiomsFulfilled = Axioms.checkAll(graph);

        if (!axiomsFulfilled)
            throw new RuntimeException("Axioms not fulfilled!");

        Tree leastResolvedTree = graph.getHasseDiagram();
        System.out.println(leastResolvedTree);
    }

    private void ahoWithInformativeTriples(List<List<Node>> adjList) {
        // **** Aho Build mit informative triple ****

        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(adjList);
        System.out.println(informativeTriples);

        AhoBuild<InformativeTriple> ahoBuild = new AhoBuild<>();
        Tree result = ahoBuild.build(informativeTriples, informativeTripleFinder.getLeaves());

        System.out.println(result.toNewickNotation());
    }

}