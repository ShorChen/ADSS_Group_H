package Inventory.DataAccess;

import Inventory.Domain.Entities.*;
import java.util.List;
import java.util.Map;

public interface InventoryDAO {
    // Products
    void addProduct(ProductDL product);
    void updateProduct(ProductDL product);
    void deleteProduct(String barcode);
    @SuppressWarnings("unused")
    ProductDL getProduct(String barcode);
    Map<String, ProductDL> getAllProducts();

    // Categories
    int addCategory(CategoryDL category);
    @SuppressWarnings("unused")
    CategoryDL getCategory(int categoryId);
    Map<Integer, CategoryDL> getAllCategories();
    void deleteCategory(int categoryId);

    // Defective Items
    void addDefectiveItem(DefectiveItemDL item);
    List<DefectiveItemDL> getAllDefectiveItems();

    // Promotions
    int addPromotion(PromotionDL promo);
    List<PromotionDL> getAllPromotions();
    void deletePromotion(int promoId);
}