package org.example.musicstore.core.common.exception;

public class KeyConstraintCoreException extends CoreException {
    private KeyConstraintCoreException(String message) {
        super(message);
    }

    public static KeyConstraintCoreException whenDeleting(Object keyValue) {
        var message = String.format("Cannot delete item because the key '%s' is referenced by another entity.", keyValue);
        return new KeyConstraintCoreException(message);
    }

    public static KeyConstraintCoreException whenAddingToCollection(Object keyValue) {
        var message = String.format("Cannot add item to collection because the key '%s' does not fulfill the constraint.", keyValue);
        return new KeyConstraintCoreException(message);
    }
}
