package org.example.musicstore.core.common.exception;

public class NotFoundCoreException extends CoreException {
    private NotFoundCoreException(String message) {
        super(message);
    }

    public static NotFoundCoreException forKey(Object keyValue) {
        var message = String.format("Not found item with key '%s'", keyValue);
        return new NotFoundCoreException(message);
    }
}
