package Suppliers.Presentation.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record AgreementPL(int agreementId, LocalDate startDate, DeliveryTermsPL deliveryTerms,
                          List<ProductLinePL> productLines, Map<Integer, List<DiscountBracketPL>> discountPolicy) {
}