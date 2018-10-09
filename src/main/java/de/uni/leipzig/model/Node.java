package de.uni.leipzig.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class Node implements Comparable<Node> {

    private String label;

    private List<Integer> ids;

    private String path = "";

    public static Node helpNode() {
        return new Node("*", new ArrayList<>());
    }

    public static Node of(Integer label, List<Integer> id) {
        return of(label.toString(), id);
    }

    public static Node of(String label, List<Integer> id) {
        if (label.isEmpty())
            throw new IllegalArgumentException("Could not create a node with negative label.");

        return new Node(label, id);
    }

    private Node(String label, List<Integer> id) {
        this.label = label;
        this.ids = id;
        for (Integer i : id) {
            this.path += i;
        }
    }

    public boolean isHelpNode() {
        return label.equals("*");
    }

    @Override
    public String toString() {
        if (isHelpNode())
            return label;

        return label + "-" + path;
    }

    @Override
    public int compareTo(Node o) {
        return this.getPath().compareTo(o.getPath());
    }

}
