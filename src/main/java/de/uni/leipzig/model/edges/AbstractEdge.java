package de.uni.leipzig.model.edges;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@AllArgsConstructor
@NonFinal
public abstract class AbstractEdge<T> {

    @NonNull
    T first;

    @NonNull
    T second;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AbstractEdge<T> other = (AbstractEdge<T>) obj;

        if (first.equals(other.second) && second.equals(other.first))
            return true;
        if (!first.equals(other.first))
            return false;
        if (!second.equals(other.second))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + first.hashCode();
        result = prime * result + second.hashCode();
        return result;
    }

}
