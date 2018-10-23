package de.uni.leipzig.informative;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import com.google.common.collect.*;

import de.uni.leipzig.informative.model.*;
import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.uncolored.DefaultTripleFinder;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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

    private final DefaultTripleFinder defaultTripleFinder;

    private final DiGraphExtractor graphExtractor;

    public InformativeTripleFinder() {
        this(new DefaultTripleFinder(), new DiGraphExtractor());
    }

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(AdjacencyList adjList) {
        Pair<Set<Triple>, Set<Node>> triplesAndLeaves = defaultTripleFinder.findTriple(adjList);
        Set<Triple> triples = triplesAndLeaves.getFirst();
        DiGraph graph = graphExtractor.extract(adjList);

        return findTriple(triples, graph);
    }

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(Set<Triple> triples, DiGraph graph) {

        // FIXME missing edges in graph

        Set<Node> leaves = Sets.newHashSet();
        List<ThreeNodeGraph> subgraphs = Lists.newArrayList();

        triples.stream()
                .filter(t -> {
                    boolean contained = false;
                    for (Node leave : graph.getNodes()) {
                        if (t.contains(leave)) {
                            contained = true;
                            break;
                        }
                    }
                    return contained;
                })
                .forEach(triple -> {

                    ThreeNodeGraph subgraph = new ThreeNodeGraph(triple);

                    // add every edge that belongs to the subgraph
                    graph.getEdges().forEach(subgraph::add);

                    // add subgraph when created by informative triple
                    if (isIsomorphicToOneX(subgraph))
                        subgraphs.add(subgraph);
                });

        Set<InformativeTriple> informativeTriples = subgraphs.stream()
                .map(ThreeNodeGraph::getTriple)
                .peek(t -> {
                    leaves.add(t.getEdge().getFirst());
                    leaves.add(t.getEdge().getSecond());
                    leaves.add(t.getNode());
                })
                .map(t -> new InformativeTriple(t.getEdge(), t.getNode()))
                .collect(Collectors.toSet());
        return Pair.of(informativeTriples, leaves);
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
