package org.example.musicstore.core.common.exception;

public class DuplicatedItemCoreException extends CoreException {
    private DuplicatedItemCoreException(String message) {
        super(message);
    }

    public static DuplicatedItemCoreException forKey(Object value) {
        var message = String.format("Cannot perform the operation, there already exists an item with key '%s'", value);
        return new DuplicatedItemCoreException(message);
    }
}
