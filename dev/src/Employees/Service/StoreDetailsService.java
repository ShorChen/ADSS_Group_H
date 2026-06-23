package Employees.Service;

import Employees.DataAccess.StoreDAO;
import Employees.DataAccess.Pools.StorePool;
import Employees.Presentation.DTO.StoreDetailsPL;
import Employees.Shared.Enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsService {
    private final StoreDAO storePool;

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
                -> closedDays.add(WeekDay.fromArgs(s)));
        return closedDays;
    }
}
