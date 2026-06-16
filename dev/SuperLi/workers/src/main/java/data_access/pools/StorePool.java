package data_access.pools;

import data_access.entities.keys.BranchKey;
import data_access.entities.keys.WeekKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorePool {
    private boolean firstStartUp;
    private List<String> closedDays;
    private final Map<BranchKey, WeekKey> weekStartDate;
    private static StorePool instance;

    public static StorePool Instance() {
        if (instance == null)
            instance = new StorePool();
        return instance;
    }

    private StorePool() {
        closedDays = new ArrayList<>();
        weekStartDate = new HashMap<>();
        firstStartUp = true;
    }

    public List<String> getClosedDays() {
        return new ArrayList<>(closedDays);
    }

    public void setClosedDays(List<String> closedDays) {
        this.closedDays = new ArrayList<>(closedDays);
    }

    public boolean isFirstStartUp() {
        return firstStartUp;
    }

    public void setFirstStartUp(boolean firstStartUp) {
        this.firstStartUp = firstStartUp;
    }

    public void addBranch(BranchKey branchKey , WeekKey weekKey) {
        weekStartDate.put(branchKey, weekKey);
    }

    public static void free() {
        instance = null;
    }

    public WeekKey getStartingWeek(int branchId) {
        return weekStartDate.getOrDefault(new BranchKey(branchId), WeekKey.NO_WEEK);
    }
}
