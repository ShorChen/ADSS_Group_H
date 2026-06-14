package Transportation;

import Core.Domain.Role;
import Suppliers.Domain.Security.SessionManager;
import Transportation.Domain.Entities.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialTransportationTests {
    private TruckDL currentTruck;
    private DriverDL currentDriver;
    private SiteDL originSite;
    private DeliveryDL currentDelivery;
    private final TransportationManager manager = new TransportationManager("TRANS123", "Main Manager");

    @Test
    @Order(1)
    void step01_loginAsTransportManager() {
        SessionManager.getInstance().login(Role.TRANSPORTATION_MANAGER);
        assertEquals(Role.TRANSPORTATION_MANAGER, SessionManager.getInstance().getCurrentRole());
    }

    @Test
    @Order(2)
    void step02_createTruckAndVerifyWeightLogic() {
        currentTruck = new TruckDL("11-222-33", "Volvo", 5000.0, 15000.0, true);
        assertNotNull(currentTruck);
        assertFalse(currentTruck.isOverweight(9000.0));
        assertTrue(currentTruck.isOverweight(11000.0));
    }

    @Test
    @Order(3)
    void step03_verifyTruckRefrigerationAndModel() {
        assertTrue(currentTruck.isRefrigerated());
        assertEquals("Volvo", currentTruck.getModel());
    }

    @Test
    @Order(4)
    void step04_createDriverAndSite() {
        currentDriver = new DriverDL("D001", "Moshe", "HEAVY", LocalDate.now().plusYears(1));
        originSite = new SiteDL("Logistics Center", "Ashdod", "Eran", "050-1234567");
        assertEquals("HEAVY", currentDriver.getLicenseType());
        assertEquals("Logistics Center", originSite.getSiteName());
    }

    @Test
    @Order(5)
    void step05_verifyDriverLicenseValidity() {
        LocalDate expiry = currentDriver.getLicenseExpiry();
        assertTrue(expiry.isAfter(LocalDate.now()));
    }

    @Test
    @Order(6)
    void step06_createDestinationWithCargo() {
        List<CargoItemDL> items = new ArrayList<>();
        items.add(new CargoItemDL("Milk", 50.0, 10));
        items.add(new CargoItemDL("Bread", 10.0, 20));
        DestinationDL dest = new DestinationDL("Super-Lee Tel Aviv", items);
        assertEquals(2, dest.getItems().size());
        List<DestinationDL> route = new ArrayList<>();
        route.add(dest);
        currentDelivery = new DeliveryDL(1, LocalDate.now(), "08:00", "PENDING", currentTruck.getLicenseNumber(), currentDriver.getEmployeeId(), originSite.getSiteName(), route);
        assertEquals("PENDING", currentDelivery.getStatus());
    }

    @Test
    @Order(7)
    void step07_managerAuthorizesDelivery() {
        manager.authorizeDelivery(currentDelivery);
        assertEquals("AUTHORIZED_BY_TRANS123", currentDelivery.getStatus());
    }

    @Test
    @Order(8)
    void step08_replanDelivery() {
        currentDelivery.setTruckLicense("44-555-66");
        currentDelivery.setDriverId("D002");
        currentDelivery.setStatus("REPLANNED");
        assertEquals("44-555-66", currentDelivery.getTruckLicense());
        assertEquals("REPLANNED", currentDelivery.getStatus());
    }

    @Test
    @Order(9)
    void step09_completeDeliveryStatus() {
        currentDelivery.setStatus("COMPLETED");
        assertEquals("COMPLETED", currentDelivery.getStatus());
    }

    @Test
    @Order(10)
    void step10_logout() {
        SessionManager.getInstance().logout();
        assertThrows(SecurityException.class, () -> SessionManager.getInstance().getCurrentRole());
    }
}