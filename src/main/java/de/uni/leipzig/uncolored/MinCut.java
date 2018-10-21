package de.uni.leipzig.uncolored;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.flow.GusfieldGomoryHuCutTree;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.conversion.GraphFromTripleSet;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class MinCut {

    private final GraphFromTripleSet graphFromTripleSet;

    public MinCut() {
        this(new GraphFromTripleSet());
    }

    public Set<Triple> create(Set<Triple> tripleSetR) {

        // note that the GusfieldGomoryHuCutTree requires the graph to be undirected
        Graph<Node, Edge> graph = graphFromTripleSet.toJGraph(tripleSetR);

        GusfieldGomoryHuCutTree<Node, Edge> minCutGraph = new GusfieldGomoryHuCutTree<>(graph);
        minCutGraph.calculateMinCut();

        Set<Node> sink = minCutGraph.getSinkPartition();
        Set<Node> source = minCutGraph.getSourcePartition();

        Set<Triple> cutTripleSet = new HashSet<>();

        for (Triple triple : tripleSetR) {
            if ((sink.contains(triple.getEdge().getFirst())
                    && source.contains(triple.getEdge().getSecond()))
                    || (source.contains(triple.getEdge().getFirst())
                            && sink.contains(triple.getEdge().getSecond()))) {
                continue;
            }

            cutTripleSet.add(triple);
        }

        return cutTripleSet;
    }

}
