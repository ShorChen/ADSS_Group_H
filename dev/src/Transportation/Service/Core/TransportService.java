package Transportation.Service.Core;

import Core.Service.Response;
import Transportation.Domain.Entities.*;
import Transportation.Domain.Facades.TransportFacade;
import Transportation.Service.DTO.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public class TransportService {
    private final TransportFacade transportFacade;

    public TransportService(TransportFacade transportFacade) {
        this.transportFacade = transportFacade;
    }

    public Response<Boolean> addTruck(String licenseNumber, String model, double netWeight, double maxWeight, boolean isRefrigerated) {
        try {
            transportFacade.addTruck(licenseNumber, model, netWeight, maxWeight, isRefrigerated);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<TruckSL>> getAllTrucks() {
        try {
            List<TruckSL> list = transportFacade.getAllTrucks().stream().map(t -> new TruckSL(t.getLicenseNumber(), t.getModel(), t.getNetWeight(), t.getMaxWeight(), t.isRefrigerated())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> addDriver(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {
        try {
            transportFacade.addDriver(employeeId, name, licenseType, licenseExpiry);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<DriverSL>> getAllDrivers() {
        try {
            List<DriverSL> list = transportFacade.getAllDrivers().stream().map(d -> new DriverSL(d.getEmployeeId(), d.getName(), d.getLicenseType(), d.getLicenseExpiry())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> addSite(String siteName, String address, String contactPerson, String phoneNumber) {
        try {
            transportFacade.addSite(siteName, address, contactPerson, phoneNumber);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<SiteSL>> getAllSites() {
        try {
            List<SiteSL> list = transportFacade.getAllSites().stream().map(s -> new SiteSL(s.getSiteName(), s.getAddress(), s.getContactPerson(), s.getPhoneNumber())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Integer> createDelivery(LocalDate date, String departureTime, String truckLicense, String driverId, String originSite, List<DestinationSL> destinations, String managerId) {
        try {
            List<DestinationDL> dlDests = destinations.stream().map(dest -> {
                List<CargoItemDL> cargo = dest.items().stream().map(item -> new CargoItemDL(item.itemName(), item.weight(), item.quantity())).collect(Collectors.toList());
                return new DestinationDL(dest.destinationSite(), cargo);
            }).collect(Collectors.toList());
            int id = transportFacade.createDelivery(date, departureTime, truckLicense, driverId, originSite, dlDests, managerId);
            return new Response<>(id);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> cancelDelivery(int deliveryId) {
        try {
            transportFacade.cancelDelivery(deliveryId);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<DeliverySL>> getAllDeliveries() {
        try {
            List<DeliverySL> list = transportFacade.getAllDeliveries().stream().map(d -> {
                List<DestinationSL> dests = d.getDestinations().stream().map(dest -> {
                    List<CargoItemSL> items = dest.getItems().stream().map(i -> new CargoItemSL(i.getItemName(), i.getWeight(), i.getQuantity())).collect(Collectors.toList());
                    return new DestinationSL(dest.getDestinationSite(), items);
                }).collect(Collectors.toList());
                return new DeliverySL(d.getDeliveryId(), d.getDate(), d.getDepartureTime(), d.getStatus(), d.getTruckLicense(), d.getDriverId(), d.getOriginSite(), dests);
            }).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> replanDelivery(int deliveryId, String newTruckLicense, String newDriverId) {
        try {
            transportFacade.replanDelivery(deliveryId, newTruckLicense, newDriverId);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }
}