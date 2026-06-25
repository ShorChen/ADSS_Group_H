package Transportation.DataAccess;

import Transportation.Domain.Entities.*;
import java.util.List;

public interface TransportationDAO {
    void updateDelivery(DeliveryDL delivery);
    void addTruck(TruckDL truck);
    @SuppressWarnings("unused")
    TruckDL getTruck(String licenseNumber);
    List<TruckDL> getAllTrucks();
    void addDriver(DriverDL driver);
    @SuppressWarnings("unused")
    DriverDL getDriver(String employeeId);
    List<DriverDL> getAllDrivers();
    void addSite(SiteDL site);
    @SuppressWarnings("unused")
    SiteDL getSite(String siteName);
    List<SiteDL> getAllSites();
    int addDelivery(DeliveryDL delivery);
    @SuppressWarnings("unused")
    DeliveryDL getDelivery(int deliveryId);
    List<DeliveryDL> getAllDeliveries();
    void updateDeliveryStatus(int deliveryId, String status);
}