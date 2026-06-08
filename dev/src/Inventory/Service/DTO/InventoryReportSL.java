package Inventory.Service.DTO;
import java.time.LocalDate;
import java.util.List;
public record InventoryReportSL(int categoryId, LocalDate generationDate, List<ProductSL> items) {}