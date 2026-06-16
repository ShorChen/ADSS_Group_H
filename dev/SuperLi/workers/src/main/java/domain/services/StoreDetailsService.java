package domain.services;

import data_access.entities.keys.BranchKey;
import data_access.entities.keys.BranchWeekKey;
import data_access.entities.keys.WeekKey;
import data_access.pools.StorePool;
import presentation.model.StoreDetailsPL;
import shared.enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsService {
    private final StorePool storePool;

    public StoreDetailsService() {
        storePool = StorePool.Instance();
    }

    public void addUpdateStoreDetails(StoreDetailsPL storeDetailsPL) {
        List<String> closedDays = new ArrayList<>();
        storeDetailsPL.getClosedDays().forEach(day ->
                closedDays.add(day.toString())
        );
        storePool.setClosedDays(closedDays);
    }


    public void setClosedDays(List<String> closed) {
        storePool.setClosedDays(closed);
    }

    public List<WeekDay> getClosedDays() {
        List<WeekDay> closedDays = new ArrayList<>();
        storePool.getClosedDays().forEach(s
                -> closedDays.add(WeekDay.valueOf(s)));
        return closedDays;
    }

    public boolean isFirstWeek(int branchId, int year, int week) {
        return storePool.getStartingWeek(branchId).equals(new WeekKey(year, week));
    }
}
