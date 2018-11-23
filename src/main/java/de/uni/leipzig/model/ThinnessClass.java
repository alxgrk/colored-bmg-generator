package de.uni.leipzig.model;

import java.util.*;

import com.google.common.collect.Sets;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class ThinnessClass {

    private Set<Node> nodes = new HashSet<>();

    private Set<Node> outNeighbours;

    private Set<Node> inNeighbours;

    public ThinnessClass(@NonNull Set<Node> outNeighbours, @NonNull Set<Node> inNeighbours,
            Node... nodes) {
        this.outNeighbours = outNeighbours;
        this.inNeighbours = inNeighbours;
        this.nodes = Sets.newHashSet(nodes);
    }

    public void add(Node node) {
        this.nodes.add(node);
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

}
