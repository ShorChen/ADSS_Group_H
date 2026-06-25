package Transportation.Service.Core;

import Core.Service.Response;
import Employees.Service.Core.BranchManagerService;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Transportation.DataAccess.TransportationDAO;
import Transportation.Domain.Entities.CargoItemDL;
import Transportation.Domain.Entities.DestinationDL;
import Transportation.Domain.Entities.DriverDL;
import Transportation.Domain.Entities.TruckDL;
import Transportation.Domain.Facades.TransportationFacade;
import Transportation.Service.DTO.*;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public class TransportationService {
    private final TransportationFacade transportationFacade;
    private final TransportationDAO transportationDAO;
    private final BranchManagerService branchManagerService;

    public TransportationService(TransportationFacade transportationFacade, TransportationDAO transportationDAO, Employees.Service.Core.BranchManagerService branchManagerService) {
        this.transportationDAO = transportationDAO;
        this.transportationFacade = transportationFacade;
        this.branchManagerService = branchManagerService;
    }

    public Response<Boolean> addTruck(String licenseNumber, String model, double netWeight, double maxWeight, boolean isRefrigerated) {
        try {
            transportationFacade.addTruck(licenseNumber, model, netWeight, maxWeight, isRefrigerated);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<TruckSL>> getAllTrucks() {
        try {
            List<TruckSL> list = transportationFacade.getAllTrucks().stream().map(t -> new TruckSL(t.getLicenseNumber(), t.getModel(), t.getNetWeight(), t.getMaxWeight(), t.isRefrigerated())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> addDriver(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {
        try {
            transportationFacade.addDriver(employeeId, name, licenseType, licenseExpiry);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<DriverSL>> getAllDrivers() {
        try {
            List<DriverSL> list = transportationFacade.getAllDrivers().stream().map(d -> new DriverSL(d.getEmployeeId(), d.getName(), d.getLicenseType(), d.getLicenseExpiry())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> addSite(String siteName, String address, String contactPerson, String phoneNumber) {
        try {
            transportationFacade.addSite(siteName, address, contactPerson, phoneNumber);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<SiteSL>> getAllSites() {
        try {
            List<SiteSL> list = transportationFacade.getAllSites().stream().map(s -> new SiteSL(s.getSiteName(), s.getAddress(), s.getContactPerson(), s.getPhoneNumber())).collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Integer> createDelivery(LocalDate date, String departureTime, String truckLicense, String driverId, String originSite, List<DestinationSL> destinations, String managerId) {
        try {
            verifyDriverLicenseForTruck(driverId, truckLicense);

            List<DestinationDL> dlDests = destinations.stream().map(dest -> {
                List<CargoItemDL> cargo = dest.items().stream().map(item -> new CargoItemDL(item.itemName(), item.weight(), item.quantity())).collect(Collectors.toList());
                return new DestinationDL(dest.destinationSite(), cargo);
            }).collect(Collectors.toList());

            verifyDriverShift(driverId, date, departureTime, dlDests);
            verifyStorekeeperAtDestinations(date, departureTime, dlDests);

            int id = transportationFacade.createDelivery(date, departureTime, truckLicense, driverId, originSite, dlDests, managerId);
            return new Response<>(id);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<Boolean> cancelDelivery(int deliveryId) {
        try {
            transportationFacade.cancelDelivery(deliveryId);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<DeliverySL>> getAllDeliveries() {
        try {
            List<DeliverySL> list = transportationFacade.getAllDeliveries().stream().map(d -> {
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
            Transportation.Domain.Entities.DeliveryDL delivery = transportationDAO.getDelivery(deliveryId);
            if (delivery == null) {
                throw new IllegalArgumentException("Error! delivery with id " + deliveryId + " does not exist in the system.");
            }

            verifyDriverLicenseForTruck(newDriverId, newTruckLicense);

            verifyDriverShift(newDriverId, delivery.getDate(), delivery.getDepartureTime(), delivery.getDestinations());
            verifyStorekeeperAtDestinations(delivery.getDate(), delivery.getDepartureTime(), delivery.getDestinations());

            transportationFacade.replanDelivery(deliveryId, newTruckLicense, newDriverId);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    private void verifyDriverLicenseForTruck(String driverId, String truckLicense) {
        DriverDL driver = transportationDAO.getDriver(driverId);
        TruckDL truck = transportationDAO.getTruck(truckLicense);

        if (driver == null) {
            throw new IllegalArgumentException("driver does not exist in the system.");
        }
        if (truck == null) {
            throw new IllegalArgumentException("truck does not exist in the system.");
        }

        String license = driver.getLicenseType();
        double weight = truck.getMaxWeight();

        if (license.equalsIgnoreCase("C1") && weight > 12.0) {
            throw new IllegalArgumentException("Error! the driver " + driver.getName() + " does not have the required license to drive this truck.");
        }
    }

    private void verifyDriverShift(String driverId, LocalDate date, String departureTime, List<Transportation.Domain.Entities.DestinationDL> destinations) {
        int year = date.getYear();
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int dayOfWeek = date.getDayOfWeek().getValue();
        WeekDay day = WeekDay.fromInteger(dayOfWeek % 7);

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int defaultBranchId = 1;

        ShiftType startShiftType = (departureHour < 14) ? ShiftType.DAY : ShiftType.EVENING;

        // INTEGRATION: Simply ask the Employee module!
        boolean isWorking = branchManagerService.isEmployeeWorking(defaultBranchId, year, week, day, startShiftType, driverId);

        if (!isWorking) {
            throw new IllegalArgumentException(" Error! the driver is not scheduled to work during the departure time of the delivery.");
        }
    }

    private void verifyStorekeeperAtDestinations(LocalDate date, String departureTime, List<Transportation.Domain.Entities.DestinationDL> destinations) {
        int year = date.getYear();
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int dayOfWeek = date.getDayOfWeek().getValue();
        WeekDay day = WeekDay.fromInteger(dayOfWeek % 7);

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int defaultBranchId = 1;

        ShiftType shiftType = (departureHour < 14) ? ShiftType.DAY : ShiftType.EVENING;

        for (Transportation.Domain.Entities.DestinationDL dest : destinations) {
            // INTEGRATION: Simply ask the Employee module if a Storekeeper is present!
            boolean hasStorekeeper = branchManagerService.isRoleAssigned(defaultBranchId, year, week, day, shiftType, "Storekeeper");

            if (!hasStorekeeper) {
                throw new IllegalArgumentException("The destination site (" + dest.getDestinationSite() + ") does not have a storekeeper scheduled to work during the delivery time.");
            }
        }
    }
}