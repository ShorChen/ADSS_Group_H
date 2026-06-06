package Transportation.Presentation.DTO;
import java.time.LocalDate;
public record DriverPL(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {}