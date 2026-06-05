package Inventory.Domain.ValueObjects;

@SuppressWarnings("ClassCanBeRecord")
public class Warehouse {
    private final String locationName;

    public Warehouse(String locationName) {
        this.locationName = locationName;
    }

    @SuppressWarnings("unused")
    public String getLocationName() { return locationName; }
}