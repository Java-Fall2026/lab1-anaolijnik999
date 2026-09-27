package ua.logistics.model;

import ua.common.BaseEntity;
import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Waybill entity representing shipping documentation.
 * Business identity key (🔑❓) is defined as a combination of (shipment, dispatchDate).
 */
public class Waybill extends BaseEntity {

    private final Shipment shipment;
    private final Vehicle vehicle;
    private final LocalDate dispatchDate;
    private final LocalDate estimatedArrival;

    public Waybill(Shipment shipment, Vehicle vehicle, LocalDate dispatchDate, LocalDate estimatedArrival) {
        super();
        LogisticsUtils.validateNotNull(shipment, "Shipment");
        LogisticsUtils.validateNotNull(vehicle, "Vehicle");

        LogisticsUtils.validateVehicleCapacity(shipment.getCargoWeightKg(), vehicle.getMaxWeightKg());
        LogisticsUtils.validateDateOrder(shipment.getCreationDate(), dispatchDate, "Creation date", "Dispatch date");
        LogisticsUtils.validateDateOrder(dispatchDate, estimatedArrival, "Dispatch date", "Estimated arrival date");

        this.shipment = shipment;
        this.vehicle = vehicle;
        this.dispatchDate = dispatchDate;
        this.estimatedArrival = estimatedArrival;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public LocalDate getEstimatedArrival() {
        return estimatedArrival;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waybill waybill = (Waybill) o;
        return Objects.equals(shipment, waybill.shipment) && Objects.equals(dispatchDate, waybill.dispatchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipment, dispatchDate);
    }

    @Override
    public String toString() {
        return "Waybill{" +
                "shipment=" + shipment +
                ", vehicle=" + vehicle +
                ", dispatchDate=" + dispatchDate +
                ", estimatedArrival=" + estimatedArrival +
                ", createdAt=" + createdAt +
                '}';
    }
}
