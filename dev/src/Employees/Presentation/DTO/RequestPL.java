package Employees.Presentation.DTO;

public record RequestPL(int requestId, int shiftId, String prevEmployeeId, String newEmployeeId, String managerId, String prevApproved, String newApproved, String managerApproved, boolean denied) {}