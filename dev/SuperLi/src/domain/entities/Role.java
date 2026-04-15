package domain.entities;

public class Role {
    public static final Role MANAGER = new Role("Manager");
    public static final Role Storekeeper = new Role("Storekeeper");
    public static final Role ShiftManager = new Role("ShiftManager");
    public static final Role Cashier = new Role("Cashier");
    private final String tag;

    public Role(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
