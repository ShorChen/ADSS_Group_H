package Integration;
import Core.Domain.Role;
import Inventory.Domain.Entities.ProductDL;
import Suppliers.Domain.Entities.OrderDL;
import Suppliers.Domain.Security.SessionManager;
import Transportation.Domain.Entities.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemIntegrationTests {
    private ProductDL inventoryProduct;
    private OrderDL generatedOrder;
    private DeliveryDL generatedDelivery;
    @Test
    @Order(1)
    void step01_loginAsManager() {
        SessionManager.getInstance().login(Role.ORDER_MANAGER);
        assertEquals(Role.ORDER_MANAGER, SessionManager.getInstance().getCurrentRole());
    }
    @Test
    @Order(2)
    void step02_initializeInventory() {
        inventoryProduct = new ProductDL("1001", "Milk 3%", "Tnuva", 1, 4.0, 6.0, 50, 20, 10, "A1", 1);
        assertNotNull(inventoryProduct);
        assertEquals(30, inventoryProduct.getTotalQuantity());
    }
    @Test
    @Order(3)
    void step03_verifyStockShortage() {
        assertTrue(inventoryProduct.getTotalQuantity() < inventoryProduct.getMinQuantity());
    }
    @Test
    @Order(4)
    void step04_generateShortageOrder() {
        generatedOrder = new OrderDL(1, "1111", "Tnuva", "Tel Aviv", "050-0000000", new ArrayList<>());
        assertEquals("1111", generatedOrder.getSupplierBusinessNumber());
    }
    @Test
    @Order(5)
    void step05_verifyOrderAttributes() {
        assertNotNull(generatedOrder.getOrderDate());
        assertEquals("Tnuva", generatedOrder.getSupplierName());
    }
    @Test
    @Order(6)
    void step06_setupTransportInfrastructure() {
        TruckDL truck = new TruckDL("11-222-33", "Volvo", 5000.0, 15000.0, true);
        DriverDL driver = new DriverDL("D001", "Moshe", "HEAVY", LocalDate.now().plusYears(1));
        assertNotNull(truck);
        assertNotNull(driver);
    }
    @Test
    @Order(7)
    void step07_createTransportRoute() {
        DestinationDL dest = new DestinationDL("Branch 1", new ArrayList<>());
        dest.getItems().add(new CargoItemDL("Milk", 10.0, 20));
        List<DestinationDL> route = new ArrayList<>();
        route.add(dest);
        generatedDelivery = new DeliveryDL(1, LocalDate.now(), "08:00", "PENDING", "11-222-33", "D001", "Tnuva", route);
        assertEquals(1, generatedDelivery.getDestinations().size());
    }
    @Test
    @Order(8)
    void step08_verifyCargoLoading() {
        assertEquals(20, generatedDelivery.getDestinations().get(0).getItems().get(0).getQuantity());
    }
    @Test
    @Order(9)
    void step09_verifyDeliveryState() {
        assertEquals("PENDING", generatedDelivery.getStatus());
        assertEquals("Tnuva", generatedDelivery.getOriginSite());
    }
    @Test
    @Order(10)
    void step10_logoutAndClearSession() {
        SessionManager.getInstance().logout();
        assertThrows(SecurityException.class, () -> SessionManager.getInstance().getCurrentRole());
    }
}