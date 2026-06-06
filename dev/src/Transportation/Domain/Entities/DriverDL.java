package Transportation.Domain.Entities;

import java.time.LocalDate;

@SuppressWarnings("ClassCanBeRecord")
public class DriverDL {
    private final String employeeId;
    private final String name;
    private final String licenseType;
    private final LocalDate licenseExpiry;

    public DriverDL(String employeeId, String name, String licenseType, LocalDate licenseExpiry) {
        this.employeeId = employeeId;
        this.name = name;
        this.licenseType = licenseType;
        this.licenseExpiry = licenseExpiry;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getLicenseType() { return licenseType; }
    public LocalDate getLicenseExpiry() { return licenseExpiry; }
}