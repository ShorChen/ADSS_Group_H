package domain.services;

import data_access.pools.StorePool;

public class FirstScreenService {
    private final StorePool store;


    public boolean isFirstStartUp() {
        return store.isFirstStartUp();
    }

    public FirstScreenService() {
        store = StorePool.Instance();
    }

    public void setFirstStartUpAsFalse() {
        store.setFirstStartUp(false);
    }

}
