package Transportation.Service.Core;

import Core.Service.Response;
import Transportation.DataAccess.TransportDAO;
import Transportation.Domain.Entities.*;
import Transportation.Domain.Facades.TransportFacade;
import Transportation.Service.DTO.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import Workers.Domain.Service.ShiftService;
import Workers.Shared.Enums.ShiftType;

import java.time.temporal.IsoFields;

@SuppressWarnings("ClassCanBeRecord")
public class TransportService {
    private final TransportFacade transportFacade;
    private final TransportDAO transportDAO;
    private final ShiftService shiftService;


    public TransportService(TransportFacade transportFacade, TransportDAO transportDAO, ShiftService shiftService) {
        this.transportDAO = transportDAO;
        this.transportFacade = transportFacade;
        this.shiftService = shiftService;
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
            verifyDriverLicenseForTruck(driverId, truckLicense);

            List<DestinationDL> dlDests = destinations.stream().map(dest -> {
                List<CargoItemDL> cargo = dest.items().stream().map(item -> new CargoItemDL(item.itemName(), item.weight(), item.quantity())).collect(Collectors.toList());
                return new DestinationDL(dest.destinationSite(), cargo);
            }).collect(Collectors.toList());

            verifyDriverShift(driverId, date, departureTime, dlDests);
            verifyStorekeeperAtDestinations(date, departureTime, dlDests);

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
            Transportation.Domain.Entities.DeliveryDL delivery = transportDAO.getDelivery(deliveryId);
            if (delivery == null) {
                throw new IllegalArgumentException( "Eror! delivery with id " + deliveryId + " does not exist in the system." );
            }

            verifyDriverLicenseForTruck(newDriverId, newTruckLicense);

            verifyDriverShift(newDriverId, delivery.getDate(), delivery.getDepartureTime(), delivery.getDestinations());
            verifyStorekeeperAtDestinations(delivery.getDate(), delivery.getDepartureTime(), delivery.getDestinations());

            transportFacade.replanDelivery(deliveryId, newTruckLicense, newDriverId);
            return new Response<>(true);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    private void verifyDriverLicenseForTruck(String driverId, String truckLicense) {
        DriverDL driver = transportDAO.getDriver(driverId);
        TruckDL truck = transportDAO.getTruck(truckLicense);

        if (driver == null) {
            throw new IllegalArgumentException("driver does not exist in the system.");
        }
        if (truck == null) {
            throw new IllegalArgumentException("truck does not exist in the system.");
        }

        String license = driver.getLicenseType();
        double weight = truck.getMaxWeight();

        if (license.equalsIgnoreCase("C1") && weight > 12.0) {
            throw new IllegalArgumentException("Error! the driver " + driver.getName() + " not have the required license to to drive in this truck " );
        }
    }

    private void verifyDriverShift(String driverId, LocalDate date, String departureTime, List<Transportation.Domain.Entities.DestinationDL> destinations) {
        int year = date.getYear();
        int week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int dayOfWeek = date.getDayOfWeek().getValue();
        Workers.Shared.Enums.WeekDay day = Workers.Shared.Enums.WeekDay.fromInteger(dayOfWeek % 7);

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);

        //assert that estimated duration is at least 2 hours
        int estimatedDurationHours = destinations.size() * 2;
        int arrivalHour = departureHour + estimatedDurationHours;

        int defaultBranchId = 1;
        java.util.Map<Workers.Domain.Entities.ShiftKey, Workers.Domain.Entities.Shift> weekShifts = shiftService.getShiftsOfWeek(defaultBranchId, year, week);

        Workers.Shared.Enums.ShiftType startShiftType = (departureHour < 14) ? ShiftType.DAY : ShiftType.EVENING;
        Workers.Domain.Entities.Shift startShift = weekShifts.get(new Workers.Domain.Entities.ShiftKey(day, startShiftType));

        if (startShift == null || !startShift.doesEmployeeWork(driverId)) {
            throw new IllegalArgumentException(" Error! the driver is not scheduled to work during the departure time of the delivery.");
        }

        if (departureHour < 14 && arrivalHour >= 14) {
            Workers.Domain.Entities.Shift endShift = weekShifts.get(new Workers.Domain.Entities.ShiftKey(day, ShiftType.EVENING));

            if (endShift == null || !endShift.doesEmployeeWork(driverId)) {
                throw new IllegalArgumentException("Error: the driver is not assigned to the following evening shift to complete it.");
            }
        }
    }

    private void verifyStorekeeperAtDestinations(LocalDate date, String departureTime, List<Transportation.Domain.Entities.DestinationDL> destinations) {
        int year = date.getYear();
        int week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int dayOfWeek = date.getDayOfWeek().getValue();
        Workers.Shared.Enums.WeekDay day = Workers.Shared.Enums.WeekDay.fromInteger(dayOfWeek % 7);

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int currentHour = departureHour;

        int defaultBranchId = 1;
        java.util.Map<Workers.Domain.Entities.ShiftKey, Workers.Domain.Entities.Shift> weekShifts = shiftService.getShiftsOfWeek(defaultBranchId, year, week);

        for (Transportation.Domain.Entities.DestinationDL dest : destinations) {
            currentHour += 2;

            Workers.Shared.Enums.ShiftType arrivalShiftType = (currentHour < 14) ? ShiftType.DAY : Workers.Shared.Enums.ShiftType.EVENING;
            Workers.Domain.Entities.Shift destinationShift = weekShifts.get(new Workers.Domain.Entities.ShiftKey(day, arrivalShiftType));

            if (destinationShift == null) {
                throw new IllegalArgumentException("Error: the destination site (" + dest.getDestinationSite() + ") does not have a shift scheduled during the estimated arrival time.");
            }

            boolean hasStorekeeper = false;
            for (java.util.Map.Entry<Workers.Domain.Entities.Role, java.util.Set<String>> entry : destinationShift.getEmployees().entrySet()) {
                if (entry.getKey().getTag().equalsIgnoreCase("Storekeeper") && !entry.getValue().isEmpty()) {
                    hasStorekeeper = true;
                    break;
                }
            }

            if (!hasStorekeeper) {
                throw new IllegalArgumentException("the destination site (" + dest.getDestinationSite() + ") does not have a storekeeper scheduled to work during the estimated arrival time.");
            }
        }
    }
}