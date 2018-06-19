package de.uni.leipzig.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class Node {

    private Integer label;

    private List<Integer> ids;

    private String path = "";

    public static Node helpNode() {
        return new Node(-1, new ArrayList<>());
    }

    public static Node of(Integer label, List<Integer> id) {
        if (label < 0)
            throw new IllegalArgumentException("Could not create a node with negative label.");

        return new Node(label, id);
    }

    private Node(Integer label, List<Integer> id) {
        this.label = label;
        this.ids = id;
        for (Integer i : id) {
            this.path += i;
        }
    }

    public boolean isHelpNode() {
        return label == -1;
    }

    @Override
    public String toString() {
        if (isHelpNode())
            return "*";

        return label + "-" + path;
    }

}
