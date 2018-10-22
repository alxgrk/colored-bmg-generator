package de.uni.leipzig.model.edges;

import de.uni.leipzig.model.Tree;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@EqualsAndHashCode(callSuper = true)
public class TreeEdge extends AbstractPair<Tree> {

    public TreeEdge(Tree first, Tree second) {
        super(first, second);
    }

    @Override
    public String toString() {
        return getFirst() + "," + getSecond();
    }

}
