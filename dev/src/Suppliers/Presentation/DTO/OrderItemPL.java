package Suppliers.Presentation.DTO;

public class OrderItemPL {
    private final String productName;
    private final String supplierBusinessNumber;
    private final String supplierName;
    private final int catalogId;
    private final int quantity;
    private final double priceBeforeDiscount;
    private final double totalPrice;

    public OrderItemPL(String productName, String supplierBusinessNumber, String supplierName, int catalogId, int quantity, double priceBeforeDiscount, double totalPrice) {
        this.productName = productName;
        this.supplierBusinessNumber = supplierBusinessNumber;
        this.supplierName = supplierName;
        this.catalogId = catalogId;
        this.quantity = quantity;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getSupplierBusinessNumber() {
        return supplierBusinessNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}