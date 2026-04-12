package domain;

public enum Role {
    SHIFT_MANAGER(true),
    CASHIER(false),         
    WAREHOUSE_WORKER(false), 
    GENERAL(false);        

    private final boolean canCancelTransactions;

    Role(boolean canCancelTransactions) {
        this.canCancelTransactions = canCancelTransactions;
    }

    public boolean canCancelTransactions() {
        return canCancelTransactions;
}
