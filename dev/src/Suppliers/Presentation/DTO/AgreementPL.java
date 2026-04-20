package Suppliers.Presentation.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AgreementPL {
    private final int agreementId;
    private final LocalDate startDate;
    private final DeliveryTermsPL deliveryTerms;
    private final List<ProductLinePL> productLines;
    private final Map<Integer, List<DiscountBracketPL>> discountPolicy;

    public AgreementPL(int agreementId, LocalDate startDate, DeliveryTermsPL deliveryTerms, List<ProductLinePL> productLines, Map<Integer, List<DiscountBracketPL>> discountPolicy) {
        this.agreementId = agreementId;
        this.startDate = startDate;
        this.deliveryTerms = deliveryTerms;
        this.productLines = productLines;
        this.discountPolicy = discountPolicy;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public DeliveryTermsPL getDeliveryTerms() {
        return deliveryTerms;
    }

    public List<ProductLinePL> getProductLines() {
        return productLines;
    }

    public Map<Integer, List<DiscountBracketPL>> getDiscountPolicy() {
        return discountPolicy;
    }
}