package Inventory.Domain.Entities;

import java.time.LocalDate;

@SuppressWarnings("ClassCanBeRecord")
public class PromotionDL {
    private final int promoId;
    private final String name;
    private final double discountPercentage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String targetType;
    private final String targetId;

    public PromotionDL(int promoId, String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId) {
        this.promoId = promoId;
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public int getPromoId() { return promoId; }
    public String getName() { return name; }
    public double getDiscountPercentage() { return discountPercentage; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getTargetType() { return targetType; }
    public String getTargetId() { return targetId; }

    @SuppressWarnings("unused")
    public boolean isActive(LocalDate currentDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }
}