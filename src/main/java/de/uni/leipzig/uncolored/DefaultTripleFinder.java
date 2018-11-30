package de.uni.leipzig.uncolored;

import static de.uni.leipzig.Util.*;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.Util.LcaForTwoNodes;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;
import lombok.Getter;

@Getter
public class DefaultTripleFinder implements TripleFinder<Triple> {

    /**
     * Extracts all possible triples from the adjacent list.<br>
     * <b>Be careful:</b> triples could be equal, but inverted!
     * 
     * @param list
     * @return
     */
    public Pair<Set<Triple>, Set<Node>> findTriple(Tree tree) {

        Set<Node> leaves = new HashSet<>(tree.getLeaves());
        Set<Triple> triples = findByLeaves(leaves);

        return Pair.of(triples, leaves);
    }

    /**
     * Extracts all possible triples from the adjacent list.<br>
     * <b>Be careful:</b> triples could be equal, but inverted!
     * 
     * @param list
     * @return
     */
    public Pair<Set<Triple>, Set<Node>> findTriple(AdjacencyList list) {

        Set<Node> leaves = list.getChildNodes();
        Set<Triple> triples = findByLeaves(leaves);

        return Pair.of(triples, leaves);
    }

    public Set<Triple> findByLeaves(Set<Node> leaves) {
        Set<Triple> triples = new HashSet<>();

        for (Node firstNode : leaves) {

            List<LcaForTwoNodes> lcaCandidates = leaves.stream()
                    .filter(n -> !firstNode.equals(n)
                            && !firstNode.getColor().equals(n.getColor()))
                    .map(n -> new LcaForTwoNodes(firstNode, n, findLCA(firstNode, n).size()))
                    .collect(Collectors.toList());

            if (lcaCandidates.size() > 1) {
                List<LcaForTwoNodes> closest = lcaCandidates.stream()
                        .collect(maxList(Comparator.comparing(LcaForTwoNodes::getLcaPath)));

                lcaCandidates.removeAll(closest);

                closest.stream()
                        .flatMap(lca -> lcaCandidates.stream()
                                .map(LcaForTwoNodes::getB)
                                .map(c -> new DefaultTriple(
                                        new DiEdge(lca.getA(), lca.getB()),
                                        c)))
                        .forEach(triples::add);
            }
        }
        return triples;
    }

}
