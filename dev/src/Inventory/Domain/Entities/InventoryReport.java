package Inventory.Domain.Entities;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class InventoryReport {
    private final int categoryId;
    private final LocalDate generationDate;
    private final List<ProductDL> items;

    public InventoryReport(int categoryId, LocalDate generationDate, List<ProductDL> items) {
        this.categoryId = categoryId;
        this.generationDate = generationDate;
        this.items = items;
    }

    public int getCategoryId() { return categoryId; }
    public LocalDate getGenerationDate() { return generationDate; }
    public List<ProductDL> getItems() { return items; }
}