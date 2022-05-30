package org.example.musicstore.core.common.validation;

public interface ObjectValidator<T, E extends Throwable> {
    void validate(T item) throws E;
}
