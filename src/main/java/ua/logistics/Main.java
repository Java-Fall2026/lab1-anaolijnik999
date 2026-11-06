package ua.logistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.logistics.loader.VehicleLoader;
import ua.logistics.model.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Application started.");

        System.out.println("=== 1. Reading CSV File with Valid and Invalid Rows ===");
        Path validFile = Path.of("data", "vehicles.csv");
        List<Vehicle> loadedVehicles = VehicleLoader.loadVehicles(validFile);
        System.out.println("Successfully loaded vehicles count: " + loadedVehicles.size());
        loadedVehicles.forEach(v -> System.out.println(" - " + v));

        System.out.println("\n=== 2. Attempting to Read Missing File ===");
        Path missingFile = Path.of("data", "non_existent.csv");
        List<Vehicle> missingVehicles = VehicleLoader.loadVehicles(missingFile);
        System.out.println("Vehicles from missing file count: " + missingVehicles.size());

        System.out.println("\n=== 3. Incorrect Value in Setter and Exception Handling ===");
        Client client = new Client("12345678", "Logistics Plus Ltd", "+380501112233");
        Shipment shipment = new Shipment("TRK-2001", client, CargoType.STANDARD, 1000.0, LocalDate.now(), ShipmentStatus.REGISTERED);
        System.out.println("Initial shipment status: " + shipment.getStatus());

        try {
            System.out.print("Attempting to set invalid status (null): ");
            shipment.setStatus(null);
        } catch (IllegalArgumentException e) {
            log.error("Caught expected validation error in setter: {}", e.getMessage());
            System.out.println("Caught exception -> " + e.getMessage());
        }

        System.out.println("\n=== 4. Logging Levels Demonstration ===");
        log.info("This is an INFO level log: Application workflow is proceeding normally.");
        log.warn("This is a WARN level log: Potential issue detected (e.g., invalid data skipped).");
        log.error("This is an ERROR level log: Critical error or invalid operation encountered.");

        log.info("Application finished successfully.");
    }
}