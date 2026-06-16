package data_access.pools;

import data_access.entities.BranchEntity;
import data_access.entities.keys.BranchKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BranchPool {
    private final Map<BranchKey, BranchEntity> branches;
    private static BranchPool instance;

    public static BranchPool Instance() {
        if (instance == null)
            instance = new BranchPool();
        return instance;
    }

    private BranchPool(){
        branches = new HashMap<>();
    }


    public List<String> getAllBranchLocations() {
        Set<String> locations = new HashSet<>();
        branches.forEach((_,branch) ->
                locations.add(branch.location()));
        return new ArrayList<>(locations);
    }

    public boolean exists(int branchId) {
        return branches.containsKey(createKey(branchId));
    }

    public List<BranchEntity> getAllBranches(){
        List<BranchEntity> branchEntities = new ArrayList<>();
        branches.forEach((branchKey, branch) ->
                branchEntities.add(branch));
        return branchEntities;
    }

    public void addUpdateBranch(@NotNull BranchEntity branch) {
        branches.put(createKey(branch.branchId()), branch);
    }

    private BranchKey createKey(int branchId) {
        return new BranchKey(branchId);
    }

    public static void free(){
        instance = null;
    }
}
