package Inventory.Service.DTO;
import java.time.LocalDate;
public record PromotionSL(int promoId, String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId, boolean isActive) {}