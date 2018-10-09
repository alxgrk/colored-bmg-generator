package de.uni.leipzig.manipulation;

import java.util.Set;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import lombok.Getter;

@Getter
public abstract class Manipulator {

    private final Integer percentage;

    protected Manipulator(Integer percentage) {
        if (percentage < 0 || percentage > 99)
            throw new IllegalArgumentException("No valid percentage - must be between 1 & 99 %");

        this.percentage = percentage;
    }

    public abstract void manipulate(Set<Triple> tripleSet, Set<Node> leaves);

}
