package domain;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class HRFacade {
    private Map<String, Employee> employees;
    private Map<String, Role> roles;
    private List<Shift> shifts;

    public HRFacade() {
        this.employees = new HashMap<>();
        this.roles = new HashMap<>();
        this.shifts = new ArrayList<>();
    }   

    public void initSystemData() {
        // Initialize system data here        
    }
}
