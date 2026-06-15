package Workers.Domain.Service;

import Workers.DataAccess.Pools.StorePool;
import Workers.Presentation.Model.StoreDetailsPL;
import Workers.Shared.Enums.WeekDay;

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

}
