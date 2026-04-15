package Suppliers.Domain.Service;

import Suppliers.Domain.Business.AgreementBL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgreementSL {
    private final int agreementId;
    private final LocalDate startDate;
    private final DeliveryTermsSL deliveryTerms;
    private final List<ProductLineSL> productLines;
    private final Map<Integer, List<DiscountBracketSL>> discountPolicy;

    AgreementSL(AgreementBL agreementBL) {
        this.agreementId = agreementBL.getAgreementId();
        this.startDate = agreementBL.getStartDate();
        this.deliveryTerms = new DeliveryTermsSL(agreementBL.getDeliveryTerms());
        this.productLines = agreementBL.getProductLines().stream().map(ProductLineSL::new).collect(Collectors.toList());
        this.discountPolicy = agreementBL.getDiscountPolicy().getProductDiscounts().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(DiscountBracketSL::new).collect(Collectors.toList())
                ));
    }

    public int getAgreementId() { return agreementId; }
    public LocalDate getStartDate() { return startDate; }
    public DeliveryTermsSL getDeliveryTerms() { return deliveryTerms; }
    public List<ProductLineSL> getProductLines() { return productLines; }
    public Map<Integer, List<DiscountBracketSL>> getDiscountPolicy() { return discountPolicy; }
}