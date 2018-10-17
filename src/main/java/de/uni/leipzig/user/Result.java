package de.uni.leipzig.user;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    @SuppressWarnings("unchecked")
    public static <T> Result<T> empty() {
        return (Result<T>) new Result<>(null);
    }

    private T value;

    public Result<T> fixValue(T value) {
        if (this.value == null)
            this.value = value;
        return this;
    }

}
