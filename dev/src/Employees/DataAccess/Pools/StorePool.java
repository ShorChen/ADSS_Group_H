package Employees.DataAccess.Pools;

import Employees.DataAccess.StoreDAO;

import java.util.ArrayList;
import java.util.List;

public class StorePool implements StoreDAO {
    private boolean firstStartUp;
    private List<String> closedDays;
    private static StorePool instance;

    public static StorePool Instance() {
        if (instance == null)
            instance = new StorePool();
        return instance;
    }

    private StorePool() {
        closedDays = new ArrayList<>();
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

    public static void free() {
        instance = null;
    }
}