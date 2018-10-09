package de.uni.leipzig.informative;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.informative.model.ThreeNodeGraph;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.TripleFinder;
import lombok.Getter;

public class InformativeTripleFinder {

    /**
     * The allowed frequencies for an {@link ThreeNodeGraph} to be isomorphic to the subgraphs X1,
     * X2, X3 and X4, which are required for informative triples.
     */
    private static final List<String> POSSIBILITIES = Lists.newArrayList(
            "220", "202", "022", // X1
            "321", "231", "132", "123", // X2
            "110", "101", "011", // X3
            "211", "121", "112" // X4
    );

    private TripleFinder tripleFinder = new TripleFinder();

    private DiGraphExtractor graphExtractor = new DiGraphExtractor();

    @Getter
    private Set<Node> leaves = new HashSet<>();

    public Set<InformativeTriple> findInformativeTriples(List<List<Node>> adjList) {
        Set<Triple> triples = tripleFinder.findTriple(adjList);
        DiGraph graph = graphExtractor.extract(adjList);

        return findInformativeTriples(triples, graph);
    }

    public Set<InformativeTriple> findInformativeTriples(Set<Triple> triples, DiGraph graph) {

        List<ThreeNodeGraph> subgraphs = Lists.newArrayList();

        for (Triple triple : triples) {

            ThreeNodeGraph subgraph = new ThreeNodeGraph(triple);

            // add every edge that belongs to the subgraph
            graph.getEdges().forEach(subgraph::add);

            // add subgraph when created by informative triple
            if (isIsomorphicToOneX(subgraph))
                subgraphs.add(subgraph);
        }

        return subgraphs.stream()
                .map(ThreeNodeGraph::getTriple)
                .peek(t -> {
                    leaves.add(t.getEdge().getFirst());
                    leaves.add(t.getEdge().getSecond());
                    leaves.add(t.getNode());
                })
                .map(t -> new InformativeTriple(t.getEdge(), t.getNode()))
                .collect(Collectors.toSet());
    }

    private boolean isIsomorphicToOneX(ThreeNodeGraph subgraph) {

        String nodeFrequencyAsString = subgraph.getNodeFrequency()
                .values()
                .stream()
                .map(i -> i.toString())
                .reduce("", (i, s) -> i.concat(s));

        return POSSIBILITIES.contains(nodeFrequencyAsString);
    }

}
