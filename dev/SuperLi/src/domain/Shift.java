package domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Shift {
    private Date date;
    private ShiftType type;

    private List<ShiftRoleRequirement> requirements;
    private Map<Role, List<Employee>> assignments;

    public Shift(Date date, ShiftType type) {
        this.date = date;
        this.type = type;
        this.requirements = new ArrayList<>();
        this.assignments = new HashMap<>();
    }

    public void addRequirement(ShiftRoleRequirement requirement) {
        requirements.add(requirement);
        assignments.putIfAbsent(requirement.getRole(), new ArrayList<>());
    }

    public boolean assignEmployeeToRole(Employee employee, Role role) {
        if (!assignments.containsKey(role)) {
            throw new IllegalArgumentException("Role not required for this shift.");
        }
        List<Employee> assignedToRole = assignments.get(role);
        for (ShiftRoleRequirement req : requirements) {
            if (req.getRole().equals(role) && assignedToRole.size() >= req.getRequiredAmount()) {
                System.out.println("requirement for role is already fulfilled.");
                return false;
            }
        }

        assignedToRole.add(employee);
        return true;
        }
}
