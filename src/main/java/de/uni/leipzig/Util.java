package de.uni.leipzig;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.model.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public boolean equalSets(Collection<?> c1, Collection<?> c2) {
        Set<?> set1 = c1 instanceof Set ? (Set<?>) c1 : Sets.newHashSet(c1);
        Set<?> set2 = c2 instanceof Set ? (Set<?>) c2 : Sets.newHashSet(c2);
        return equalSets(set1, set2);
    }

    public boolean equalSets(Set<?> set1, Set<?> set2) {
        return set1.equals(set2);
    }

    public boolean properSubset(Collection<?> subsetOf, Collection<?> parentSet) {
        return properSubset(
                subsetOf instanceof Set ? (Set<?>) subsetOf : Sets.newHashSet(subsetOf),
                parentSet instanceof Set ? (Set<?>) parentSet : Sets.newHashSet(parentSet));
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
