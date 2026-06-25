package Inventory;

import Core.Domain.Role;
import Inventory.Domain.Entities.*;
import Core.Domain.SessionManager;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialInventoryTests {
    private ProductDL currentProduct;

    @Test
    @Order(1)
    void step01_loginAsInventoryManager() {
        SessionManager.getInstance().login("ADMIN", Role.INVENTORY_MANAGER);
        assertEquals(Role.INVENTORY_MANAGER, SessionManager.getInstance().getCurrentRole());
    }

    @Test
    @Order(2)
    void step02_createCategory() {
        CategoryDL category = new CategoryDL(1, "Dairy", null);
        assertNotNull(category);
        assertEquals("Dairy", category.getName());
    }

    @Test
    @Order(3)
    void step03_createProductWithQuantities() {
        currentProduct = new ProductDL("1001", "Milk 3%", "Tnuva", 1, 4.0, 6.0, 50, 20, 100, "A1", 1);
        assertEquals("1001", currentProduct.getBarcode());
        assertEquals(20, currentProduct.getShelfQuantity());
        assertEquals(100, currentProduct.getWarehouseQuantity());
    }

    @Test
    @Order(4)
    void step04_testProductPricingAndThresholds() {
        assertEquals(4.0, currentProduct.getCostPrice());
        assertEquals(6.0, currentProduct.getSellingPrice());
        assertEquals(50, currentProduct.getMinQuantity());
    }

    @Test
    @Order(5)
    void step05_calculateTransferredQuantities() {
        int updatedShelf = currentProduct.getShelfQuantity() + 30;
        int updatedWarehouse = currentProduct.getWarehouseQuantity() - 30;
        assertEquals(50, updatedShelf);
        assertEquals(70, updatedWarehouse);
    }

    @Test
    @Order(6)
    void step06_createDefectiveItem() {
        DefectiveItemDL defect = new DefectiveItemDL(1, "1001", 10, "SHELF", "Expired", LocalDate.now());
        assertNotNull(defect);
        assertEquals(10, defect.getQuantity());
    }

    @Test
    @Order(7)
    void step07_verifyDefectAttributes() {
        DefectiveItemDL defect = new DefectiveItemDL(2, "1001", 5, "WAREHOUSE", "Broken", LocalDate.now());
        assertEquals("WAREHOUSE", defect.getLocation());
        assertEquals("Broken", defect.getReason());
    }

    @Test
    @Order(8)
    void step08_createPromotion() {
        PromotionDL promo = new PromotionDL(1, "Summer Sale", 15.0, LocalDate.now().minusDays(1), LocalDate.now().plusDays(10), "CATEGORY", "1");
        assertNotNull(promo);
        assertEquals(15.0, promo.getDiscountPercentage());
    }

    @Test
    @Order(9)
    void step09_verifyExpiredPromotionIsInactive() {
        PromotionDL expiredPromo = new PromotionDL(2, "Old Sale", 20.0, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), "PRODUCT", "1001");
        assertFalse(expiredPromo.isActive(LocalDate.now()));
        assertEquals("PRODUCT", expiredPromo.getTargetType());
    }

    @Test
    @Order(10)
    void step10_logoutAndVerifySecurity() {
        SessionManager.getInstance().logout();
        assertThrows(SecurityException.class, () -> SessionManager.getInstance().getCurrentRole());
    }
}