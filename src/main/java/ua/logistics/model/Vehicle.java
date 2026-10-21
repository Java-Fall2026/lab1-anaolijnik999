package ua.logistics.model;

import ua.logistics.util.LogisticsUtils;

public record Vehicle(String vinCode, String model, double maxWeightKg) {
    public Vehicle {
        LogisticsUtils.validateNotBlank(vinCode, "VIN code");
        LogisticsUtils.validateNotBlank(model, "Vehicle model");
        LogisticsUtils.validateStrictlyPositive(maxWeightKg, "Vehicle max weight");
    }
}