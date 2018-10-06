package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.TripleFinder;

public class DeletionManipulator extends Manipulator {

    protected DeletionManipulator(Integer percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, TripleFinder tripleFinder) {
        if (getPercentage() == 0)
            return;

        Integer toBeDeleted = tripleSet.size() * getPercentage() / 100;
        System.out.println("remove " + toBeDeleted + " triples from set");
        Set<Triple> triplesToRemove = Sets.newHashSet();

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeDeleted-- != 0) {
            if (iterator.hasNext()) {
                triplesToRemove.add(iterator.next());
            } else
                break;
        }

        tripleSet.removeAll(triplesToRemove);
    }

}
