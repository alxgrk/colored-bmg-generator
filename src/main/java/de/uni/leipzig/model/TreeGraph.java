package de.uni.leipzig.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.edges.TreeEdge;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeGraph {

    final Set<Tree> trees;

    final Set<TreeEdge> edges;

    final Set<Color> colors;

    public TreeGraph(Set<Tree> nodes, Set<TreeEdge> edges) {
        this.trees = Sets.newHashSet(nodes);
        this.edges = Sets.newHashSet(edges);

        this.colors = this.trees.stream()
                .flatMap(t -> t.getColors().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return trees + " === " + edges;
    }
}
