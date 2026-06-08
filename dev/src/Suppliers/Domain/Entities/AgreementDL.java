package Suppliers.Domain.Entities;

import Suppliers.Domain.ValueObjects.DeliveryTermsDL;
import Suppliers.Domain.ValueObjects.DiscountBracketDL;
import Suppliers.Domain.ValueObjects.DiscountPolicyDL;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class AgreementDL {
    private final int agreementId;
    private final LocalDate startDate;
    private final DeliveryTermsDL deliveryTerms;
    private final List<ProductLineDL> productLines;
    private final DiscountPolicyDL discountPolicy;

    public AgreementDL(int agreementId, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.agreementId = agreementId;
        this.startDate = LocalDate.now();
        this.deliveryTerms = new DeliveryTermsDL(fixedDeliveryDays, supplierTransports);
        this.productLines = new ArrayList<>();
        this.discountPolicy = new DiscountPolicyDL();
    }


    public ProductLineDL addProductLine(int supplierCatalogId, String name, double basePrice, int quantity) {
        if (basePrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        for (ProductLineDL pl : productLines)
            if (pl.getSupplierCatalogId() == supplierCatalogId)
                throw new IllegalArgumentException("Product line already exists in this agreement");
        ProductLineDL productLine = new ProductLineDL(supplierCatalogId, name, basePrice, quantity);
        productLines.add(productLine);
        return productLine;
    }

    public void removeProductLine(int supplierCatalogId) {
        boolean removed = productLines.removeIf(pl -> pl.getSupplierCatalogId() == supplierCatalogId);
        if (!removed) throw new NoSuchElementException("Product line not found in this agreement");
        List<DiscountBracketDL> brackets = discountPolicy.getProductDiscounts().get(supplierCatalogId);
        if (brackets != null) brackets.clear();
    }

    public ProductLineDL updateProductLineBasePrice(int supplierCatalogId, double newBasePrice) {
        if (newBasePrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        for (ProductLineDL pl : productLines)
            if (pl.getSupplierCatalogId() == supplierCatalogId) {
                pl.setBasePrice(newBasePrice);
                return pl;
            }
        throw new NoSuchElementException("Product line not found in this agreement");
    }

    public ProductLineDL updateProductLineQuantity(int supplierCatalogId, int newQuantity) {
        if (newQuantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        for (ProductLineDL pl : productLines)
            if (pl.getSupplierCatalogId() == supplierCatalogId) {
                pl.setQuantity(newQuantity);
                return pl;
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

    private void checkProductExists(int supplierCatalogId) {
        boolean exists = productLines.stream().anyMatch(pl -> pl.getSupplierCatalogId() == supplierCatalogId);
        if (!exists) throw new IllegalArgumentException("Cannot add discount for a product not in the agreement");
    }

    public int getAgreementId() {
        return agreementId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public DeliveryTermsDL getDeliveryTerms() {
        return deliveryTerms;
    }

    public List<ProductLineDL> getProductLines() {
        return Collections.unmodifiableList(productLines);
    }

    public DiscountPolicyDL getDiscountPolicy() {
        return discountPolicy;
    }
}