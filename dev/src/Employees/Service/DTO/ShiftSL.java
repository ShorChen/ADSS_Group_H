package Employees.Service.DTO;
import java.time.LocalDateTime;

public record ShiftSL(
        int shiftId,
        int branchId,
        int year,
        int week,
        LocalDateTime startDate,
        String day,
        String shiftType
) {}