package domain.entities;

import java.util.Objects;

public class Role {
    public static final Role MANAGER = new Role("Manager");
    public static final Role Storekeeper = new Role("Storekeeper");
    public static final Role ShiftManager = new Role("Shift Manager");
    public static final Role Cashier = new Role("Cashier");

    private final String tag;
    public Role(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return Objects.equals(tag, role.tag);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(tag);
    }

    @Override
    public String toString() {
        return tag;
    }
}
