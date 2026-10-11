package ua.logistics.model;

/**
 * Enum representing cargo types with an associated risk or price factor.
 */
public enum CargoType {
    STANDARD(1.0),
    FRAGILE(1.5),
    HAZARDOUS(2.0),
    PERISHABLE(1.8);

    private final double riskFactor;

    CargoType(double riskFactor) {
        this.riskFactor = riskFactor;
    }

    public double getRiskFactor() {
        return riskFactor;
    }
}
