package Inventory.Domain.Entities;

@SuppressWarnings("ClassCanBeRecord")
public class LowStockAlert {
    private final String barcode;
    private final String productName;
    private final int currentTotal;
    private final int minRequired;

    public LowStockAlert(String barcode, String productName, int currentTotal, int minRequired) {
        this.barcode = barcode;
        this.productName = productName;
        this.currentTotal = currentTotal;
        this.minRequired = minRequired;
    }

    public String getBarcode() { return barcode; }
    public String getProductName() { return productName; }
    public int getCurrentTotal() { return currentTotal; }
    public int getMinRequired() { return minRequired; }
}