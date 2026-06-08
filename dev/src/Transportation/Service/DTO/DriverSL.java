package Transportation.Service.DTO;
import java.time.LocalDate;
public record DriverSL(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {}