package ua.logistics.model;

import ua.common.BaseEntity;
import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Shipment entity with mutable status field (marked with ✎).
 */
public class Shipment extends BaseEntity {

    private final String trackingNumber;
    private final Client sender;
    private final String cargoType;
    private final double cargoWeightKg;
    private final LocalDate creationDate;
    private String status;

    public Shipment(String trackingNumber, Client sender, String cargoType, double cargoWeightKg, LocalDate creationDate, String status) {
        super();
        LogisticsUtils.validateNotBlank(trackingNumber, "Tracking number");
        LogisticsUtils.validateNotNull(sender, "Sender");
        LogisticsUtils.validateStrictlyPositive(cargoWeightKg, "Cargo weight");
        LogisticsUtils.validateNotFutureDate(creationDate, "Creation date");

        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.cargoType = LogisticsUtils.checkAndNormalizeCargoType(cargoType);
        this.cargoWeightKg = cargoWeightKg;
        this.creationDate = creationDate;

        setStatus(status);
    }

    public final void setStatus(String status) {
        this.status = LogisticsUtils.checkAndNormalizeStatus(status);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Client getSender() {
        return sender;
    }

    public String getCargoType() {
        return cargoType;
    }

    public double getCargoWeightKg() {
        return cargoWeightKg;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(trackingNumber, shipment.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackingNumber);
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", sender=" + sender +
                ", cargoType='" + cargoType + '\'' +
                ", cargoWeightKg=" + cargoWeightKg +
                ", creationDate=" + creationDate +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
