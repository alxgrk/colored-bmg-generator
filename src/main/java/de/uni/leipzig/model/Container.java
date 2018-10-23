package de.uni.leipzig.model;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
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
