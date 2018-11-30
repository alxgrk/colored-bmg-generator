package de.uni.leipzig.conversion;

import java.util.*;
import java.util.stream.Collectors;

import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.DefaultTripleFinder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TripleFromTree {

    private final DefaultTripleFinder defaultTripleFinder;

    public TripleFromTree() {
        this(new DefaultTripleFinder());
    }

    public Set<Triple> extractOf(Map<Set<Color>, Tree> stTrees) {
        return stTrees.values()
                .stream()
                .flatMap(t -> defaultTripleFinder.findTriple(t).getFirst().stream())
                .collect(Collectors.toSet());
    }

}
