package de.uni.leipzig.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import de.uni.leipzig.model.edges.TreeEdge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeGraph {

    final Set<Tree> trees;

    final Set<TreeEdge> edges;

    final Set<Color> colors;

    public TreeGraph(Set<Tree> nodes, Set<TreeEdge> edges) {
        this.trees = ImmutableSet.copyOf(nodes);
        this.edges = ImmutableSet.copyOf(edges);

        this.colors = this.trees.stream()
                .flatMap(t -> t.getColors().stream())
                .collect(Collectors.toSet());
    }
    @Override
    public String toString() {
        return trees + " === " + edges;
    }
}
