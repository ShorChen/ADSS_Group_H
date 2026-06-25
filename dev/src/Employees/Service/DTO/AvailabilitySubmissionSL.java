package Employees.Service.DTO;

public record AvailabilitySubmissionSL(
        String employeeId,
        String dayOfWeek,
        String shiftType,
        boolean isAvailable
) {}