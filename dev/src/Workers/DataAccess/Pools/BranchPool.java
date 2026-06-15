package Workers.DataAccess.Pools;

import Workers.DataAccess.Entities.BranchEntity;
import Workers.DataAccess.Entities.Keys.BranchKey;
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

    public boolean exists(int branchId, String location) {
        return branches.containsKey(createKey(branchId, location));
    }

    public List<BranchEntity> getAllBranches(){
        List<BranchEntity> branchEntities = new ArrayList<>();
        branches.forEach((branchKey, branch) ->
                branchEntities.add(branch));
        return branchEntities;
    }

    public void addUpdateBranch(@NotNull BranchEntity branch) {
        branches.put(createKey(branch.branchId(), branch.location()), branch);
    }

    private BranchKey createKey(int branchId, String location) {
        return new BranchKey(branchId, location);
    }

    public static void free(){
        instance = null;
    }
}
