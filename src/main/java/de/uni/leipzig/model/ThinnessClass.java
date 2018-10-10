package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ThinnessClass {

    private Set<Node> nodes = new HashSet<>();

    @VisibleForTesting
    protected ThinnessClass(Node... nodes) {
        this.nodes = Sets.newHashSet(nodes);
    }

    public void add(Node node) {
        this.nodes.add(node);
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

}
