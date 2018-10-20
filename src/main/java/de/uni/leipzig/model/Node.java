package de.uni.leipzig.model;

import java.util.*;

import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class Node implements Comparable<Node> {

    private Color color;

    private List<Integer> ids;

    private String path = "";

    public static Node helpNode() {
        return new Node("*", new ArrayList<>());
    }

    public static Node of(Integer color, List<Integer> id) {
        return of(color.toString(), id);
    }

    public static Node of(String color, List<Integer> id) {
        if (color.isEmpty())
            throw new IllegalArgumentException("Could not create a node with negative color.");

        return new Node(color, id);
    }

    private Node(String color, List<Integer> id) {
        this.color = new Color(color);
        this.ids = id;
        for (Integer i : id) {
            this.path += i;
        }
    }

    public boolean isHelpNode() {
        return color.toString().equals("*");
    }

    @Override
    public String toString() {
        if (isHelpNode())
            return color.toString();

        return color + "-" + path;
    }

    @Override
    public int compareTo(Node o) {
        return this.getPath().compareTo(o.getPath());
    }

}
