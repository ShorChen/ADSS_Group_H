package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.AgreementDL;

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

    public AgreementSL(AgreementDL agreementDL) {
        agreementId = agreementDL.getAgreementId();
        startDate = agreementDL.getStartDate();
        deliveryTerms = new DeliveryTermsSL(agreementDL.getDeliveryTerms());
        productLines = agreementDL.getProductLines().stream().map(ProductLineSL::new).collect(Collectors.toList());
        discountPolicy = agreementDL.getDiscountPolicy().getProductDiscounts().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(DiscountBracketSL::new).collect(Collectors.toList())
                ));
    }

    public int getAgreementId() {
        return agreementId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public DeliveryTermsSL getDeliveryTerms() {
        return deliveryTerms;
    }

    public List<ProductLineSL> getProductLines() {
        return productLines;
    }

    public Map<Integer, List<DiscountBracketSL>> getDiscountPolicy() {
        return discountPolicy;
    }
}