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

    public AgreementBL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.agreementId = ++idCounter;
        this.startDate = LocalDate.now();
        this.deliveryTerms = new DeliveryTermsBL(fixedDeliveryDays, supplierTransports);
        this.productLines = new ArrayList<>();
        this.discountPolicy = new DiscountPolicyBL();
    }

    public ProductLineBL addProductLine(int internalCatalogId, int supplierCatalogId, double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        for (ProductLineBL pl : productLines)
            if (pl.getInternalCatalogId() == internalCatalogId) throw new IllegalArgumentException("Product line already exists in this agreement");
        ProductLineBL productLine = new ProductLineBL(internalCatalogId, supplierCatalogId, price);
        productLines.add(productLine);
        return productLine;
    }

    public void removeProductLine(int internalCatalogId) {
        boolean removed = productLines.removeIf(pl -> pl.getInternalCatalogId() == internalCatalogId);
        if (!removed) throw new NoSuchElementException("Product line not found in this agreement");
        try {
            discountPolicy.getProductDiscounts().get(internalCatalogId).clear();
        } catch (Exception ignored) {}
    }

    public ProductLineBL updateProductLine(int internalCatalogId, double newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        for (ProductLineBL pl : productLines) {
            if (pl.getInternalCatalogId() == internalCatalogId) {
                pl.setAgreedPrice(newPrice);
                return pl;
            }
        }
        throw new NoSuchElementException("Product line not found in this agreement");
    }

    public void addDiscount(int internalCatalogId, int minQuantity, double discountPercentage) {
        checkProductExists(internalCatalogId);
        discountPolicy.addBracket(internalCatalogId, minQuantity, discountPercentage);
    }

    public void removeDiscount(int internalCatalogId, int minQuantity) {
        discountPolicy.removeBracket(internalCatalogId, minQuantity);
    }

    public void updateDiscount(int internalCatalogId, int minQuantity, double newDiscountPercentage) {
        discountPolicy.updateBracket(internalCatalogId, minQuantity, newDiscountPercentage);
    }

    public void updateDeliveryTerms(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        deliveryTerms.updateTerms(fixedDeliveryDays, supplierTransports);
    }

    private void checkProductExists(int internalCatalogId) {
        boolean exists = productLines.stream().anyMatch(pl -> pl.getInternalCatalogId() == internalCatalogId);
        if (!exists) throw new IllegalArgumentException("Cannot add discount for a product not in the agreement");
    }

    public int getAgreementId() { return agreementId; }
    public LocalDate getStartDate() { return startDate; }
    public DeliveryTermsBL getDeliveryTerms() { return deliveryTerms; }
    public List<ProductLineBL> getProductLines() { return Collections.unmodifiableList(productLines); }
    public DiscountPolicyBL getDiscountPolicy() { return discountPolicy; }
}