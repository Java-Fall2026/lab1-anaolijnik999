package ua.logistics;

import ua.logistics.model.CargoType;
import ua.logistics.model.Client;
import ua.logistics.model.Shipment;
import ua.logistics.model.ShipmentStatus;
import ua.logistics.model.Vehicle;
import ua.logistics.model.Waybill;
import ua.logistics.util.LogisticsUtils;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== 1. Record Creation & Accessing Components ===");
        Client client1 = new Client("12345678", "Logistics Plus Ltd", "+380501112233");
        Client client2 = new Client("12345678", "Logistics Plus Ltd", "+380501112233");
        Client client3 = new Client("87654321", "Cargo Express LLC", "+380673334455");

        Vehicle vehicle1 = new Vehicle("1HGCR2F83HA000000", "MAN TGX", 10000.0);

        Shipment shipment1 = new Shipment(
                "TRK-1001",
                client1,
                CargoType.FRAGILE,
                2500.0,
                LocalDate.now().minusDays(2),
                ShipmentStatus.REGISTERED
        );

        Waybill waybill1 = new Waybill(
                shipment1,
                vehicle1,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3)
        );

        // Accessing record components using generated accessor methods (e.g., taxId() instead of getTaxId())
        System.out.println("Client Tax ID (via record accessor): " + client1.taxId());
        System.out.println("Created Shipment: " + shipment1);
        System.out.println("Created Waybill: " + waybill1);

        System.out.println("\n=== 2. Enum in Action ===");
        System.out.println("Available Cargo Types:");
        for (CargoType type : CargoType.values()) {
            System.out.println(" - " + type + " with Risk Factor: " + type.getRiskFactor());
        }

        try {
            System.out.print("Testing valueOf with invalid constant: ");
            CargoType.valueOf("INVALID_TYPE");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception -> " + e.getMessage());
        }

        System.out.println("\n=== 3. Switch Expressions Demonstration ===");
        System.out.println("Switch 1 (CargoType description): " + LogisticsUtils.getCargoDescription(CargoType.FRAGILE));
        System.out.println("Switch 2 (Status description): " + LogisticsUtils.getStatusDescription(ShipmentStatus.IN_TRANSIT));

        System.out.println("\n=== 4. Validation Failures in Record Compact Constructors ===");
        try {
            System.out.print("Attempting to create Client with blank Tax ID: ");
            new Client("", "Empty Tax ID Corp", "+380500000000");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception -> " + e.getMessage());
        }

        System.out.println("\n=== 5. Record equals & hashCode (Auto-generated) ===");
        System.out.println("client1.equals(client2) [same data]: " + client1.equals(client2));
        System.out.println("client1.hashCode() == client2.hashCode(): " + (client1.hashCode() == client2.hashCode()));
        System.out.println("client1.equals(client3) [different data]: " + client1.equals(client3));

        System.out.println("\n=== 6. Domain Calculated Methods ===");
        System.out.println("Transit days: " + LogisticsUtils.transitDays(waybill1));
        System.out.println("Vehicle load percentage: " + LogisticsUtils.loadPercent(waybill1) + "%");

        System.out.println("\n=== 7. Output Record via Generated toString() ===" );
        System.out.println(client1);
    }
}