package ua.logistics.util;

import ua.logistics.model.Waybill;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * Public facade utility class for validation, normalization, and domain calculation methods.
 */
public class LogisticsUtils {

    // Package-private domain constants
    static final Set<String> ALLOWED_CARGO_TYPES = Set.of("STANDARD", "FRAGILE", "HAZARDOUS", "PERISHABLE");
    static final Set<String> ALLOWED_STATUSES = Set.of("REGISTERED", "IN_TRANSIT", "DELIVERED", "RETURNED");

    private LogisticsUtils() {
        // Prevent instantiation
    }

    public static String checkAndNormalizeCargoType(String cargoType) {
        String normalized = FormatHelper.normalizeUppercase(cargoType);
        ValidationHelper.requireInAllowedList(normalized, ALLOWED_CARGO_TYPES, "Cargo type");
        return normalized;
    }

    public static String checkAndNormalizeStatus(String status) {
        String normalized = FormatHelper.normalizeUppercase(status);
        ValidationHelper.requireInAllowedList(normalized, ALLOWED_STATUSES, "Status");
        return normalized;
    }

    public static void validateNotNull(Object value, String fieldName) {
        ValidationHelper.requireNotNull(value, fieldName);
    }

    public static void validateNotBlank(String value, String fieldName) {
        ValidationHelper.requireNotBlank(value, fieldName);
    }

    public static void validateStrictlyPositive(double value, String fieldName) {
        ValidationHelper.requireStrictlyPositive(value, fieldName);
    }

    public static void validateNotFutureDate(LocalDate date, String fieldName) {
        ValidationHelper.requireNotFuture(date, fieldName);
    }

    public static void validateDateOrder(LocalDate startDate, LocalDate endDate, String startName, String endName) {
        ValidationHelper.requireDateOrder(startDate, endDate, startName, endName);
    }

    public static void validateVehicleCapacity(double cargoWeight, double maxWeight) {
        if (cargoWeight > maxWeight) {
            throw new IllegalArgumentException(
                "Cargo weight (" + cargoWeight + " kg) exceeds vehicle max capacity (" + maxWeight + " kg)"
            );
        }
    }

    // --- Calculated Domain Methods ---

    /**
     * Calculates the duration of transit in days between dispatch and estimated arrival.
     */
    public static long transitDays(Waybill waybill) {
        ValidationHelper.requireNotNull(waybill, "Waybill");
        return ChronoUnit.DAYS.between(waybill.getDispatchDate(), waybill.getEstimatedArrival());
    }

    /**
     * Calculates vehicle load capacity percentage based on cargo weight and vehicle max capacity.
     */
    public static double loadPercent(Waybill waybill) {
        ValidationHelper.requireNotNull(waybill, "Waybill");
        double cargoWeight = waybill.getShipment().getCargoWeightKg();
        double maxWeight = waybill.getVehicle().getMaxWeightKg();
        return (cargoWeight / maxWeight) * 100.0;
    }
}