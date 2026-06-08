package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.AgreementDL;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AgreementSL(
        int agreementId,
        LocalDate startDate,
        DeliveryTermsSL deliveryTerms,
        List<ProductLineSL> productLines,
        Map<Integer, List<DiscountBracketSL>> discountPolicy
) {

    public AgreementSL(AgreementDL agreementDL) {
        this(
                agreementDL.getAgreementId(),
                agreementDL.getStartDate(),
                new DeliveryTermsSL(agreementDL.getDeliveryTerms()),
                agreementDL.getProductLines().stream().map(ProductLineSL::new).collect(Collectors.toList()),
                agreementDL.getDiscountPolicy().getProductDiscounts().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().map(DiscountBracketSL::new).collect(Collectors.toList())
                        ))
        );
    }
}