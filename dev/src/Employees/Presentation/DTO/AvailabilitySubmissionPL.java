package Employees.Presentation.DTO;

public record AvailabilitySubmissionPL(
        String employeeId,
        String dayOfWeek,
        String shiftType,
        boolean isAvailable
) {}