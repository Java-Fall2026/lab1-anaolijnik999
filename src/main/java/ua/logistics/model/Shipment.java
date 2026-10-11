package ua.logistics.model;

import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;
import java.util.Objects;

public class Shipment {

    private final String trackingNumber;
    private final Client sender;
    private final CargoType cargoType;
    private final double cargoWeightKg;
    private final LocalDate creationDate;
    private ShipmentStatus status;

    public Shipment(String trackingNumber, Client sender, CargoType cargoType, double cargoWeightKg, LocalDate creationDate, ShipmentStatus status) {
        LogisticsUtils.validateNotBlank(trackingNumber, "Tracking number");
        LogisticsUtils.validateNotNull(sender, "Sender");
        LogisticsUtils.validateNotNull(cargoType, "Cargo type");
        LogisticsUtils.validateStrictlyPositive(cargoWeightKg, "Cargo weight");
        LogisticsUtils.validateNotFutureDate(creationDate, "Creation date");

        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.cargoType = cargoType;
        this.cargoWeightKg = cargoWeightKg;
        this.creationDate = creationDate;
        setStatus(status);
    }

    public final void setStatus(ShipmentStatus status) {
        LogisticsUtils.validateNotNull(status, "Status");
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Client getSender() {
        return sender;
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public double getCargoWeightKg() {
        return cargoWeightKg;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public ShipmentStatus getStatus() {
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
                ", cargoType=" + cargoType +
                ", cargoWeightKg=" + cargoWeightKg +
                ", creationDate=" + creationDate +
                ", status=" + status +
                '}';
    }
}