package de.uni.leipzig.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Container<T> {

    @SuppressWarnings("unchecked")
    public static <T> Container<T> empty() {
        return (Container<T>) new Container<>(null);
    }

    private T value;

    public boolean isEmpty() {
        return value == null;
    }

}
