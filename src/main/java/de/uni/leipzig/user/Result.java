package de.uni.leipzig.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    @SuppressWarnings("unchecked")
    public static <T> Result<T> empty() {
        return (Result<T>) new Result<>(null);
    }

    T value;

    public void fixValue(T value) {
        this.value = value;
    }

}
