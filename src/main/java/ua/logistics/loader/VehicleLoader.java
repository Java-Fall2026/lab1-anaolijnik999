package ua.logistics.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.logistics.exception.InvalidDataException;
import ua.logistics.model.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VehicleLoader {
    private static final Logger log = LoggerFactory.getLogger(VehicleLoader.class);

    public static List<Vehicle> loadVehicles(Path filePath) {
        List<Vehicle> vehicles = new ArrayList<>();
        log.info("Starting to load vehicles from file: {}", filePath);

        if (!Files.exists(filePath)) {
            log.error("File does not exist: {}", filePath);
            return vehicles;
        }

        // try-with-resources
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    Vehicle vehicle = parseVehicleLine(line, lineNumber);
                    vehicles.add(vehicle);
                } catch (InvalidDataException | NumberFormatException e) { // multi-catch
                    log.warn("Skipping invalid record at line {}: {}", lineNumber, e.getMessage());
                }
            }
        } catch (IOException | SecurityException e) { // multi-catch
            log.error("I/O or security error occurred while reading file: {}", filePath, e);
        } finally {
            // finally block
            log.info("Finished processing file attempt for path: {}", filePath);
        }

        log.info("Loaded {} vehicles successfully", vehicles.size());
        return vehicles;
    }

    private static Vehicle parseVehicleLine(String line, int lineNumber) throws InvalidDataException {
        String[] parts = line.split(",", -1);
        if (parts.length < 3) {
            throw new InvalidDataException("Line " + lineNumber + ": Insufficient columns");
        }

        String vinCode = parts[0].trim();
        String model = parts[1].trim();
        double maxWeightKg;

        try {
            maxWeightKg = Double.parseDouble(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Line " + lineNumber + ": Invalid weight format", e);
        }

        try {
            return new Vehicle(vinCode, model, maxWeightKg);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Line " + lineNumber + ": Validation failed: " + e.getMessage(), e);
        }
    }
}