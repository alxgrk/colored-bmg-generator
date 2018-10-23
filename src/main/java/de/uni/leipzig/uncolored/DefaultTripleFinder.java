package de.uni.leipzig.uncolored;

import java.util.*;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;
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
    public Pair<Set<Triple>, Set<Node>> findTriple(AdjacencyList list) {

        Set<Node> leaves = new HashSet<>();
        Set<Triple> triples = new HashSet<>();

        for (int i = 0; i < list.size(); i++) {

            List<Node> firstList = list.get(i);

            if (!isLeave(firstList)) {
                continue;
            }

            Node firstNode = firstList.get(0);
            // add every leave
            leaves.add(firstNode);

            for (int j = 0; j < list.size(); j++) {

                List<Node> secondList = list.get(j);

                if (!isLeave(secondList) || j == i) {
                    continue;
                }

                Node secondNode = secondList.get(0);

                for (int k = 0; k < list.size(); k++) {

                    List<Node> thirdList = list.get(k);

                    if (isLeave(thirdList) && k != j && k != i) {

                        Node thirdNode = thirdList.get(0);

                        List<Integer> x = firstNode.getIds();
                        List<Integer> y = secondNode.getIds();
                        List<Integer> z = thirdNode.getIds();

                        List<Integer> lcaXY = findLCA(x, y);
                        List<Integer> lcaXYZ = findLCA(z, lcaXY);

                        // xy is the ingroup, z is the outgroup
                        if (isTriple(lcaXY, lcaXYZ)) {
                            Edge edge = new Edge(firstNode, secondNode);
                            Triple tripel = new DefaultTriple(edge, thirdNode);
                            triples.add(tripel);
                        }
                    }
                }
            }
        }

        return Pair.of(triples, leaves);
    }

    private boolean isTriple(List<Integer> lcaXY, List<Integer> lcaXYZ) {
        return lcaXY.size() > lcaXYZ.size();
    }

    private List<Integer> findLCA(List<Integer> a, List<Integer> b) {

        List<Integer> ancester = new ArrayList<>();

        int v = 0;

        while (a.get(v) == b.get(v)) {

            ancester.add(a.get(v));
            if (v == a.size() - 1 || v == b.size() - 1) {
                return ancester;
            }
            v++;
        }
        return ancester;
    }

    private boolean isLeave(List<Node> nodes) {
        return nodes.size() == 1;
    }

}
