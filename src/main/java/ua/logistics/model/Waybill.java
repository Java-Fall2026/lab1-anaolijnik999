package ua.logistics.model;

import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;

public record Waybill(Shipment shipment, Vehicle vehicle, LocalDate dispatchDate, LocalDate estimatedArrival) {
    public Waybill {
        LogisticsUtils.validateNotNull(shipment, "Shipment");
        LogisticsUtils.validateNotNull(vehicle, "Vehicle");

        // Використовуємо .maxWeightKg() замість .getMaxWeightKg()
        LogisticsUtils.validateVehicleCapacity(shipment.getCargoWeightKg(), vehicle.maxWeightKg());
        LogisticsUtils.validateDateOrder(shipment.getCreationDate(), dispatchDate, "Creation date", "Dispatch date");
        LogisticsUtils.validateDateOrder(dispatchDate, estimatedArrival, "Dispatch date", "Estimated arrival date");
    }
}