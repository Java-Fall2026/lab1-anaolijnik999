package ua.logistics.repository;

import ua.logistics.model.Vehicle;

import java.util.List;
import java.util.Optional;

public class SearchBenchmark {

    private static final int DATASET_SIZE = 100_000;
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASURED_ITERATIONS = 15;

    private SearchBenchmark() {
    }

    public static void main(String[] args) {
        IdentityExtractor<Vehicle> vinExtractor = Vehicle::vinCode;
        GenericRepository<Vehicle> repository = new GenericRepository<>("BenchmarkVehicles", vinExtractor);

        for (int i = 0; i < DATASET_SIZE; i++) {
            String vin = String.format("VIN%08d", i);
            repository.add(new Vehicle(vin, "Actros", 18000.0));
        }

        List<Vehicle> itemsList = repository.findAll();
        String firstKey = String.format("VIN%08d", 0);
        String lastKey = String.format("VIN%08d", DATASET_SIZE - 1);
        String missingKey = "VIN99999999";

        for (int w = 0; w < WARMUP_ITERATIONS; w++) {
            repository.findByIdentity(firstKey);
            repository.findByIdentity(lastKey);
            repository.findByIdentity(missingKey);
            linearSearch(itemsList, firstKey, vinExtractor);
            linearSearch(itemsList, lastKey, vinExtractor);
            linearSearch(itemsList, missingKey, vinExtractor);
        }

        long mapFirstNs = measureMinNanos(() -> repository.findByIdentity(firstKey));
        long listFirstNs = measureMinNanos(() -> linearSearch(itemsList, firstKey, vinExtractor));

        long mapLastNs = measureMinNanos(() -> repository.findByIdentity(lastKey));
        long listLastNs = measureMinNanos(() -> linearSearch(itemsList, lastKey, vinExtractor));

        long mapMissingNs = measureMinNanos(() -> repository.findByIdentity(missingKey));
        long listMissingNs = measureMinNanos(() -> linearSearch(itemsList, missingKey, vinExtractor));

        System.out.printf("First element   | Map: %,d ns | List: %,d ns%n", mapFirstNs, listFirstNs);
        System.out.printf("Last element    | Map: %,d ns | List: %,d ns%n", mapLastNs, listLastNs);
        System.out.printf("Missing element | Map: %,d ns | List: %,d ns%n", mapMissingNs, listMissingNs);
    }

    private static long measureMinNanos(Runnable operation) {
        long best = Long.MAX_VALUE;
        for (int i = 0; i < MEASURED_ITERATIONS; i++) {
            long start = System.nanoTime();
            operation.run();
            long elapsed = System.nanoTime() - start;
            if (elapsed < best) {
                best = elapsed;
            }
        }
        return best;
    }

    private static <E> Optional<E> linearSearch(
            List<E> items, String identity, IdentityExtractor<E> extractor) {
        for (E item : items) {
            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}