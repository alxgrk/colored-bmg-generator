package de.uni.leipzig.method;

import java.util.Set;

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

    Aho() {
        this(new AhoBuild(), new UserInput());
    }

    protected Aho(AhoBuild ahoBuild, UserInput input) {
        this(ahoBuild, input, new DefaultTripleFinder());
    }

    @Override
    public Tree create(AdjacencyList adjList) {
        return create(tripleFinder.findTriple(adjList), tripleFinder.getLeaves());
    }

    @Override
    public Tree create(Set<Triple> triples, Set<Node> leaves) {
        System.out.println(triples.toString());

        new Manipulation(triples, leaves, input);

        Tree result = ahoBuild.build(triples, leaves);

        System.out.println(result.toNewickNotation());
        System.out.println(result.print());

        return result;
    }
}