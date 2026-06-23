package Inventory.Service.Core;

import Inventory.Domain.Entities.*;
import Inventory.Domain.Facades.InventoryFacade;
import Inventory.Service.DTO.*;
import Core.Service.Response;
import Suppliers.Service.Core.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {
    private final InventoryFacade inventoryFacade;
    private OrderService orderService;

    public InventoryService(InventoryFacade inventoryFacade) {
        this.inventoryFacade = inventoryFacade;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public Response<ProductSL> addProduct(String barcode, String name, String manufacturer, int categoryId, double costPrice, double sellingPrice, int minQuantity, int shelfQuantity, int warehouseQuantity, String aisle, int position) {
        try {
            ProductDL product = inventoryFacade.addProduct(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position);
            return new Response<>(new ProductSL(product.getBarcode(), product.getName(), product.getManufacturer(), product.getCategoryId(), product.getCostPrice(), product.getSellingPrice(), product.getMinQuantity(), product.getShelfQuantity(), product.getWarehouseQuantity(), product.getAisle(), product.getPosition()));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateProductQuantities(String barcode, int shelfQuantity, int warehouseQuantity) {
        try {
            ProductDL product = inventoryFacade.getProductOrThrow(barcode);
            int oldShelf = product.getShelfQuantity();
            int oldWarehouse = product.getWarehouseQuantity();
            inventoryFacade.updateProductQuantities(barcode, shelfQuantity, warehouseQuantity);
            try {
                checkAndTriggerShortageOrder(barcode);
            } catch (Exception ex) {
                inventoryFacade.updateProductQuantities(barcode, oldShelf, oldWarehouse);
                throw new Exception("Inventory update reverted because Order failed: " + ex.getMessage());
            }

            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateProductPricing(String barcode, double sellingPrice, int minQuantity) {
        try {
            ProductDL product = inventoryFacade.getProductOrThrow(barcode);
            double oldPrice = product.getSellingPrice();
            int oldMin = product.getMinQuantity();
            inventoryFacade.updateProductPricing(barcode, sellingPrice, minQuantity);
            try {
                checkAndTriggerShortageOrder(barcode);
            } catch (Exception ex) {
                inventoryFacade.updateProductPricing(barcode, oldPrice, oldMin);
                throw new Exception("Pricing update reverted because Order failed: " + ex.getMessage());
            }

            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<DefectiveItemSL> reportDefectiveItem(String barcode, int quantity, String location, String reason) {
        try {
            ProductDL product = inventoryFacade.getProductOrThrow(barcode);
            int oldShelf = product.getShelfQuantity();
            int oldWarehouse = product.getWarehouseQuantity();
            DefectiveItemDL defectiveItem = inventoryFacade.reportDefectiveItem(barcode, quantity, location, reason);
            try {
                checkAndTriggerShortageOrder(barcode);
            } catch (Exception ex) {
                inventoryFacade.updateProductQuantities(barcode, oldShelf, oldWarehouse);
                throw new Exception("Defect report reverted because Order failed: " + ex.getMessage());
            }
            return new Response<>(new DefectiveItemSL(defectiveItem.getDefectId(), defectiveItem.getBarcode(), defectiveItem.getQuantity(), defectiveItem.getLocation(), defectiveItem.getReason(), defectiveItem.getReportDate()));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> transferWarehouseToShelf(String barcode, int amount) {
        try {
            inventoryFacade.transferWarehouseToShelf(barcode, amount);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> deleteProduct(String barcode) {
        try {
            inventoryFacade.deleteProduct(barcode);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<CategorySL> addCategory(String name, Integer parentId) {
        try {
            CategoryDL category = inventoryFacade.addCategory(name, parentId);
            return new Response<>(new CategorySL(category.getCategoryId(), category.getName(), category.getParentId()));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> deleteCategory(int categoryId) {
        try {
            inventoryFacade.deleteCategory(categoryId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<DefectiveItemSL>> getAllDefectiveItems() {
        try {
            List<DefectiveItemSL> list = inventoryFacade.getAllDefectiveItems().stream().map(d -> new DefectiveItemSL(d.getDefectId(), d.getBarcode(), d.getQuantity(), d.getLocation(), d.getReason(), d.getReportDate())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<PromotionSL> addPromotion(String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId) {
        try {
            PromotionDL promotion = inventoryFacade.addPromotion(name, discountPercentage, startDate, endDate, targetType, targetId);
            return new Response<>(new PromotionSL(promotion.getPromoId(), promotion.getName(), promotion.getDiscountPercentage(), promotion.getStartDate(), promotion.getEndDate(), promotion.getTargetType(), promotion.getTargetId(), promotion.isActive(LocalDate.now())));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> deletePromotion(int promoId) {
        try {
            inventoryFacade.deletePromotion(promoId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<PromotionSL>> getAllPromotions() {
        try {
            LocalDate today = LocalDate.now();
            List<PromotionSL> list = inventoryFacade.getAllPromotions().stream().map(p -> new PromotionSL(p.getPromoId(), p.getName(), p.getDiscountPercentage(), p.getStartDate(), p.getEndDate(), p.getTargetType(), p.getTargetId(), p.isActive(today))).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<LowStockAlertSL>> generateShortageReport() {
        try {
            List<LowStockAlertSL> list = inventoryFacade.generateShortageReport().stream().map(a -> new LowStockAlertSL(a.getBarcode(), a.getProductName(), a.getCurrentTotal(), a.getMinRequired())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<InventoryReportSL> generateCategoryReport(int categoryId) {
        try {
            InventoryReport report = inventoryFacade.generateCategoryReport(categoryId);
            List<ProductSL> items = report.getItems().stream().map(p -> new ProductSL(p.getBarcode(), p.getName(), p.getManufacturer(), p.getCategoryId(), p.getCostPrice(), p.getSellingPrice(), p.getMinQuantity(), p.getShelfQuantity(), p.getWarehouseQuantity(), p.getAisle(), p.getPosition())).collect(Collectors.toList());
            return new Response<>(new InventoryReportSL(report.getCategoryId(), report.getGenerationDate(), items));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<ProductSL>> getAllProducts() {
        try {
            List<ProductSL> list = inventoryFacade.getAllProducts().stream().map(p -> new ProductSL(p.getBarcode(), p.getName(), p.getManufacturer(), p.getCategoryId(), p.getCostPrice(), p.getSellingPrice(), p.getMinQuantity(), p.getShelfQuantity(), p.getWarehouseQuantity(), p.getAisle(), p.getPosition())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<CategorySL>> getAllCategories() {
        try {
            List<CategorySL> list = inventoryFacade.getAllCategories().stream().map(c -> new CategorySL(c.getCategoryId(), c.getName(), c.getParentId())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private void checkAndTriggerShortageOrder(String barcode) throws Exception {
        ProductDL product = inventoryFacade.getProductOrThrow(barcode);
        if (product.requiresRestockAlert()) {
            int missingAmount = product.getMinQuantity() - product.getTotalQuantity();
            if (orderService != null) {
                Response<Integer> orderResponse = orderService.generateShortageOrder(product.getName(), missingAmount);
                if (!orderResponse.isSuccess()) {
                    throw new Exception(orderResponse.getErrorMessage());
                }
            }
        }
    }
}