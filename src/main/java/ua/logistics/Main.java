package ua.logistics;

import ua.logistics.model.Client;
import ua.logistics.model.Shipment;
import ua.logistics.model.Vehicle;
import ua.logistics.model.Waybill;
import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== 1. Object Creation (Constructor vs Static Factory Method) ===");
        // Client and Vehicle created via static factory methods of(...)
        Client client1 = Client.of("12345678", "Logistics Plus Ltd", "+380501112233");
        Client client2 = Client.of("12345678", "Logistics Plus Branch", "+380509998877");
        Client client3 = Client.of("87654321", "Cargo Express LLC", "+380673334455");

        Vehicle vehicle1 = Vehicle.of("1HGCR2F83HA000000", "MAN TGX", 10000.0);

        // Shipment and Waybill created via constructors
        Shipment shipment1 = new Shipment(
                "TRK-1001",
                client1,
                "  fragile ",
                2500.0,
                LocalDate.now().minusDays(2),
                " registered "
        );

        Waybill waybill1 = new Waybill(
                shipment1,
                vehicle1,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3)
        );

        System.out.println("Created Client: " + client1);
        System.out.println("Created Shipment: " + shipment1);
        System.out.println("Created Waybill: " + waybill1);

        System.out.println("\n=== 2. Normalization in Action ===");
        System.out.println("Cargo type normalized: '" + shipment1.getCargoType() + "' (Expected: FRAGILE)");
        System.out.println("Status normalized: '" + shipment1.getStatus() + "' (Expected: REGISTERED)");

        System.out.println("\n=== 3. Validation Failures (try/catch) ===");

        try {
            System.out.print("Test 1 (Overweight cargo for vehicle): ");
            Shipment heavyShipment = new Shipment("TRK-9999", client1, "STANDARD", 15000.0, LocalDate.now(), "REGISTERED");
            new Waybill(heavyShipment, vehicle1, LocalDate.now(), LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception -> " + e.getMessage());
        }

        try {
            System.out.print("Test 2 (Estimated arrival before dispatch date): ");
            new Waybill(shipment1, vehicle1, LocalDate.now(), LocalDate.now().minusDays(2));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception -> " + e.getMessage());
        }

        try {
            System.out.print("Test 3 (Invalid enum status): ");
            new Shipment("TRK-1002", client1, "STANDARD", 500.0, LocalDate.now(), "INVALID_STATUS");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception -> " + e.getMessage());
        }

        System.out.println("\n=== 4. Updating Object via Setter ===");
        System.out.println("Status before update: " + shipment1.getStatus());
        shipment1.setStatus(" in_transit ");
        System.out.println("Status after valid update: " + shipment1.getStatus());

        try {
            System.out.print("Attempting invalid status update: ");
            shipment1.setStatus("UNKNOWN_STATUS");
        } catch (IllegalArgumentException e) {
            System.out.println("Error -> " + e.getMessage());
            System.out.println("Status remained unchanged: " + shipment1.getStatus());
        }

        System.out.println("\n=== 5. Object Identity & Equality (== vs equals vs hashCode) ===");
        System.out.println("client1 == client2: " + (client1 == client2));
        System.out.println("client1.equals(client2) [same taxId]: " + client1.equals(client2));
        System.out.println("client1.hashCode() == client2.hashCode(): " + (client1.hashCode() == client2.hashCode()));

        System.out.println("client1.equals(client3) [different taxId]: " + client1.equals(client3));

        System.out.println("\n=== 6. Domain Calculated Methods ===");
        System.out.println("Transit days: " + LogisticsUtils.transitDays(waybill1));
        System.out.println("Vehicle load percentage: " + LogisticsUtils.loadPercent(waybill1) + "%");

        System.out.println("\n=== 7. Output All Entities via toString() ===");
        System.out.println(client1);
        System.out.println(vehicle1);
        System.out.println(shipment1);
        System.out.println(waybill1);

        System.out.println("\n=== 8. Access Control Checks (Commented non-compilable code) ===");
        // Un-commenting lines below causes compilation errors:

        // 1. Direct call to package-private class outside package:
        // ua.logistics.util.ValidationHelper.requireNotBlank("test", "field");
        // Compilation error: ValidationHelper is not public in ua.logistics.util; cannot be accessed from outside package.

        // 2. Direct call to private constructor:
        // Client c = new Client("123", "Test", "123");
        // Compilation error: Client(java.lang.String, java.lang.String, java.lang.String) has private access in ua.logistics.model.Client.
    }
}
