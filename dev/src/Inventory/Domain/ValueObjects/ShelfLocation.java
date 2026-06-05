package Inventory.Domain.ValueObjects;

public class ShelfLocation {
    private String aisle;
    private int position;

    public ShelfLocation(String aisle, int position) {
        this.aisle = aisle;
        this.position = position;
    }

    public String getAisle() { return aisle; }

    public int getPosition() { return position; }

    @SuppressWarnings("unused")
    public void setAisle(String aisle) { this.aisle = aisle; }

    @SuppressWarnings("unused")
    public void setPosition(int position) { this.position = position; }
}