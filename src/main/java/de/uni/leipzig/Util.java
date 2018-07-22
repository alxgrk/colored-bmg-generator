package de.uni.leipzig;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.model.Node;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public Comparator<Node> nodeByPathComparator = (c1, c2) -> c1.getPath().compareTo(c2.getPath());

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

}
