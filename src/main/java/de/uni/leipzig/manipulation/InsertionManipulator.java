package de.uni.leipzig.manipulation;

import java.util.*;

import com.google.common.collect.*;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;

public class InsertionManipulator extends Manipulator {

    public InsertionManipulator(Integer percentage) {
        this((double) percentage);
    }

    public InsertionManipulator(Double percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, Set<Node> leaves) {
        if (getPercentage() == 0)
            return;

        Integer toBeInserted = (int) (tripleSet.size() * getPercentage() / 100);
        System.out.println("add " + toBeInserted + " triples to set");
        Set<Triple> triplesToAdd = Sets.newHashSet();

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeInserted-- != 0) {
            Triple nextTriple = iterator.next();
            addSimilar(nextTriple, triplesToAdd, leaves);
        }

        tripleSet.addAll(triplesToAdd);
    }

    private void addSimilar(Triple nextTriple, Set<Triple> triplesToAdd,
            Set<Node> leaves) {
        Node x = nextTriple.getEdge().getFirst();
        Node y = nextTriple.getEdge().getSecond();
        Node z = nextTriple.getNode();

        // manipulate label of node x
        String newLabel = x.getColor()
                .toString()
                .replace(x.getColor().toString().charAt(0),
                        (char) (x.getColor().toString().charAt(0) + 1));
        Node newX = Node.of(newLabel, x.getIds());

        // add another Integer (either the last value of the ids or 1) of node y
        List<Integer> yIds = y.getIds();
        List<Integer> newYIds = Lists.newArrayList(yIds);
        newYIds.add(!yIds.isEmpty() ? yIds.get(yIds.size() - 1) : 1);
        Node newY = Node.of(y.getColor().toString(), newYIds);

        // leave node z as is
        Node newZ = Node.of(z.getColor().toString(), z.getIds());

        triplesToAdd.add(new DefaultTriple(new Edge(newX, newY), newZ));
        leaves.add(newX);
        leaves.add(newY);
        leaves.add(newZ);
    }

}
