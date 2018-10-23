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
        int colorComp = this.getColor().compareTo(o.getColor());

        if (colorComp == 0) {
            Iterator<Integer> thisIt = this.getIds().iterator();
            Iterator<Integer> thatIt = o.getIds().iterator();

            while (thisIt.hasNext() && thatIt.hasNext()) {
                Integer thisI = (Integer) thisIt.next();
                Integer thatI = (Integer) thatIt.next();

                if (thisI != thatI)
                    return thisI.compareTo(thatI);
            }

            return !thisIt.hasNext() ? (!thatIt.hasNext() ? 0 : -1) : 1;
        } else {
            return colorComp;
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Node))
            return false;

        Node other = (Node) obj;

        if (this.isHelpNode())
            return super.equals(other);

        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (ids == null) {
            if (other.ids != null)
                return false;
        } else if (!ids.equals(other.ids))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((ids == null) ? 0 : ids.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

}
