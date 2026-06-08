package Transportation.Service.DTO;
import java.time.LocalDate;
import java.util.List;
public record DeliverySL(int deliveryId, LocalDate date, String departureTime, String status, String truckLicense, String driverId, String originSite, List<DestinationSL> destinations) {}