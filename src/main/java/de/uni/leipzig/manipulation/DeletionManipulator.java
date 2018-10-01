package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.Set;

import de.uni.leipzig.model.Triple;

public class DeletionManipulator extends Manipulator {

    protected DeletionManipulator(Integer percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet) {
        if (getPercentage() == 0)
            return;

        Integer toBeDeleted = tripleSet.size() * getPercentage() / 100;
        System.out.println("remove " + toBeDeleted + " triples from set");
        while (toBeDeleted-- != 0) {
            Iterator<Triple> iterator = tripleSet.iterator();
            if (iterator.hasNext()) {
                tripleSet.remove(iterator.next());
            } else
                break;
        }
    }

}
