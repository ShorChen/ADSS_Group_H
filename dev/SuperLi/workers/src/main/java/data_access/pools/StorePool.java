package data_access.pools;

import data_access.entities.BranchEntity;
import data_access.entities.keys.BranchKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorePool {
    private List<String> closedDays;
    private static StorePool instance;

    public static StorePool Instance() {
        if (instance == null)
            instance = new StorePool();
        return instance;
    }

    private StorePool() {
        closedDays = new ArrayList<>();
    }

    public List<String> getClosedDays() {
        return new ArrayList<>(closedDays);
    }

    public void setClosedDays(List<String> closedDays) {
        this.closedDays = new ArrayList<>(closedDays);
    }

}
