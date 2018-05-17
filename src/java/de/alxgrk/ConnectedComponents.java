package de.alxgrk;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.alxgrk.model.Node;
import de.alxgrk.model.Tree;
import de.alxgrk.model.Triple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectedComponents {

    public static List<Tree> construct(Set<Triple> tripleSetR, List<Node> leftOvers) {
        List<Tree> subTrees = new ArrayList<>();

        // iterate over remaining nodes as long, as there are some left
        while (!leftOvers.isEmpty()) {

            // store nodes contained by a connected component in a sorted set
            TreeSet<Node> subTree = new TreeSet<>((c1, c2) -> c1.getValue().compareTo(c2
                    .getValue()));

            leftOvers = leftOvers.stream()
                    .filter(n -> {
                        // if this is the first of the remaining nodes,
                        // add it and remove it from 'leftOvers'
                        if (subTree.isEmpty()) {
                            subTree.add(n);
                            return false;
                        }

                        // iterate over all triples in order to see, if either
                        // the first or the second value of the edge connects
                        // the current and any other node already existing in
                        // the connected component
                        for (Triple t : tripleSetR) {
                            Node first = t.getEdge().getFirst();
                            Node second = t.getEdge().getSecond();

                            // current node is connected to a node of the
                            // connected component -> remove it from 'leftOvers'
                            if (first.equals(n) && subTree.contains(second)) {
                                subTree.add(first);
                                return false;
                            }

                            // current node is connected to a node of the
                            // connected component -> remove it from 'leftOvers'
                            if (second.equals(n) && subTree.contains(first)) {
                                subTree.add(second);
                                return false;
                            }
                        }

                        // current node is not part of this connected component,
                        // leave it in 'leftOvers' for the next iteration
                        return true;
                    })
                    .collect(Collectors.toList());

            subTrees.add(new Tree(subTree));
        }

        return subTrees;
    }

}
