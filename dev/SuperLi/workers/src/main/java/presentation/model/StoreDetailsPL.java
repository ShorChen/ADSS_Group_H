package presentation.model;

import domain.entities.store.Branch;

import java.util.List;

public class StoreDetailsPL {
    private List<Branch> branches;
    private String manager;

    public StoreDetailsPL(List<Branch> branches, String manager) {
        this.branches = branches;
        this.manager = manager;
    }
}
