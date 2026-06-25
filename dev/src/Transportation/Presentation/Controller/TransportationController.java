package Transportation.Presentation.Controller;

import Core.Service.Response;
import Transportation.Presentation.DTO.*;
import Transportation.Service.Core.TransportationService;
import Transportation.Service.DTO.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"UnusedReturnValue", "ClassCanBeRecord"})
public class TransportationController {
    private final TransportationService transportationService;

    public TransportationController(TransportationService transportationService) {
        this.transportationService = transportationService;
    }

    public void addTruck(String licenseNumber, String model, double netWeight, double maxWeight, boolean isRefrigerated) {
        Response<Boolean> response = transportationService.addTruck(licenseNumber, model, netWeight, maxWeight, isRefrigerated);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public List<TruckPL> getAllTrucks() {
        Response<List<TruckSL>> response = transportationService.getAllTrucks();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new TruckPL(sl.licenseNumber(), sl.model(), sl.netWeight(), sl.maxWeight(), sl.isRefrigerated())).collect(Collectors.toList());
    }

    public void addDriver(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {
        Response<Boolean> response = transportationService.addDriver(employeeId, name, licenseType, licenseExpiry);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public List<DriverPL> getAllDrivers() {
        Response<List<DriverSL>> response = transportationService.getAllDrivers();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new DriverPL(sl.employeeId(), sl.name(), sl.licenseType(), sl.licenseExpiry())).collect(Collectors.toList());
    }

    public void addSite(String siteName, String address, String contactPerson, String phoneNumber) {
        Response<Boolean> response = transportationService.addSite(siteName, address, contactPerson, phoneNumber);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public List<SitePL> getAllSites() {
        Response<List<SiteSL>> response = transportationService.getAllSites();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(sl -> new SitePL(sl.siteName(), sl.address(), sl.contactPerson(), sl.phoneNumber())).collect(Collectors.toList());
    }

    public int createDelivery(LocalDate date, String departureTime, String truckLicense, String driverId, String originSite, List<DestinationPL> destinations, String managerId) {
        List<DestinationSL> slDests = destinations.stream().map(dest -> {
            List<CargoItemSL> cargo = dest.items().stream().map(item -> new CargoItemSL(item.itemName(), item.weight(), item.quantity())).collect(Collectors.toList());
            return new DestinationSL(dest.destinationSite(), cargo);
        }).collect(Collectors.toList());
        Response<Integer> response = transportationService.createDelivery(date, departureTime, truckLicense, driverId, originSite, slDests, managerId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData();
    }

    public void cancelDelivery(int deliveryId) {
        Response<Boolean> response = transportationService.cancelDelivery(deliveryId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    public List<DeliveryPL> getAllDeliveries() {
        Response<List<DeliverySL>> response = transportationService.getAllDeliveries();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
        return response.getData().stream().map(d -> {
            List<DestinationPL> dests = d.destinations().stream().map(dest -> {
                List<CargoItemPL> items = dest.items().stream().map(i -> new CargoItemPL(i.itemName(), i.weight(), i.quantity())).collect(Collectors.toList());
                return new DestinationPL(dest.destinationSite(), items);
            }).collect(Collectors.toList());
            return new DeliveryPL(d.deliveryId(), d.date(), d.departureTime(), d.status(), d.truckLicense(), d.driverId(), d.originSite(), dests);
        }).collect(Collectors.toList());
    }

    public void replanDelivery(int deliveryId, String newTruckLicense, String newDriverId) {
        Response<Boolean> response = transportationService.replanDelivery(deliveryId, newTruckLicense, newDriverId);
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }
}