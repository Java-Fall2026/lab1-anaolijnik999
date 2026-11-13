package ua.logistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.logistics.exception.InvalidDataException;
import ua.logistics.loader.VehicleLoader;
import ua.logistics.model.*;
import ua.logistics.util.LogisticsUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

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
    @DisplayName("Test 4: Parse vehicle line throws InvalidDataException with Line N and cause")
    void testParseVehicleLineInvalid() {
        String invalidLine = "1HGCR2F83HA000000,MAN TGX,not_a_number";
        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            VehicleLoader.parseVehicleLine(invalidLine, 12);
        });
        assertTrue(ex.getMessage().contains("Line 12"));
        assertNotNull(ex.getCause());
        assertInstanceOf(NumberFormatException.class, ex.getCause());
    }

    @Test
    @DisplayName("Test 5: Load vehicles using @TempDir with valid and invalid rows")
    void testLoadVehiclesWithTempDir(@TempDir Path tempDir) throws Exception {
        Path csvFile = tempDir.resolve("test_vehicles.csv");
        String content = "vinCode,model,maxWeightKg\n" +
                         "1HGCR2F83HA000000,MAN TGX,10000.0\n" +
                         "INVALID_ROW_MISSING_COLUMNS\n" +
                         "2HGCR2F83HA111111,Volvo FH16,15000.0";
        Files.writeString(csvFile, content);

        List<Vehicle> vehicles = VehicleLoader.loadVehicles(csvFile);
        assertEquals(2, vehicles.size(), "Should successfully load 2 valid vehicles and skip 1 invalid");
        assertEquals("1HGCR2F83HA000000", vehicles.get(0).vinCode());
        assertEquals("2HGCR2F83HA111111", vehicles.get(1).vinCode());
    }

    @Test
    @DisplayName("Test 6: Calculated method loadPercent")
    void testLoadPercentCalculation() {
        Client client = new Client("12345678", "Logistics Ltd", "+380501112233");
        Vehicle vehicle = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);
        Shipment shipment = new Shipment("TRK-101", client, CargoType.STANDARD, 2500.0, LocalDate.now(), ShipmentStatus.REGISTERED);
        Waybill waybill = new Waybill(shipment, vehicle, LocalDate.now(), LocalDate.now().plusDays(2));

        double load = LogisticsUtils.loadPercent(waybill);
        assertEquals(25.0, load);
    }
}