package domain.services;

import data_access.pools.BranchPool;
import data_access.pools.StorePool;
import domain.entities.store.Branch;
import presentation.model.StoreDetailsPL;
import shared.enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsService {
    private BranchPool branchPool;
    private StorePool storePool;

    public StoreDetailsService() {
        branchPool = BranchPool.Instance();
        storePool = StorePool.Instance();
    }

    public void addUpdateStoreDetails(StoreDetailsPL storeDetailsPL) {
        branchPool = BranchPool.Instance();
        storePool = StorePool.Instance();
    }

    public void addUpdateBranch(Branch branch) {
        branchPool.addUpdateBranch(branch.toEntity());
    }

    public boolean containsBranchAtLocation(String location) {
        return branchPool.getAllBranchLocations().contains(location);
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
