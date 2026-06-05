package Inventory.Domain.Entities;

import Inventory.Domain.ValueObjects.ShelfLocation;
import Inventory.Domain.ValueObjects.Warehouse;

public class ProductDL {
    private final String barcode;
    private final String name;
    private final String manufacturer;
    private final int categoryId;
    private final double costPrice;
    private double sellingPrice;
    private int minQuantity;
    private int shelfQuantity;
    private int warehouseQuantity;
    private final ShelfLocation shelfLocation;
    private final Warehouse warehouse;

    public ProductDL(String barcode, String name, String manufacturer, int categoryId, double costPrice, double sellingPrice, int minQuantity, int shelfQuantity, int warehouseQuantity, String aisle, int position) {
        this.barcode = barcode;
        this.name = name;
        this.manufacturer = manufacturer;
        this.categoryId = categoryId;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.minQuantity = minQuantity;
        this.shelfQuantity = shelfQuantity;
        this.warehouseQuantity = warehouseQuantity;
        this.shelfLocation = new ShelfLocation(aisle, position);
        this.warehouse = new Warehouse("Main Warehouse");
    }

    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public int getCategoryId() { return categoryId; }
    public double getCostPrice() { return costPrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public int getShelfQuantity() { return shelfQuantity; }
    public void setShelfQuantity(int shelfQuantity) { this.shelfQuantity = shelfQuantity; }

    public int getWarehouseQuantity() { return warehouseQuantity; }
    public void setWarehouseQuantity(int warehouseQuantity) { this.warehouseQuantity = warehouseQuantity; }

    public String getAisle() { return shelfLocation.getAisle(); }
    public int getPosition() { return shelfLocation.getPosition(); }

    @SuppressWarnings("unused")
    public Warehouse getWarehouse() { return warehouse; }

    public int getTotalQuantity() {
        return shelfQuantity + warehouseQuantity;
    }

    public boolean requiresRestockAlert() {
        return getTotalQuantity() < minQuantity;
    }
}