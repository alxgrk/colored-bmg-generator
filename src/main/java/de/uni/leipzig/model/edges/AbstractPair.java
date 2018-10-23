package de.uni.leipzig.model.edges;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
@AllArgsConstructor
@NonFinal
public abstract class AbstractPair<T> implements Comparable<AbstractPair<T>> {

    @NonNull
    T first;

    @NonNull
    T second;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public int compareTo(AbstractPair<T> o) {
        if ((first.equals(o.first) && second.equals(o.second))
                || (first.equals(o.second) && second.equals(o.first)))
            return 0;

        if (first instanceof Comparable<?> && o.first instanceof Comparable<?>) {

            Comparable thisFirst = (Comparable) first;
            Comparable thatFirst = (Comparable) o.first;
            Comparable thisSecond = (Comparable) second;
            Comparable thatSecond = (Comparable) o.second;

            int firstComp = thisFirst.compareTo(thatFirst);

            if (firstComp == 0)
                return thisSecond.compareTo(thatSecond);
            else
                return firstComp;

        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AbstractPair<T> other = (AbstractPair<T>) obj;

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
        int hash1 = first.hashCode();
        int hash2 = second.hashCode();
        return hash1 > hash2 ? hash1 * 31 + hash2 : hash2 * 31 + hash1;
    }

}
