package de.uni.leipzig.manipulation;

import java.util.*;

import de.uni.leipzig.model.*;

public class DeletionManipulator extends Manipulator {

    public DeletionManipulator(Integer percentage) {
        this((double) percentage);
    }

    public DeletionManipulator(Double percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, Set<Node> leaves) {
        if (getPercentage() == 0)
            return;

        Integer toBeDeleted = (int) (tripleSet.size() * getPercentage() / 100);
        System.out.println("remove " + toBeDeleted + " triples from set");

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeDeleted-- != 0) {
            iterator.next();
            iterator.remove();
        }
    }

}
