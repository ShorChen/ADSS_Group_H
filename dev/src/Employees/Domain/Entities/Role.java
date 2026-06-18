package Employees.Domain.Entities;

import java.util.Objects;

public class Role {
    public static final Role MANAGER = new Role("Manager");
    public static final Role Storekeeper = new Role("Storekeeper");
    public static final Role ShiftManager = new Role("Shift Manager");
    public static final Role Cashier = new Role("Cashier");
    public static final Role Driver = new Role("Driver");


    private final String tag;
    public Role(String tag) {
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
        Role role = (Role) o;
        return Objects.equals(this.tag, role.tag); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag); 
    }


}
