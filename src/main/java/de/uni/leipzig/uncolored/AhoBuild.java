package de.uni.leipzig.uncolored;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.zalando.fauxpas.ThrowingRunnable;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.user.UserInput;

public class AhoBuild {

    private List<Tree> connectedComponents;

    private boolean alwaysMinCut = false;

    public Tree build(Set<Triple> tripleSetR, Set<Node> leaveSetL) {

        // if there is only one leave, return a tree containing only this leave
        if (leaveSetL.size() == 1)
            return new Tree(leaveSetL);

        connectedComponents = ConnectedComponents.construct(tripleSetR, leaveSetL);

        // exit if tree is no phylogenetic one
        if (connectedComponents.size() == 1) {

            ThrowingRunnable<Exception> minCut = () -> {
                DiGraphFromTripleSet creator = new DiGraphFromTripleSet();

                connectedComponents = creator.create(tripleSetR, leaveSetL);
            };

            if (alwaysMinCut)
                minCut.run();
            else {

                UserInput ui = new UserInput();

                ui.register("exit", () -> {
                    throw new RuntimeException("no phylogenetic tree");
                });

                ui.register("min cut", minCut);

                ui.register("always min cut", () -> {
                    alwaysMinCut = true;
                    minCut.run();
                });

                ui.askWithOptions(
                        "How do you want to handle that there is only one connected component?");
            }
        }

        // create invisible root node
        List<Node> root = new ArrayList<>();
        root.add(Node.helpNode());

        return connectedComponents.stream()
                .map(component -> {

                    // get all nodes of this component recursively
                    Set<Node> subLeaveSet = Sets.newHashSet(component.getNodes());

                    // filter all triples describing this component
                    Set<Triple> subTripleSet = filter(subLeaveSet, tripleSetR);

                    // recursively invoke 'build' with the triples/leaves of
                    // this component
                    Tree subTree = build(subTripleSet, subLeaveSet);

                    return subTree;
                })
                // concatenate all components under the root tree
                .reduce(new Tree(root), (i, t) -> {
                    Tree newTree = i.addSubTree(t);
                    return newTree;
                });
    }

    /**
     * Returns a new set of triples with each triple only containing nodes from the provided leave
     * set.
     */
    private Set<Triple> filter(Set<Node> subLeaveSet, Set<Triple> tripleSetR) {
        return tripleSetR.stream()
                .filter(t -> subLeaveSet.contains(t.getEdge().getFirst())
                        && subLeaveSet.contains(t.getEdge().getSecond()) && subLeaveSet.contains(t
                                .getNode()))
                .collect(Collectors.toSet());
    }

}
