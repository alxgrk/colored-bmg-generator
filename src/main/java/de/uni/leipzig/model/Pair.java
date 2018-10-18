package de.uni.leipzig.model;

import de.uni.leipzig.model.edges.AbstractPair;

public class Pair<T> extends AbstractPair<T> {

    public Pair(T first, T second) {
        super(first, second);
    }

    @Override
    public String toString() {
        return getFirst() + "," + getSecond();
    }

}
