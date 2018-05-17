package de.alxgrk.model;

import lombok.Value;

@Value
public class Node {

    String value;

    boolean visible;

    public static Node invisible() {
        return new Node("");
    }

    public static Node of(String value) {
        if (value.length() < 1)
            throw new IllegalArgumentException("Node value length must be >= 0");

        return new Node(value);
    }

    private Node(String value) {
        this.value = value;
        this.visible = value.length() >= 1;
    }

    @Override
    public String toString() {
        if (!visible)
            return "";

        return value;
    }

}
