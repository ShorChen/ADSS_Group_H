package Transportation.Presentation.DTO;
import java.time.LocalDate;
import java.util.List;
public record DeliveryPL(int deliveryId, LocalDate date, String departureTime, String status, String truckLicense, String driverId, String originSite, List<DestinationPL> destinations) {}