package Inventory.Presentation.DTO;
public record LowStockAlertPL(String barcode, String productName, int currentTotal, int minRequired) {}