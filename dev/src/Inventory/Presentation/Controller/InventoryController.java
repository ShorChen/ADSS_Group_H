package Inventory.Presentation.Controller;

import Inventory.Presentation.DTO.*;
import Inventory.Service.Core.InventoryService;
import Inventory.Service.DTO.*;
import Core.Service.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"UnusedReturnValue", "ClassCanBeRecord"})
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductPL addProduct(String barcode, String name, String manufacturer, int categoryId, double costPrice, double sellingPrice, int minQuantity, int shelfQuantity, int warehouseQuantity, String aisle, int position) {
        Response<ProductSL> response = inventoryService.addProduct(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        ProductSL sl = response.getData();
        return new ProductPL(sl.barcode(), sl.name(), sl.manufacturer(), sl.categoryId(), sl.costPrice(), sl.sellingPrice(), sl.minQuantity(), sl.shelfQuantity(), sl.warehouseQuantity(), sl.aisle(), sl.position());
    }

    public boolean updateProductQuantities(String barcode, int shelfQuantity, int warehouseQuantity) {
        Response<Boolean> response = inventoryService.updateProductQuantities(barcode, shelfQuantity, warehouseQuantity);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData();
    }

    public void transferWarehouseToShelf(String barcode, int amount) {
        Response<Boolean> response = inventoryService.transferWarehouseToShelf(barcode, amount);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public boolean updateProductPricing(String barcode, double sellingPrice, int minQuantity) {
        Response<Boolean> response = inventoryService.updateProductPricing(barcode, sellingPrice, minQuantity);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData();
    }

    public void deleteProduct(String barcode) {
        Response<Boolean> response = inventoryService.deleteProduct(barcode);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public CategoryPL addCategory(String name, Integer parentId) {
        Response<CategorySL> response = inventoryService.addCategory(name, parentId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        CategorySL sl = response.getData();
        return new CategoryPL(sl.categoryId(), sl.name(), sl.parentId());
    }

    public void deleteCategory(int categoryId) {
        Response<Boolean> response = inventoryService.deleteCategory(categoryId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public DefectiveItemPL reportDefectiveItem(String barcode, int quantity, String location, String reason) {
        Response<DefectiveItemSL> response = inventoryService.reportDefectiveItem(barcode, quantity, location, reason);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        DefectiveItemSL sl = response.getData();
        return new DefectiveItemPL(sl.defectId(), sl.barcode(), sl.quantity(), sl.location(), sl.reason(), sl.reportDate());
    }

    public List<DefectiveItemPL> getAllDefectiveItems() {
        Response<List<DefectiveItemSL>> response = inventoryService.getAllDefectiveItems();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new DefectiveItemPL(sl.defectId(), sl.barcode(), sl.quantity(), sl.location(), sl.reason(), sl.reportDate())).collect(Collectors.toList());
    }

    public PromotionPL addPromotion(String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId) {
        Response<PromotionSL> response = inventoryService.addPromotion(name, discountPercentage, startDate, endDate, targetType, targetId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        PromotionSL sl = response.getData();
        return new PromotionPL(sl.promoId(), sl.name(), sl.discountPercentage(), sl.startDate(), sl.endDate(), sl.targetType(), sl.targetId(), sl.isActive());
    }

    public void deletePromotion(int promoId) {
        Response<Boolean> response = inventoryService.deletePromotion(promoId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public List<PromotionPL> getAllPromotions() {
        Response<List<PromotionSL>> response = inventoryService.getAllPromotions();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new PromotionPL(sl.promoId(), sl.name(), sl.discountPercentage(), sl.startDate(), sl.endDate(), sl.targetType(), sl.targetId(), sl.isActive())).collect(Collectors.toList());
    }

    public List<LowStockAlertPL> generateShortageReport() {
        Response<List<LowStockAlertSL>> response = inventoryService.generateShortageReport();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new LowStockAlertPL(sl.barcode(), sl.productName(), sl.currentTotal(), sl.minRequired())).collect(Collectors.toList());
    }

    public InventoryReportPL generateCategoryReport(int categoryId) {
        Response<InventoryReportSL> response = inventoryService.generateCategoryReport(categoryId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        InventoryReportSL sl = response.getData();
        List<ProductPL> items = sl.items().stream().map(p -> new ProductPL(p.barcode(), p.name(), p.manufacturer(), p.categoryId(), p.costPrice(), p.sellingPrice(), p.minQuantity(), p.shelfQuantity(), p.warehouseQuantity(), p.aisle(), p.position())).collect(Collectors.toList());
        return new InventoryReportPL(sl.categoryId(), sl.generationDate(), items);
    }

    public List<ProductPL> getAllProducts() {
        Response<List<ProductSL>> response = inventoryService.getAllProducts();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new ProductPL(sl.barcode(), sl.name(), sl.manufacturer(), sl.categoryId(), sl.costPrice(), sl.sellingPrice(), sl.minQuantity(), sl.shelfQuantity(), sl.warehouseQuantity(), sl.aisle(), sl.position())).collect(Collectors.toList());
    }

    public List<CategoryPL> getAllCategories() {
        Response<List<CategorySL>> response = inventoryService.getAllCategories();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new CategoryPL(sl.categoryId(), sl.name(), sl.parentId())).collect(Collectors.toList());
    }
}