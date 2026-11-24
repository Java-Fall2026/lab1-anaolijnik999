package ua.logistics.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String identityOf(T item);
}