package Suppliers.Domain;

import Core.Domain.Role;
import Suppliers.Domain.Entities.*;
import Suppliers.Domain.Security.*;
import Suppliers.Domain.ValueObjects.*;
import org.junit.jupiter.api.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialDomainTests {
    private SupplierDL currentSupplier;
    private AgreementDL currentAgreement;
    private ProductLineDL currentProduct;

    @Test
    @Order(1)
    void step01_loginAsSupplierManager() {
        SessionManager.getInstance().login(Role.SUPPLIER_MANAGER);
        assertEquals(Role.SUPPLIER_MANAGER, SessionManager.getInstance().getCurrentRole());
    }

    @Test
    @Order(2)
    void step02_createNewSupplier() {
        PaymentDetails pd = new PaymentDetails("IL1234567890123456789012345", "Net 30");
        currentSupplier = new SupplierDL("Tnuva", "123456789", "Tel Aviv", pd);
        assertNotNull(currentSupplier);
        assertEquals("Tnuva", currentSupplier.getName());
    }

    @Test
    @Order(3)
    void step03_validateAndAddContactPerson() {
        String phone = "054-1234567";
        String email = "contact@tnuva.co.il";
        assertDoesNotThrow(() -> ValidationUtils.validatePhone(phone));
        assertDoesNotThrow(() -> ValidationUtils.validateEmail(email));
        ContactPersonDL cp = currentSupplier.addContactPerson("Yossi", phone, email);
        assertEquals(1, currentSupplier.getContactPersonnel().size());
        assertEquals("Yossi", cp.getName());
    }

    @Test
    @Order(4)
    void step04_failToAddNewContactWithInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateEmail("yossi_at_tnuva.com"); // Missing the @ symbol
        });
    }

    @Test
    @Order(5)
    void step05_addAgreementToSupplier() {
        List<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY));
        currentAgreement = currentSupplier.addAgreement(1, days, true);
        assertEquals(1, currentSupplier.getAgreements().size());
        assertTrue(currentAgreement.getDeliveryTerms().isSupplierTransports());
    }

    @Test
    @Order(6)
    void step06_addProductLineToAgreement() {
        currentProduct = currentAgreement.addProductLine(101, "Milk 3%", 5.90, 100);
        assertEquals(1, currentAgreement.getProductLines().size());
        assertEquals("Milk 3%", currentProduct.getName());
    }

    @Test
    @Order(7)
    void step07_addDiscountToProduct() {
        currentAgreement.addDiscount(101, 50, 10.0);
        assertFalse(currentAgreement.getDiscountPolicy().getProductDiscounts().isEmpty());
    }

    @Test
    @Order(8)
    void step08_verifyDiscountCalculationLogic() {
        double discount1 = currentAgreement.getDiscountPolicy().calculateDiscount(101, 100);
        assertEquals(10.0, discount1);
        double discount2 = currentAgreement.getDiscountPolicy().calculateDiscount(101, 20);
        assertEquals(0.0, discount2);
    }

    @Test
    @Order(9)
    void step09_updateProductPriceAndQuantity() {
        currentAgreement.updateProductLineBasePrice(101, 6.00);
        currentAgreement.updateProductLineQuantity(101, 500);
        assertEquals(6.00, currentProduct.getBasePrice());
        assertEquals(500, currentProduct.getQuantity());
    }

    @Test
    @Order(10)
    void step10_logoutAndVerifySecurity() {
        SessionManager.getInstance().logout();
        assertThrows(SecurityException.class, () -> SessionManager.getInstance().getCurrentRole());
    }
}