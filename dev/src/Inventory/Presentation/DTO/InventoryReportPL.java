package Inventory.Presentation.DTO;
import java.time.LocalDate;
import java.util.List;
public record InventoryReportPL(int categoryId, LocalDate generationDate, List<ProductPL> items) {}