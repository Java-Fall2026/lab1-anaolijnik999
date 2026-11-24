package ua.logistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.logistics.exception.DuplicateIdentityException;
import ua.logistics.model.CargoType;
import ua.logistics.model.Client;
import ua.logistics.model.Shipment;
import ua.logistics.model.ShipmentStatus;
import ua.logistics.model.Vehicle;
import ua.logistics.repository.GenericRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryLab4Test {

    @Test
    @DisplayName("Add and findByIdentity returns added item")
    void testAddAndFindByIdentity() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Vehicles", Vehicle::vinCode);
        Vehicle v = new Vehicle("VIN001", "MAN TGX", 10000.0);
        repo.add(v);

        Optional<Vehicle> found = repo.findByIdentity("VIN001");
        assertTrue(found.isPresent());
        assertEquals(v, found.get());
    }

    @Test
    @DisplayName("findByIdentity returns empty Optional when identity is absent")
    void testFindByIdentityMissing() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Vehicles", Vehicle::vinCode);
        assertTrue(repo.findByIdentity("UNKNOWN").isEmpty());
    }

    @Test
    @DisplayName("Adding null or blank identity throws IllegalArgumentException")
    void testNullAndBlankIdentityThrows() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Vehicles", Vehicle::vinCode);
        assertThrows(IllegalArgumentException.class, () -> repo.add(null));

        GenericRepository<Vehicle> blankExtractorRepo = new GenericRepository<>("BlankIdRepo", v -> "   ");
        Vehicle validVehicle = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);
        assertThrows(IllegalArgumentException.class, () -> blankExtractorRepo.add(validVehicle));
    }

    @Test
    @DisplayName("Duplicate add throws DuplicateIdentityException with identity and repo name")
    void testDuplicateAddThrows() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Fleet", Vehicle::vinCode);
        Vehicle v1 = new Vehicle("VIN001", "MAN TGX", 10000.0);
        Vehicle v2 = new Vehicle("VIN001", "Volvo FH", 15000.0);
        repo.add(v1);

        DuplicateIdentityException ex = assertThrows(DuplicateIdentityException.class, () -> repo.add(v2));
        assertTrue(ex.getMessage().contains("VIN001"));
        assertTrue(ex.getMessage().contains("Fleet"));
    }

    @Test
    @DisplayName("addIfAbsent returns false and keeps size unchanged for duplicate")
    void testAddIfAbsentDuplicate() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Fleet", Vehicle::vinCode);
        Vehicle v1 = new Vehicle("VIN001", "MAN TGX", 10000.0);
        Vehicle v2 = new Vehicle("VIN001", "Volvo FH", 15000.0);

        assertTrue(repo.addIfAbsent(v1));
        assertFalse(repo.addIfAbsent(v2));
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("removeByIdentity returns true for existing and false for missing item")
    void testRemoveByIdentity() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Fleet", Vehicle::vinCode);
        repo.add(new Vehicle("VIN001", "MAN TGX", 10000.0));

        assertTrue(repo.removeByIdentity("VIN001"));
        assertFalse(repo.removeByIdentity("VIN001"));
        assertEquals(0, repo.size());
    }

    @Test
    @DisplayName("Modifying list returned by findAll does not affect repository state")
    void testFindAllImmutability() {
        GenericRepository<Vehicle> repo = new GenericRepository<>("Fleet", Vehicle::vinCode);
        repo.add(new Vehicle("VIN001", "MAN TGX", 10000.0));

        List<Vehicle> snapshot = repo.findAll();
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("Same GenericRepository class works with multiple distinct domain types")
    void testMultipleDomainTypes() {
        GenericRepository<Client> clientRepo = new GenericRepository<>("Clients", Client::taxId);
        GenericRepository<Shipment> shipmentRepo = new GenericRepository<>("Shipments", Shipment::getTrackingNumber);

        Client client = new Client("11223344", "Acme Logistics", "+380670001122");
        Shipment shipment = new Shipment(
                "SHP-55", client, CargoType.FRAGILE, 500.0, LocalDate.now(), ShipmentStatus.IN_TRANSIT
        );

        clientRepo.add(client);
        shipmentRepo.add(shipment);

        assertEquals(1, clientRepo.size());
        assertEquals(1, shipmentRepo.size());
        assertEquals(client, clientRepo.findByIdentity("11223344").orElseThrow());
        assertEquals(shipment, shipmentRepo.findByIdentity("SHP-55").orElseThrow());
    }
}