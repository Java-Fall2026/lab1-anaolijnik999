package ua.logistics.util;

/**
 * Package-private helper class responsible for string normalization and formatting.
 */
class FormatHelper {

    private FormatHelper() {
        // Prevent instantiation
    }

    static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    static String normalizeUppercase(String value) {
        return normalize(value).toUpperCase();
    }
}