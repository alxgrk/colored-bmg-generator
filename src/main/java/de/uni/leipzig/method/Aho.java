package de.uni.leipzig.method;

import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.manipulation.Manipulation;
import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.*;
import de.uni.leipzig.user.UserInput;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class Aho implements TreeCreation {

    private final AhoBuild ahoBuild;

    private final UserInput input;

    private final TripleFinder<Triple> tripleFinder;

    private boolean interactiveMode = true;

    private boolean inPrintMode = true;

    Aho() {
        this(new AhoBuild(), new UserInput());
    }

    protected Aho(AhoBuild ahoBuild, UserInput input) {
        this(ahoBuild, input, new DefaultTripleFinder());
    }

    @Override
    public TreeCreation inNonInteractiveMode(boolean mode) {
        ahoBuild.setAlwaysMinCut(mode);
        interactiveMode = !mode;
        return this;
    }

    @Override
    public TreeCreation inPrintMode(boolean mode) {
        inPrintMode = mode;
        return this;
    }

    @Override
    public Tree create(AdjacencyList adjList) {
        Pair<Set<Triple>, Set<Node>> triplesAndLeaves = tripleFinder.findTriple(adjList);
        return create(triplesAndLeaves.getFirst(), triplesAndLeaves.getSecond());
    }

    @Override
    public Tree create(Set<Triple> triples, Set<Node> leaves) {

        if (interactiveMode)
            new Manipulation(triples, leaves, input);

        Tree result = ahoBuild.build(triples, leaves);

        if (inPrintMode)
            System.out.println(result.print());

        return result;
    }
}