package ua.logistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.logistics.exception.InvalidDataException;
import ua.logistics.model.*;
import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LogisticsTest {

    @Test
    @DisplayName("Test 1: Correct object creation")
    void testCorrectObjectCreation() {
        Vehicle vehicle = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);
        assertNotNull(vehicle);
        assertEquals("1HGCR2F83HA000000", vehicle.vinCode());
        assertEquals(10000.0, vehicle.maxWeightKg());
    }

    @Test
    @DisplayName("Test 2: IllegalArgumentException on invalid vehicle weight")
    void testInvalidVehicleWeight() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Vehicle("1HGCR2F83HA000000", "MAN TGX", -500.0);
        });
    }

    @Test
    @DisplayName("Test 3: InvalidDataException creation with message and cause")
    void testInvalidDataExceptionWithCause() {
        Throwable cause = new NumberFormatException("Bad number");
        InvalidDataException ex = new InvalidDataException("Line 5 error", cause);
        assertEquals("Line 5 error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    @DisplayName("Test 4: Calculated method loadPercent")
    void testLoadPercentCalculation() {
        Client client = new Client("12345678", "Logistics Ltd", "+380501112233");
        Vehicle vehicle = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);
        Shipment shipment = new Shipment("TRK-101", client, CargoType.STANDARD, 2500.0, LocalDate.now(), ShipmentStatus.REGISTERED);
        Waybill waybill = new Waybill(shipment, vehicle, LocalDate.now(), LocalDate.now().plusDays(2));

        double load = LogisticsUtils.loadPercent(waybill);
        assertEquals(25.0, load);
    }

    @Test
    @DisplayName("Test 5: Calculated method transitDays")
    void testTransitDaysCalculation() {
        Client client = new Client("12345678", "Logistics Ltd", "+380501112233");
        Vehicle vehicle = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);
        Shipment shipment = new Shipment("TRK-101", client, CargoType.STANDARD, 2500.0, LocalDate.now(), ShipmentStatus.REGISTERED);
        Waybill waybill = new Waybill(shipment, vehicle, LocalDate.now(), LocalDate.now().plusDays(4));

        long days = LogisticsUtils.transitDays(waybill);
        assertEquals(4L, days);
    }

    @Test
    @DisplayName("Test 6: Switch expression for CargoType description")
    void testCargoDescriptionSwitch() {
        String desc = LogisticsUtils.getCargoDescription(CargoType.FRAGILE);
        assertNotNull(desc);
        assertTrue(desc.contains("Fragile"));
    }
}