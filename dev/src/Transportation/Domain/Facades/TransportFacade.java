package Transportation.Domain.Facades;

import Transportation.DataAccess.TransportDAO;
import Transportation.Domain.Entities.*;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class TransportFacade {
    private final TransportDAO transportDAO;

    public TransportFacade(TransportDAO transportDAO) {
        this.transportDAO = transportDAO;
    }

    public void addTruck(String licenseNumber, String model, double netWeight, double maxWeight, boolean isRefrigerated) {
        transportDAO.addTruck(new TruckDL(licenseNumber, model, netWeight, maxWeight, isRefrigerated));
    }

    public void addDriver(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {
        transportDAO.addDriver(new DriverDL(employeeId, name, licenseType, licenseExpiry));
    }

    public void addSite(String siteName, String address, String contactPerson, String phoneNumber) {
        transportDAO.addSite(new SiteDL(siteName, address, contactPerson, phoneNumber));
    }

    public int createDelivery(LocalDate date, String departureTime, String truckLicense, String driverId, String originSite, List<DestinationDL> destinations, String managerId) {
        TruckDL truck = transportDAO.getTruck(truckLicense);
        if (truck == null) throw new IllegalArgumentException("Truck not found.");
        DriverDL driver = transportDAO.getDriver(driverId);
        if (driver == null) throw new IllegalArgumentException("Driver not found.");
        if (driver.getLicenseType().equals("STANDARD") && truck.getMaxWeight() > 12000) {
            throw new IllegalArgumentException("Driver license does not authorize this truck.");
        }
        double totalCargoWeight = 0;
        for (DestinationDL dest : destinations) {
            for (CargoItemDL item : dest.getItems()) {
                totalCargoWeight += (item.getWeight() * item.getQuantity());
            }
        }
        if (truck.isOverweight(totalCargoWeight)) {
            throw new IllegalArgumentException("Total cargo weight exceeds truck capacity.");
        }
        DeliveryDL delivery = new DeliveryDL(0, date, departureTime, "PENDING", truckLicense, driverId, originSite, destinations);
        TransportationManager manager = new TransportationManager(managerId, "System Manager");
        manager.authorizeDelivery(delivery);
        return transportDAO.addDelivery(delivery);
    }

    public void replanDelivery(int deliveryId, String newTruckLicense, String newDriverId) {
        DeliveryDL delivery = transportDAO.getDelivery(deliveryId);
        if (delivery == null) throw new IllegalArgumentException("Delivery not found.");
        TruckDL truck = transportDAO.getTruck(newTruckLicense);
        if (truck == null) throw new IllegalArgumentException("New truck not found.");
        DriverDL driver = transportDAO.getDriver(newDriverId);
        if (driver == null) throw new IllegalArgumentException("New driver not found.");
        if (driver.getLicenseType().equals("STANDARD") && truck.getMaxWeight() > 12000) {
            throw new IllegalArgumentException("Driver license does not authorize this truck.");
        }
        double totalCargoWeight = 0;
        for (DestinationDL dest : delivery.getDestinations()) {
            for (CargoItemDL item : dest.getItems()) {
                totalCargoWeight += (item.getWeight() * item.getQuantity());
            }
        }
        if (truck.isOverweight(totalCargoWeight)) {
            throw new IllegalArgumentException("New truck cannot hold existing cargo weight.");
        }
        delivery.setTruckLicense(newTruckLicense);
        delivery.setDriverId(newDriverId);
        delivery.setStatus("REPLANNED");
        transportDAO.updateDelivery(delivery);
    }

    public void cancelDelivery(int deliveryId) {
        transportDAO.updateDeliveryStatus(deliveryId, "CANCELLED");
    }

    public List<TruckDL> getAllTrucks() { return transportDAO.getAllTrucks(); }

    public List<DriverDL> getAllDrivers() { return transportDAO.getAllDrivers(); }

    public List<SiteDL> getAllSites() { return transportDAO.getAllSites(); }

    public List<DeliveryDL> getAllDeliveries() { return transportDAO.getAllDeliveries(); }
}