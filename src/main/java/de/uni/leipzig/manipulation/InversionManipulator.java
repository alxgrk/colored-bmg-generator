package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.Set;

import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.TripleFinder;

public class InversionManipulator extends Manipulator {

    protected InversionManipulator(Integer percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, TripleFinder tripleFinder) {
        if (getPercentage() == 0)
            return;

        Integer toBeInverted = tripleSet.size() * getPercentage() / 100;
        System.out.println("invert " + toBeInverted + " triples");

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeInverted-- != 0) {
            if (iterator.hasNext()) {
                Triple nextTriple = iterator.next();
                nextTriple.invertEdge();
            } else
                break;
        }
    }

}
