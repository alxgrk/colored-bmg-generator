package de.uni.leipzig.manipulation;

import java.util.*;

import de.uni.leipzig.model.*;

public class InversionManipulator extends Manipulator {

    public InversionManipulator(Integer percentage) {
        this((double) percentage);
    }

    public InversionManipulator(Double percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, Set<Node> leaves) {
        if (getPercentage() == 0)
            return;

        Integer toBeInverted = (int) (tripleSet.size() * getPercentage() / 100);
        System.out.println("invert " + toBeInverted + " triples");

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeInverted-- != 0) {
            Triple nextTriple = iterator.next();
            nextTriple.invertEdge();
        }
    }

}
