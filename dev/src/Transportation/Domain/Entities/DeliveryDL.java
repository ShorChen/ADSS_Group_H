package Transportation.Domain.Entities;

import java.time.LocalDate;
import java.util.List;

public class DeliveryDL {
    private final int deliveryId;
    private final LocalDate date;
    private final String departureTime;
    private String status;
    private String truckLicense;
    private String driverId;
    private final String originSite;
    private final List<DestinationDL> destinations;

    public DeliveryDL(int deliveryId, LocalDate date, String departureTime, String status, String truckLicense, String driverId, String originSite, List<DestinationDL> destinations) {
        this.deliveryId = deliveryId;
        this.date = date;
        this.departureTime = departureTime;
        this.status = status;
        this.truckLicense = truckLicense;
        this.driverId = driverId;
        this.originSite = originSite;
        this.destinations = destinations;
    }

    public int getDeliveryId() { return deliveryId; }
    public LocalDate getDate() { return date; }
    public String getDepartureTime() { return departureTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTruckLicense() { return truckLicense; }
    public void setTruckLicense(String truckLicense) { this.truckLicense = truckLicense; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public String getOriginSite() { return originSite; }
    public List<DestinationDL> getDestinations() { return destinations; }
}