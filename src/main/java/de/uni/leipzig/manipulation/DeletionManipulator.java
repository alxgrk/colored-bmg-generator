package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;

public class DeletionManipulator extends Manipulator {

    protected DeletionManipulator(Integer percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, Set<Node> leaves) {
        if (getPercentage() == 0)
            return;

        Integer toBeDeleted = tripleSet.size() * getPercentage() / 100;
        System.out.println("remove " + toBeDeleted + " triples from set");
        Set<Triple> triplesToRemove = Sets.newHashSet();

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeDeleted-- != 0) {
            triplesToRemove.add(iterator.next());
        }

        tripleSet.removeAll(triplesToRemove);
    }

}
