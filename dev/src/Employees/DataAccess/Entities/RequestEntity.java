package Employees.DataAccess.Entities;

import java.util.Objects;

public record RequestEntity(
        int requestId, ShiftEntity shift, String prevEmployee, String newEmployee,
        String manager,
        String prevApproved,
        String newApproved,
        String managerApproved, boolean denied) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RequestEntity entity)) return false;
        return requestId == entity.requestId && denied == entity.denied && Objects.equals(manager, entity.manager) && Objects.equals(shift, entity.shift) && Objects.equals(newEmployee, entity.newEmployee) && Objects.equals(newApproved, entity.newApproved) && Objects.equals(prevEmployee, entity.prevEmployee) && Objects.equals(prevApproved, entity.prevApproved) && Objects.equals(managerApproved, entity.managerApproved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, shift, prevEmployee, newEmployee, manager, prevApproved, newApproved, managerApproved, denied);
    }
}
