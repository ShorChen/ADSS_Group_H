package Inventory.Presentation.DTO;
import java.time.LocalDate;
public record PromotionPL(int promoId, String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId, boolean isActive) {}