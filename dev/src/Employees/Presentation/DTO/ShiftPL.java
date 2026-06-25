package Employees.Presentation.DTO;
import java.time.LocalDateTime;

public record ShiftPL(int shiftId, int branchId, int year, int week, LocalDateTime startDate, String day, String shiftType) {}