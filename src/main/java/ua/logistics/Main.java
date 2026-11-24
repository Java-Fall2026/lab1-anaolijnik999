package ua.logistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.logistics.exception.DuplicateIdentityException;
import ua.logistics.loader.VehicleLoader;
import ua.logistics.model.CargoType;
import ua.logistics.model.Client;
import ua.logistics.model.Shipment;
import ua.logistics.model.ShipmentStatus;
import ua.logistics.model.Vehicle;
import ua.logistics.repository.GenericRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting repository demonstration");

        GenericRepository<Vehicle> vehicleRepo = new GenericRepository<>("Vehicles", Vehicle::vinCode);
        GenericRepository<Client> clientRepo = new GenericRepository<>("Clients", Client::taxId);
        GenericRepository<Shipment> shipmentRepo = new GenericRepository<>("Shipments", Shipment::getTrackingNumber);

        Client client1 = new Client("12345678", "Cargo Plus", "+380501112233");
        clientRepo.add(client1);

        Shipment shipment1 = new Shipment(
                "TRK-100", client1, CargoType.STANDARD, 1200.0, LocalDate.now(), ShipmentStatus.REGISTERED
        );
        shipmentRepo.add(shipment1);

        System.out.println("=== 1. Repository lookup (existing and missing) ===");
        Optional<Client> foundClient = clientRepo.findByIdentity("12345678");
        Optional<Client> missingClient = clientRepo.findByIdentity("99999999");
        System.out.println("Existing client found: " + foundClient.isPresent());
        System.out.println("Missing client found: " + missingClient.isPresent());

        System.out.println("\n=== 2. Populating vehicle repository from CSV ===");
        List<Vehicle> parsedVehicles = VehicleLoader.loadVehicles(Path.of("data", "vehicles.csv"));
        for (Vehicle v : parsedVehicles) {
            vehicleRepo.addIfAbsent(v);
        }
        System.out.println("Vehicles in repository: " + vehicleRepo.size());

        System.out.println("\n=== 3. Duplicates: add vs addIfAbsent ===");
        if (!parsedVehicles.isEmpty()) {
            Vehicle existing = parsedVehicles.get(0);
            boolean addedAgain = vehicleRepo.addIfAbsent(existing);
            System.out.println("addIfAbsent result for duplicate: " + addedAgain);

            try {
                vehicleRepo.add(existing);
            } catch (DuplicateIdentityException ex) {
                System.out.println("Caught exception on add: " + ex.getMessage());
            }
        }

        System.out.println("\n=== 4. Removing by identity ===");
        boolean removedExisting = shipmentRepo.removeByIdentity("TRK-100");
        boolean removedMissing = shipmentRepo.removeByIdentity("TRK-UNKNOWN");
        System.out.println("Removed existing shipment: " + removedExisting);
        System.out.println("Removed missing shipment: " + removedMissing);

        System.out.println("\n=== 5. Logging levels summary ===");
        log.info("Completed repository demonstration steps");
        log.warn("Duplicate or missing identity cases were observed during execution");
    }
}