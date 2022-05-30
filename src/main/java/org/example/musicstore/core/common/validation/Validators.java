package org.example.musicstore.core.common.validation;

import java.util.List;
import java.util.function.Supplier;

public class Validators {
    public static <E extends Throwable> void checkEmpty(String value, Supplier<E> supplier) throws E {
        if (value == null || value.trim().isEmpty()) {
            throw supplier.get();
        }
    }

    public static <T, E extends Throwable> void checkDuplicate(List<T> items, T item, Supplier<E> supplier) throws E {
        if (items.contains(item)) {
            throw supplier.get();
        }
    }

    public static <E extends Throwable> void checkCondition(boolean condition, Supplier<E> supplier) throws E {
        if (condition) {
            throw supplier.get();
        }
    }
}
