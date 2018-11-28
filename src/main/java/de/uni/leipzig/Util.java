package de.uni.leipzig;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.Node;
import lombok.*;
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

    public List<Integer> findLCA(Node a, Node b) {
        return findLCA(a.getIds(), b.getIds());
    }

    public List<Integer> findLCA(List<Integer> a, List<Integer> b) {

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

    @AllArgsConstructor
    @Getter
    @ToString
    public static class LcaForTwoNodes {
        Node a;

        Node b;

        Integer lcaPath;
    }

    @SuppressWarnings("unchecked")
    public Set<Triple> uglyCast(Set<? extends Triple> triples) {
        return (Set<Triple>) (Collection<?>) triples;
    }

    public <T> Collector<T, ?, List<T>> maxList(Comparator<? super T> comp) {
        return Collector.of(
                ArrayList::new,
                (list, t) -> {
                    int c;
                    if (list.isEmpty() || (c = comp.compare(t, list.get(0))) == 0) {
                        list.add(t);
                    } else if (c > 0) {
                        list.clear();
                        list.add(t);
                    }
                },
                (list1, list2) -> {
                    if (list1.isEmpty()) {
                        return list2;
                    }
                    if (list2.isEmpty()) {
                        return list1;
                    }
                    int r = comp.compare(list1.get(0), list2.get(0));
                    if (r < 0) {
                        return list2;
                    } else if (r > 0) {
                        return list1;
                    } else {
                        list1.addAll(list2);
                        return list1;
                    }
                });
    }

    public <T, F> Set<T> filterAndReduce(Set<T> original,
            Function<T, F> prefilterConverter,
            Predicate<? super F> filter) {
        return filterAndReduce(original, prefilterConverter, filter, Function.identity());
    }

    public <T, F, R> Set<R> filterAndReduce(Set<T> original,
            Function<T, F> prefilterConverter,
            Predicate<? super F> filter,
            Function<T, R> postfilterConverter) {
        Set<R> subset = new HashSet<>();

        final Iterator<T> each = original.iterator();
        while (each.hasNext()) {
            T next = each.next();
            F prefiltered = prefilterConverter.apply(next);
            if (filter.test(prefiltered)) {
                subset.add(postfilterConverter.apply(next));
                each.remove();
            }
        }
        return subset;
    }

}
