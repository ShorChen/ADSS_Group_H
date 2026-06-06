package Transportation.Domain.Entities;

@SuppressWarnings("ClassCanBeRecord")
public class TruckDL {
    private final String licenseNumber;
    private final String model;
    private final double netWeight;
    private final double maxWeight;
    private final boolean isRefrigerated;

    public TruckDL(String licenseNumber, String model, double netWeight, double maxWeight, boolean isRefrigerated) {
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.isRefrigerated = isRefrigerated;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public String getModel() { return model; }
    public double getNetWeight() { return netWeight; }
    public double getMaxWeight() { return maxWeight; }
    public boolean isRefrigerated() { return isRefrigerated; }

    public boolean isOverweight(double cargoWeight) {
        return !((netWeight + cargoWeight) <= maxWeight);
    }
}