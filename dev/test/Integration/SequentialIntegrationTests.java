package Integration;

import Core.Domain.Role;
import Core.Domain.SessionManager;
import Employees.Domain.Entities.RoleDL;
import Employees.Domain.Entities.ShiftDL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Inventory.Domain.Entities.ProductDL;
import Suppliers.Domain.Entities.AgreementDL;
import Transportation.Domain.Entities.DriverDL;
import Transportation.Domain.Entities.TruckDL;
import org.junit.jupiter.api.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialIntegrationTests {
    private TruckDL heavyTruck;
    private TruckDL lightTruck;
    private DriverDL lightDriver;
    private DriverDL heavyDriver;
    private ShiftDL mondayShift;
    private ProductDL inventoryProduct;
    private AgreementDL supplierAgreement;
    private RoleDL storekeeperRole;
    private RoleDL driverRole;

    @BeforeAll
    void setUp() {
        SessionManager.getInstance().login(Role.TRANSPORTATION_MANAGER);
        heavyTruck = new TruckDL("TRK-1", "Volvo", 8000.0, 24000.0, false);
        lightTruck = new TruckDL("TRK-2", "Fiat", 3000.0, 10000.0, false);
        lightDriver = new DriverDL("DRV-1", "Dani", "C1", LocalDate.now().plusYears(1));
        heavyDriver = new DriverDL("DRV-2", "Yossi", "HEAVY", LocalDate.now().plusYears(1));
        mondayShift = new ShiftDL(1, 1, 2026, 26, LocalDateTime.now(), WeekDay.MONDAY, ShiftType.DAY, new HashMap<>(), new HashMap<>());
        storekeeperRole = new RoleDL("Storekeeper");
        driverRole = new RoleDL("Driver");
        mondayShift.setCapacity(storekeeperRole, 1);
        mondayShift.setCapacity(driverRole, 2);
        inventoryProduct = new ProductDL("1001", "Milk 3%", "Tnuva", 1, 4.0, 6.0, 50, 20, 100, "A1", 1);
        List<DayOfWeek> deliveryDays = new ArrayList<>(List.of(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY));
        supplierAgreement = new AgreementDL(1, deliveryDays, true);
        supplierAgreement.addProductLine(1001, "Milk 3%", 4.0, 500);
    }

    @Test
    @Order(1)
    void test01_transportIntegration_DriverLicenseC1FailsHeavyWeightCheck() {
        boolean isAuthorized = lightDriver.getLicenseType().equalsIgnoreCase("HEAVY") || (lightDriver.getLicenseType().equalsIgnoreCase("C1") && heavyTruck.getMaxWeight() <= 12000.0);
        assertFalse(isAuthorized);
    }

    @Test
    @Order(2)
    void test02_transportIntegration_DriverLicenseHeavyPassesHeavyWeightCheck() {
        boolean isAuthorized = heavyDriver.getLicenseType().equalsIgnoreCase("HEAVY") || (heavyDriver.getLicenseType().equalsIgnoreCase("C1") && heavyTruck.getMaxWeight() <= 12000.0);
        assertTrue(isAuthorized);
    }

    @Test
    @Order(3)
    void test03_transportIntegration_HeavyDriverScheduledInShiftValidation() {
        mondayShift.assignEmployeeToRole(driverRole, heavyDriver.getEmployeeId());
        assertTrue(mondayShift.doesEmployeeWork(heavyDriver.getEmployeeId()));
    }

    @Test
    @Order(4)
    void test04_transportIntegration_DestinationRequiresStorekeeperButMissing() {
        boolean hasStorekeeper = mondayShift.shiftRequiresRole(storekeeperRole);
        assertFalse(hasStorekeeper);
    }

    @Test
    @Order(5)
    void test05_transportIntegration_StorekeeperAssignedForDeliveryFulfillment() {
        mondayShift.assignEmployeeToRole(storekeeperRole, "SK-1");
        assertTrue(mondayShift.shiftRequiresRole(storekeeperRole));
    }

    @Test
    @Order(6)
    void test06_inventoryIntegration_CalculateTotalStockAgainstThreshold() {
        int totalStock = inventoryProduct.getShelfQuantity() + inventoryProduct.getWarehouseQuantity();
        assertEquals(120, totalStock);
        assertTrue(totalStock >= inventoryProduct.getMinQuantity());
    }

    @Test
    @Order(7)
    void test07_inventoryIntegration_StockDropsBelowMinimumTriggersAlert() {
        inventoryProduct.setWarehouseQuantity(10);
        int totalStock = inventoryProduct.getShelfQuantity() + inventoryProduct.getWarehouseQuantity();
        assertEquals(30, totalStock);
        assertTrue(totalStock < inventoryProduct.getMinQuantity());
    }

    @Test
    @Order(8)
    void test08_inventoryIntegration_CalculateExactShortageOrderQuantity() {
        int targetCapacity = inventoryProduct.getMinQuantity() * 2;
        int currentStock = inventoryProduct.getShelfQuantity() + inventoryProduct.getWarehouseQuantity();
        int orderQuantity = targetCapacity - currentStock;
        assertEquals(70, orderQuantity);
    }

    @Test
    @Order(9)
    void test09_supplierIntegration_SupplierTransportsDeliveryVerification() {
        assertTrue(supplierAgreement.getDeliveryTerms().isSupplierTransports());
    }

    @Test
    @Order(10)
    void test10_supplierIntegration_ValidDeliveryDayMatch() {
        List<DayOfWeek> days = supplierAgreement.getDeliveryTerms().getFixedDeliveryDays();
        assertTrue(days.contains(DayOfWeek.WEDNESDAY));
    }
}