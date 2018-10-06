package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;
import de.uni.leipzig.uncolored.TripleFinder;

public class InsertionManipulator extends Manipulator {

    protected InsertionManipulator(Integer percentage) {
        super(percentage);
    }

    public void manipulate(Set<Triple> tripleSet, TripleFinder tripleFinder) {
        if (getPercentage() == 0)
            return;

        Integer toBeInserted = tripleSet.size() * getPercentage() / 100;
        System.out.println("add " + toBeInserted + " triples to set");
        Set<Triple> triplesToAdd = Sets.newHashSet();

        Iterator<Triple> iterator = tripleSet.iterator();
        while (toBeInserted-- != 0) {
            if (iterator.hasNext()) {
                Triple nextTriple = iterator.next();
                addSimilar(nextTriple, triplesToAdd, tripleFinder);
            } else
                break;
        }

        tripleSet.addAll(triplesToAdd);
    }

    private void addSimilar(Triple nextTriple, Set<Triple> triplesToAdd,
            TripleFinder tripleFinder) {
        Node x = nextTriple.getEdge().getFirst();
        Node y = nextTriple.getEdge().getSecond();
        Node z = nextTriple.getNode();

        // manipulate label of node x
        String newLabel = x.getLabel()
                .replace(x.getLabel().charAt(0), (char) (x.getLabel().charAt(0) + 1));
        Node newX = Node.of(newLabel, x.getIds());

        // add another Integer (either the last value of the ids or 1) of node y
        List<Integer> yIds = y.getIds();
        List<Integer> newYIds = Lists.newArrayList(yIds);
        newYIds.add(!yIds.isEmpty() ? yIds.get(yIds.size() - 1) : 1);
        Node newY = Node.of(y.getLabel(), newYIds);

        // leave node z as is
        Node newZ = Node.of(z.getLabel(), z.getIds());

        triplesToAdd.add(new DefaultTriple(new Edge(newX, newY), newZ));
        tripleFinder.getLeaves().add(newX);
        tripleFinder.getLeaves().add(newY);
        tripleFinder.getLeaves().add(newZ);
    }

}
