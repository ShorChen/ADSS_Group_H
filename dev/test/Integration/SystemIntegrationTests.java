package Integration;

import Core.Service.Response;
import Inventory.Service.Core.InventoryService;
import Suppliers.Service.Core.OrderService;
import Transportation.Service.Core.TransportService;
import Workers.Domain.Service.EmployeeService;

import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemIntegrationTests {

    // Your Services
    private InventoryService inventoryService;
    private OrderService orderService;
    private TransportService transportService;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        // [!] Initialize your Services and Facades here.
        // Example:
        // inventoryFacade = new InventoryFacade(new SqlInventoryDAO());
        // inventoryService = new InventoryService(inventoryFacade);
        // orderService = new OrderService(supplierFacade, orderFacade);
        // transportService = new TransportService(transportFacade);
        // employeeService = new EmployeeService(employeesFacade);

        // WIRE THE INTEGRATIONS FOR ASSIGNMENT 2:
        // inventoryService.setOrderService(orderService);
        // transportService.setEmployeeService(employeeService);

        // [!] Seed your database/maps here so the tests have data to work with.
        // Add Product: "Milk" (Min: 20, Current: 30)
        // Add Product: "Bread" (No suppliers sell this item)
        // Add Supplier: "Tnuva" (Sells Milk)
        // Add Driver: "D_HEAVY" (License: HEAVY, Role: DRIVER)
        // Add Driver: "D_LIGHT" (License: LIGHT, Role: DRIVER)
        // Add Cashier: "CASH_1" (License: NONE, Role: CASHIER)
        // Add Truck: "TRK_HEAVY" (Weight: 15000 kg)
    }

    // =================================================================================
    // INTEGRATION 1: INVENTORY & SUPPLIERS (Automatic Shortage Orders)
    // =================================================================================

    @Test
    @DisplayName("1. Reporting a defect that drops stock BELOW minimum triggers a shortage order")
    void testDefectDropsStockBelowMinimum_TriggersOrder() {
        // Milk starts at 30. Min is 20. Reporting 15 defects drops stock to 15.
        // Expectation: A shortage order for 5 Milk should be automatically placed.
        Response<String> res = inventoryService.reportDefectiveItem("MILK_BARCODE", 15, "SHELF", "Broken");

        assertTrue(res.isSuccess());
        assertTrue(res.getData().contains("Automatic shortage order placed successfully"),
                "The system should have warned that an automatic order was placed.");
    }

    @Test
    @DisplayName("2. Reporting a defect that keeps stock ABOVE minimum does NOT trigger an order")
    void testDefectMaintainsStockAboveMinimum_NoOrderTriggered() {
        // Milk starts at 30. Min is 20. Reporting 2 defects drops stock to 28.
        Response<String> res = inventoryService.reportDefectiveItem("MILK_BARCODE", 2, "SHELF", "Broken");

        assertTrue(res.isSuccess());
        assertFalse(res.getData().contains("Automatic shortage order placed"),
                "An order should not be placed if stock is still above minimum.");
    }

    @Test
    @DisplayName("3. Updating pricing to increase the Minimum Threshold triggers an immediate order")
    void testUpdatePricingIncreasesMinimum_TriggersOrder() {
        // Milk starts at 30. Min is 20.
        // We change the minimum required to 40. Now we are short 10!
        Response<String> res = inventoryService.updateProductPricing("MILK_BARCODE", 6.50, 40);

        assertTrue(res.isSuccess());
        assertTrue(res.getData().contains("Automatic shortage order placed successfully"));
    }

    @Test
    @DisplayName("4. Shortage order FAILS because no supplier sells the product -> Rolls back inventory")
    void testShortageOrderFails_TriggersInventoryRollback() {
        // Bread starts at 30. Min is 20. We drop stock to 10.
        // OrderService will try to order Bread, but no supplier sells it.
        Response<String> res = inventoryService.updateProductQuantities("BREAD_BARCODE", 5, 5);

        assertFalse(res.isSuccess(), "The operation should fail because the order couldn't be placed.");
        assertTrue(res.getErrorMessage().contains("reverted"),
                "The inventory update should be rolled back to maintain data consistency.");
    }

    @Test
    @DisplayName("5. Shortage order successfully selects the cheapest supplier ignoring case")
    void testGenerateShortageOrder_FindsCheapestSupplier() {
        // Directly test the OrderService integration logic for 50 missing items of "milk"
        Response<Integer> res = orderService.generateShortageOrder("milk", 50);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData(), "Should return a valid Order ID for the cheapest supplier.");
    }

    // =================================================================================
    // INTEGRATION 2: TRANSPORTATION & EMPLOYEES (Driver License Validation)
    // =================================================================================

    @Test
    @DisplayName("6. Transport allows delivery if driver has the CORRECT license for heavy truck")
    void testTransportation_ValidDriverLicenseForHeavyTruck() {
        // Truck is 15,000kg. Driver D_HEAVY has a HEAVY license.
        Response<Boolean> res = transportService.validateDriverLicense("D_HEAVY", 15000.0);

        assertTrue(res.isSuccess());
        assertTrue(res.getData(), "Driver with HEAVY license should be valid for 15000kg truck.");
    }

    @Test
    @DisplayName("7. Transport BLOCKS delivery if driver has INCORRECT license for heavy truck")
    void testTransportation_InvalidDriverLicenseForHeavyTruck() {
        // Truck is 15,000kg. Driver D_LIGHT only has a LIGHT license.
        Response<Boolean> res = transportService.validateDriverLicense("D_LIGHT", 15000.0);

        assertFalse(res.isSuccess());
        assertTrue(res.getErrorMessage().toLowerCase().contains("insufficient license"),
                "System must block a light-licensed driver from driving a heavy truck.");
    }

    @Test
    @DisplayName("8. Transport BLOCKS delivery if the requested Driver ID does not exist")
    void testTransportation_DriverDoesNotExist() {
        // Calling EmployeeService with a fake ID
        Response<Boolean> res = transportService.validateDriverLicense("FAKE_ID_999", 5000.0);

        assertFalse(res.isSuccess());
        assertTrue(res.getErrorMessage().toLowerCase().contains("not found"),
                "System must fail gracefully if the employee does not exist.");
    }

    @Test
    @DisplayName("9. Transport BLOCKS delivery if the Employee is NOT a Driver (e.g., Cashier)")
    void testTransportation_EmployeeIsNotADriver() {
        // Employee exists, but their JobScope is CASHIER, not DRIVER.
        Response<Boolean> res = transportService.validateDriverLicense("CASH_1", 5000.0);

        assertFalse(res.isSuccess());
        assertTrue(res.getErrorMessage().toLowerCase().contains("not a driver"),
                "System must ensure the employee actually holds the DRIVER job scope.");
    }

    @Test
    @DisplayName("10. Full Delivery Issuance fails completely if license check fails")
    void testIssueDelivery_IntegrationFailsOnInvalidLicense() {
        // Try to issue an entire delivery routing using the light driver on the heavy truck
        Response<Integer> res = transportService.issueDelivery("TRK_HEAVY", "D_LIGHT", LocalDate.now(), new ArrayList<>());

        assertFalse(res.isSuccess(), "The delivery should not be created.");
        assertTrue(res.getErrorMessage().toLowerCase().contains("license validation failed"),
                "The root cause should bubble up from the Employee validation.");
    }
}