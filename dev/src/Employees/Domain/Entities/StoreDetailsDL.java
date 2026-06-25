package Employees.Domain.Entities;

import java.util.List;

public class StoreDetailsDL {
    private boolean firstStartUp;
    private List<String> closedDays;

    public StoreDetailsDL(boolean firstStartUp, List<String> closedDays) {
        this.firstStartUp = firstStartUp;
        this.closedDays = closedDays;
    }

    public boolean isFirstStartUp() { return firstStartUp; }
    public List<String> getClosedDays() { return closedDays; }
}