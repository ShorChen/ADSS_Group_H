package Suppliers.Domain;

public class ProductLine {
    private final Product product;
    private final int supplierCatalogNumber;
    private double agreedPrice;
    private final int quantity;

    public ProductLine(Product product, int supplierCatalogNumber, double agreedPrice, int quantity) {
        this.product               = product;
        this.supplierCatalogNumber = supplierCatalogNumber;
        this.agreedPrice           = agreedPrice;
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }
    public Product getProduct() { return product; }
    public int getSupplierCatalogNumber() { return supplierCatalogNumber; }
    public double getAgreedPrice() { return agreedPrice; }
    public void setAgreedPrice(double price) { this.agreedPrice = price; }
}