package ua.logistics.util;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Package-private helper class containing strict validation rules that throw IllegalArgumentException.
 */
class ValidationHelper {

    private ValidationHelper() {
        // Prevent instantiation
    }

    static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }

    static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank, got: '" + value + "'");
        }
    }

    static void requireStrictlyPositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be strictly greater than 0, got: " + value);
        }
    }

    static void requireNotFuture(LocalDate date, String fieldName) {
        requireNotNull(date, fieldName);
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " cannot be in the future, got: " + date);
        }
    }

    static void requireDateOrder(LocalDate startDate, LocalDate endDate, String startName, String endName) {
        requireNotNull(startDate, startName);
        requireNotNull(endDate, endName);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(endName + " (" + endDate + ") cannot be before " + startName + " (" + startDate + ")");
        }
    }

    static void requireInAllowedList(String value, Collection<String> allowed, String fieldName) {
        if (value == null || !allowed.contains(value)) {
            throw new IllegalArgumentException(fieldName + " must be one of " + allowed + ", got: '" + value + "'");
        }
    }
}