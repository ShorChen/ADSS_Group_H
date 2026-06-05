package Inventory.Service.DTO;
public record LowStockAlertSL(String barcode, String productName, int currentTotal, int minRequired) {}