package org.example.musicstore.core.common.exception;

public class ValidationCoreException extends CoreException {
    private ValidationCoreException(String message) {
        super(message);
    }

    public static ValidationCoreException forEmptyField(String name) {
        return new ValidationCoreException(String.format("Cannot perform operation, the field '%s' is empty", name));
    }
}
