package Inventory.Domain.Facades;

import Inventory.DataAccess.InventoryDAO;
import Inventory.Domain.Entities.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class InventoryFacade {
    private final InventoryDAO inventoryDAO;
    private final Map<String, ProductDL> products;
    private final Map<Integer, CategoryDL> categories;

    public InventoryFacade(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
        this.products = inventoryDAO.getAllProducts();
        this.categories = inventoryDAO.getAllCategories();
    }

    public ProductDL addProduct(String barcode, String name, String manufacturer, int categoryId, double costPrice, double sellingPrice, int minQuantity, int shelfQuantity, int warehouseQuantity, String aisle, int position) {
        if (products.containsKey(barcode)) throw new IllegalArgumentException("Product already exists.");
        if (!categories.containsKey(categoryId)) throw new IllegalArgumentException("Category does not exist.");
        ProductDL p = new ProductDL(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position);
        inventoryDAO.addProduct(p);
        products.put(barcode, p);
        return p;
    }

    public void updateProductQuantities(String barcode, int shelfQuantity, int warehouseQuantity) {
        ProductDL p = getProductOrThrow(barcode);
        p.setShelfQuantity(shelfQuantity);
        p.setWarehouseQuantity(warehouseQuantity);
        inventoryDAO.updateProduct(p);
    }

    public void transferWarehouseToShelf(String barcode, int amount) {
        ProductDL p = getProductOrThrow(barcode);
        if (p.getWarehouseQuantity() < amount) throw new IllegalArgumentException("Insufficient warehouse stock.");
        p.setWarehouseQuantity(p.getWarehouseQuantity() - amount);
        p.setShelfQuantity(p.getShelfQuantity() + amount);
        inventoryDAO.updateProduct(p);
    }

    public void updateProductPricing(String barcode, double sellingPrice, int minQuantity) {
        ProductDL p = getProductOrThrow(barcode);
        p.setSellingPrice(sellingPrice);
        p.setMinQuantity(minQuantity);
        inventoryDAO.updateProduct(p);
    }

    public void deleteProduct(String barcode) {
        getProductOrThrow(barcode);
        inventoryDAO.deleteProduct(barcode);
        products.remove(barcode);
    }

    public CategoryDL addCategory(String name, Integer parentId) {
        if (parentId != null && !categories.containsKey(parentId)) throw new IllegalArgumentException("Parent category does not exist.");
        CategoryDL c = new CategoryDL(0, name, parentId);
        int id = inventoryDAO.addCategory(c);
        CategoryDL saved = new CategoryDL(id, name, parentId);
        categories.put(id, saved);
        return saved;
    }

    public void deleteCategory(int categoryId) {
        if (!categories.containsKey(categoryId)) throw new IllegalArgumentException("Category not found.");
        inventoryDAO.deleteCategory(categoryId);
        categories.remove(categoryId);
    }

    public DefectiveItemDL reportDefectiveItem(String barcode, int quantity, String location, String reason) {
        ProductDL p = getProductOrThrow(barcode);
        if (location.equals("SHELF")) {
            if (p.getShelfQuantity() < quantity) throw new IllegalArgumentException("Not enough items on shelf.");
            p.setShelfQuantity(p.getShelfQuantity() - quantity);
        } else if (location.equals("WAREHOUSE")) {
            if (p.getWarehouseQuantity() < quantity) throw new IllegalArgumentException("Not enough items in warehouse.");
            p.setWarehouseQuantity(p.getWarehouseQuantity() - quantity);
        } else throw new IllegalArgumentException("Invalid location.");
        inventoryDAO.updateProduct(p);
        DefectiveItemDL d = new DefectiveItemDL(0, barcode, quantity, location, reason, LocalDate.now());
        inventoryDAO.addDefectiveItem(d);
        return d;
    }

    public List<DefectiveItemDL> getAllDefectiveItems() {
        return inventoryDAO.getAllDefectiveItems();
    }

    public PromotionDL addPromotion(String name, double discountPercentage, LocalDate startDate, LocalDate endDate, String targetType, String targetId) {
        PromotionDL promo = new PromotionDL(0, name, discountPercentage, startDate, endDate, targetType, targetId);
        int id = inventoryDAO.addPromotion(promo);
        return new PromotionDL(id, name, discountPercentage, startDate, endDate, targetType, targetId);
    }

    public void deletePromotion(int promoId) {
        inventoryDAO.deletePromotion(promoId);
    }

    public List<PromotionDL> getAllPromotions() {
        return inventoryDAO.getAllPromotions();
    }

    public List<LowStockAlert> generateShortageReport() {
        return products.values().stream().filter(ProductDL::requiresRestockAlert).map(p -> new LowStockAlert(p.getBarcode(), p.getName(), p.getTotalQuantity(), p.getMinQuantity())).collect(Collectors.toList());
    }

    public InventoryReport generateCategoryReport(int categoryId) {
        if (!categories.containsKey(categoryId)) throw new IllegalArgumentException("Category not found.");
        List<ProductDL> items = products.values().stream().filter(p -> p.getCategoryId() == categoryId).collect(Collectors.toList());
        return new InventoryReport(categoryId, LocalDate.now(), items);
    }

    public ProductDL getProductOrThrow(String barcode) {
        ProductDL p = products.get(barcode);
        if (p == null) throw new NoSuchElementException("Product not found.");
        return p;
    }

    public List<ProductDL> getAllProducts() {
        return List.copyOf(products.values());
    }

    public List<CategoryDL> getAllCategories() {
        return List.copyOf(categories.values());
    }
}