package de.uni.leipzig;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.model.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public boolean equalSets(Collection<?> s1, Collection<?> s2) {
        return Sets.difference(Sets.newHashSet(s1), Sets.newHashSet(s2)).isEmpty()
                && Sets.difference(Sets.newHashSet(s2), Sets.newHashSet(s1)).isEmpty();
    }

    public boolean properSubset(Collection<?> subsetOf, Collection<?> parentSet) {
        return properSubset(Sets.newHashSet(subsetOf), Sets.newHashSet(parentSet));
    }

    private boolean properSubset(Set<?> subsetOf, Set<?> parentSet) {
        SetView<?> intersection = Sets.intersection(subsetOf, parentSet);

        if (Util.equalSets(intersection, subsetOf)) {
            return true;
        }

        return false;
    }

    public Tree nodeSetToLeafTree(Set<Node> nodes) {
        List<Tree> subtrees = nodes.stream()
                .map(Tree::new)
                .collect(Collectors.toList());

        return subtrees.size() == 1 ? subtrees.get(0) : new Tree(subtrees);
    }

    @SuppressWarnings("unchecked")
    public Set<Triple> uglyCast(Set<? extends Triple> triples) {
        return (Set<Triple>) (Collection<?>) triples;
    }

}
