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
}
