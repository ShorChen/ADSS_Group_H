package Employees.Service.DTO;

public record RequestSL(
        int requestId,
        int shiftId,
        String prevEmployeeId,
        String newEmployeeId,
        String managerId,
        String prevApproved,
        String newApproved,
        String managerApproved,
        boolean denied
) {}