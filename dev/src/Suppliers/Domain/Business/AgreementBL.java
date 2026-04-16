package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class AgreementBL {
    private static int idCounter = 0;
    private final int agreementId;
    private final LocalDate startDate;
    private final DeliveryTermsBL deliveryTerms;
    private final List<ProductLineBL> productLines;
    private final DiscountPolicyBL discountPolicy;

    AgreementBL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.agreementId = ++idCounter;
        this.startDate = LocalDate.now();
        this.deliveryTerms = new DeliveryTermsBL(fixedDeliveryDays, supplierTransports);
        this.productLines = new ArrayList<>();
        this.discountPolicy = new DiscountPolicyBL();
    }

    public ProductLineBL addProductLine(int supplierCatalogId, String name, double basePrice, int quantity) {
        if (basePrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        for (ProductLineBL pl : productLines) {
            if (pl.getSupplierCatalogId() == supplierCatalogId) throw new IllegalArgumentException("Product line already exists in this agreement");
        }
        ProductLineBL productLine = new ProductLineBL(supplierCatalogId, name, basePrice, quantity);
        productLines.add(productLine);
        return productLine;
    }

    public void removeProductLine(int supplierCatalogId) {
        boolean removed = productLines.removeIf(pl -> pl.getSupplierCatalogId() == supplierCatalogId);
        if (!removed) throw new NoSuchElementException("Product line not found in this agreement");
        try {
            discountPolicy.getProductDiscounts().get(supplierCatalogId).clear();
        } catch (Exception ignored) {}
    }

    public ProductLineBL updateProductLineBasePrice(int supplierCatalogId, double newBasePrice) {
        if (newBasePrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        for (ProductLineBL pl : productLines) {
            if (pl.getSupplierCatalogId() == supplierCatalogId) {
                pl.setBasePrice(newBasePrice);
                return pl;
            }
        }
        throw new NoSuchElementException("Product line not found in this agreement");
    }

    public ProductLineBL updateProductLineQuantity(int supplierCatalogId, int newQuantity) {
        if (newQuantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        for (ProductLineBL pl : productLines) {
            if (pl.getSupplierCatalogId() == supplierCatalogId) {
                pl.setQuantity(newQuantity);
                return pl;
            }
        }
        throw new NoSuchElementException("Product line not found in this agreement");
    }

    public void addDiscount(int supplierCatalogId, int minQuantity, double discountPercentage) {
        checkProductExists(supplierCatalogId);
        discountPolicy.addBracket(supplierCatalogId, minQuantity, discountPercentage);
    }

    public void removeDiscount(int supplierCatalogId, int minQuantity) {
        discountPolicy.removeBracket(supplierCatalogId, minQuantity);
    }

    public void updateDiscount(int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        discountPolicy.updateBracket(supplierCatalogId, minQuantity, newDiscountPercentage);
    }

    public void updateFixedDeliveryDays(List<DayOfWeek> fixedDeliveryDays) {
        deliveryTerms.updateFixedDeliveryDays(fixedDeliveryDays);
    }

    public void updateSupplierTransports(boolean supplierTransports) {
        deliveryTerms.updateSupplierTransports(supplierTransports);
    }

    public double calculateTotal(int supplierCatalogId) {
        for (ProductLineBL pl : productLines) {
            if (pl.getSupplierCatalogId() == supplierCatalogId) {
                double discountPct = discountPolicy.calculateDiscount(supplierCatalogId, pl.getQuantity());
                return (pl.getQuantity() * pl.getBasePrice()) * (1.0 - (discountPct / 100.0));
            }
        }
        throw new NoSuchElementException("Product line not found");
    }

    private void checkProductExists(int supplierCatalogId) {
        boolean exists = productLines.stream().anyMatch(pl -> pl.getSupplierCatalogId() == supplierCatalogId);
        if (!exists) throw new IllegalArgumentException("Cannot add discount for a product not in the agreement");
    }

    public int getAgreementId() { return agreementId; }
    public LocalDate getStartDate() { return startDate; }
    public DeliveryTermsBL getDeliveryTerms() { return deliveryTerms; }
    public List<ProductLineBL> getProductLines() { return Collections.unmodifiableList(productLines); }
    public DiscountPolicyBL getDiscountPolicy() { return discountPolicy; }
}