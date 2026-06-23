package Employees.Domain.DTO;

import java.util.Objects;

public class RoleSL {
    public static final RoleSL MANAGER = new RoleSL("Manager");
    public static final RoleSL BRANCH_MANAGER = new RoleSL("Branch Manager");
    public static final RoleSL Storekeeper = new RoleSL("Storekeeper");
    public static final RoleSL ShiftManager = new RoleSL("Shift Manager");
    public static final RoleSL Cashier = new RoleSL("Cashier");
    public static final RoleSL Driver = new RoleSL("Driver");

    private final String tag;
    public RoleSL(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleSL role = (RoleSL) o;
        return Objects.equals(this.tag, role.tag); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag); 
    }


}
