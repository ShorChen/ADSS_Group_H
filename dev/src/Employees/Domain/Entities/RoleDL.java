package Employees.Domain.Entities;

import java.util.Objects;

public class RoleDL {
    public static final RoleDL MANAGER = new RoleDL("Manager");
    public static final RoleDL BRANCH_MANAGER = new RoleDL("Branch Manager");
    public static final RoleDL Storekeeper = new RoleDL("Storekeeper");
    public static final RoleDL ShiftManager = new RoleDL("Shift Manager");
    public static final RoleDL Cashier = new RoleDL("Cashier");
    public static final RoleDL Driver = new RoleDL("Driver");

    private final String tag;
    public RoleDL(String tag) {
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
        RoleDL role = (RoleDL) o;
        return Objects.equals(this.tag, role.tag); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag); 
    }


}
