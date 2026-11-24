package ua.logistics.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.logistics.exception.DuplicateIdentityException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GenericRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);

    private final String name;
    private final IdentityExtractor<T> extractor;
    private final Map<String, T> storage = new LinkedHashMap<>();

    public GenericRepository(String name, IdentityExtractor<T> extractor) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Repository name must not be null or blank");
        }
        this.name = name;
        this.extractor = Objects.requireNonNull(extractor, "Identity extractor must not be null");
    }

    public String getName() {
        return name;
    }

    public void add(T item) {
        String identity = extractAndValidateIdentity(item);
        if (storage.containsKey(identity)) {
            DuplicateIdentityException ex = new DuplicateIdentityException(
                    "Duplicate identity '" + identity + "' in repository '" + name + "'"
            );
            log.error("Failed to add duplicate identity '{}' to repository '{}'", identity, name, ex);
            throw ex;
        }
        storage.put(identity, item);
        log.info("Added entity with identity '{}' to repository '{}'", identity, name);
    }

    public boolean addIfAbsent(T item) {
        String identity = extractAndValidateIdentity(item);
        if (storage.containsKey(identity)) {
            log.warn("Skipped duplicate identity '{}' in repository '{}'", identity, name);
            return false;
        }
        storage.put(identity, item);
        log.info("Added entity with identity '{}' to repository '{}'", identity, name);
        return true;
    }

    public void addAll(Collection<? extends T> items) {
        Objects.requireNonNull(items, "Items collection must not be null");
        for (T item : items) {
            add(item);
        }
    }

    public boolean removeByIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            log.warn("Attempted to remove by blank or null identity in repository '{}'", name);
            return false;
        }
        T removed = storage.remove(identity);
        if (removed == null) {
            log.warn("Entity with identity '{}' not found for removal in repository '{}'", identity, name);
            return false;
        }
        log.info("Removed entity with identity '{}' from repository '{}'", identity, name);
        return true;
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(identity));
    }

    public List<T> findAll() {
        return List.copyOf(storage.values());
    }

    public int size() {
        return storage.size();
    }

    private String extractAndValidateIdentity(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        String identity = extractor.identityOf(item);
        if (identity == null || identity.isBlank()) {
            throw new IllegalArgumentException("Extracted identity must not be null or blank");
        }
        return identity;
    }
}