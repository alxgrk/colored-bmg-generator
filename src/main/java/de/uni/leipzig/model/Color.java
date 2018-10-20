package de.uni.leipzig.model;

import lombok.*;

@Value
public class Color {

    @Getter(value = AccessLevel.NONE)
    String color;

    @Override
    public String toString() {
        return color;
    }

}
