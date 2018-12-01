package de.uni.leipzig.manipulation;

import java.util.Set;

import de.uni.leipzig.model.*;
import lombok.Getter;

@Getter
public abstract class Manipulator {

    private final Double percentage;

    protected Manipulator(Double percentage) {
        if (percentage < 0 || percentage >= 100)
            throw new IllegalArgumentException(
                    "No valid percentage - must be more than 0 & less than 100%");

        this.percentage = percentage;
    }

    public abstract void manipulate(Set<Triple> tripleSet, Set<Node> leaves);

}
