package de.uni.leipzig.informative;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.uni.leipzig.informative.model.*;
import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.uncolored.DefaultTripleFinder;
import lombok.Getter;

public class InformativeTripleFinder implements TripleFinder<InformativeTriple> {

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

    private DefaultTripleFinder defaultTripleFinder = new DefaultTripleFinder();

    private DiGraphExtractor graphExtractor = new DiGraphExtractor();

    @Getter
    private Set<Node> leaves = new HashSet<>();

    public Set<InformativeTriple> findTriple(AdjacencyList adjList) {
        Set<Triple> triples = defaultTripleFinder.findTriple(adjList);
        DiGraph graph = graphExtractor.extract(adjList);

        return findTriple(triples, graph);
    }

    public Set<InformativeTriple> findTriple(Set<Triple> triples, DiGraph graph) {

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
