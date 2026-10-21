package ua.logistics.util;

import ua.logistics.model.CargoType;
import ua.logistics.model.ShipmentStatus;
import ua.logistics.model.Waybill;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Public facade utility class for validation, normalization, and domain calculation methods.
 */
public class LogisticsUtils {

    private LogisticsUtils() {
        // Prevent instantiation
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

    // --- Switch Expression 1: Exhaustive switch over CargoType enum without default ---
    public static String getCargoDescription(CargoType cargoType) {
        return switch (cargoType) {
            case STANDARD -> "Standard non-perishable goods";
            case FRAGILE -> "Fragile items requiring careful handling";
            case HAZARDOUS -> "Hazardous materials requiring special permits";
            case PERISHABLE -> "Perishable goods requiring temperature control";
        };
    }

    // --- Switch Expression 2: Behavior selection / factor based on ShipmentStatus ---
    public static String getStatusDescription(ShipmentStatus status) {
        return switch (status) {
            case REGISTERED -> "The shipment has been registered in the system.";
            case IN_TRANSIT -> "The shipment is currently on its way to the destination.";
            case DELIVERED -> "The shipment has been successfully delivered to the recipient.";
            case RETURNED -> "The shipment was returned to the sender.";
        };
    }

    // --- Calculated Domain Methods ---

    public static long transitDays(Waybill waybill) {
        ValidationHelper.requireNotNull(waybill, "Waybill");
        return ChronoUnit.DAYS.between(waybill.dispatchDate(), waybill.estimatedArrival());
    }

    public static double loadPercent(Waybill waybill) {
        ValidationHelper.requireNotNull(waybill, "Waybill");
        double cargoWeight = waybill.shipment().getCargoWeightKg();
        double maxWeight = waybill.vehicle().maxWeightKg();
        return (cargoWeight / maxWeight) * 100.0;
    }
}