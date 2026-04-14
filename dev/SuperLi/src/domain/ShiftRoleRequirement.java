package domain;

public class ShiftRoleRequirement {
    private Role role;
    private int requiredAmount;

    public ShiftRoleRequirement(Role role, int requiredAmount) {
        this.role = role;
        this.requiredAmount = requiredAmount;
    }

    public Role getRole() {
        return role;
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
}
