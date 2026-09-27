package ua.logistics.model;

import ua.common.BaseEntity;
import ua.logistics.util.LogisticsUtils;

import java.util.Objects;

/**
 * Vehicle entity with private constructor and static factory method.
 */
public class Vehicle extends BaseEntity {

    private final String vinCode;
    private final String model;
    private final double maxWeightKg;

    private Vehicle(String vinCode, String model, double maxWeightKg) {
        super();
        LogisticsUtils.validateNotBlank(vinCode, "VIN code");
        LogisticsUtils.validateNotBlank(model, "Vehicle model");
        LogisticsUtils.validateStrictlyPositive(maxWeightKg, "Vehicle max weight");

        this.vinCode = vinCode;
        this.model = model;
        this.maxWeightKg = maxWeightKg;
    }

    public static Vehicle of(String vinCode, String model, double maxWeightKg) {
        return new Vehicle(vinCode, model, maxWeightKg);
    }

    public String getVinCode() {
        return vinCode;
    }

    public String getModel() {
        return model;
    }

    public double getMaxWeightKg() {
        return maxWeightKg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vinCode, vehicle.vinCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vinCode);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vinCode='" + vinCode + '\'' +
                ", model='" + model + '\'' +
                ", maxWeightKg=" + maxWeightKg +
                ", createdAt=" + createdAt +
                '}';
    }
}