package de.uni.leipzig.conversion;

import java.util.*;
import java.util.stream.Collectors;

import de.uni.leipzig.model.*;

public class TripleFromTree {

    public Set<Triple> extractOf(Map<Pair<Color>, Tree> stTrees) {
        return stTrees.values()
                .stream()
                .flatMap(t -> treeToTriples(t).stream())
                .collect(Collectors.toSet());
    }

    public Set<Triple> treeToTriples(Tree tree) {

        return null;
    }

}
