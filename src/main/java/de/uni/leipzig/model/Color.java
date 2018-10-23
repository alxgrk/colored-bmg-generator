package de.uni.leipzig.model;

import lombok.*;

@Value
public class Color implements Comparable<Color> {

    @Getter(value = AccessLevel.NONE)
    String color;

    @Override
    public String toString() {
        return color;
    }

    @Override
    public int compareTo(Color o) {
        return this.toString().compareTo(o.toString());
    }

}
