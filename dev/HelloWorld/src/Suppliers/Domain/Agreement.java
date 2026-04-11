package Suppliers.Domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Agreement {
    private final int id;
    private final Date startDate;
    private final DeliveryTerms deliveryTerms;
    private final List<ProductLine> productLines;
    private final BracketDiscountPolicy discountPolicy;

    public Agreement(int id, Date startDate, DeliveryTerms deliveryTerms,
                     List<ProductLine> productLines, BracketDiscountPolicy discountPolicy) {
        this.id = id;
        this.startDate = startDate;
        this.deliveryTerms = deliveryTerms;
        this.productLines = productLines;
        this.discountPolicy = discountPolicy;
    }

    public int getId() { return id; }
    public Date getStartDate() { return startDate; }
    public DeliveryTerms getDeliveryTerms() { return deliveryTerms; }
    public List<ProductLine> getProductLines() { return Collections.unmodifiableList(productLines); }
    public BracketDiscountPolicy getDiscountPolicy() { return discountPolicy; }
    public boolean hasDiscountPolicy() { return discountPolicy != null; }

    public double getAgreedPrice(int catalogNumber) {
        return productLines.stream()
                .filter(pl -> pl.getProduct().catalogNumber() == catalogNumber)
                .mapToDouble(ProductLine::getAgreedPrice)
                .findFirst()
                .orElse(-1.0);
    }

    public double calculateEffectivePrice(int catalogNumber, int quantity) {
        double agreedPrice = getAgreedPrice(catalogNumber);
        if (agreedPrice < 0) return -1.0;
        if (discountPolicy == null) return agreedPrice;
        double discountPct = discountPolicy.calculateDiscount(catalogNumber, quantity);
        return agreedPrice * (1.0 - discountPct / 100.0);
    }
}